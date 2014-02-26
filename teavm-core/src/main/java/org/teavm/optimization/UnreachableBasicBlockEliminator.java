/*
 *  Copyright 2013 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.optimization;

import org.teavm.model.BasicBlock;
import org.teavm.model.Program;
import org.teavm.model.TryCatchBlock;
import org.teavm.model.util.InstructionTransitionExtractor;

/**
 *
 * @author Alexey Andreev
 */
public class UnreachableBasicBlockEliminator {
    public void optimize(Program program) {
        if (program.basicBlockCount() == 0) {
            return;
        }
        InstructionTransitionExtractor transitionExtractor = new InstructionTransitionExtractor();
        boolean[] reachable = new boolean[program.basicBlockCount()];
        int[] stack = new int[program.basicBlockCount()];
        int top = 0;
        stack[top++] = 0;
        while (top > 0) {
            int i = stack[--top];
            if (reachable[i]) {
                continue;
            }
            reachable[i] = true;
            BasicBlock block = program.basicBlockAt(i);
            block.getLastInstruction().acceptVisitor(transitionExtractor);
            for (BasicBlock successor : transitionExtractor.getTargets()) {
                stack[top++] = successor.getIndex();
            }
            for (TryCatchBlock tryCatch : block.getTryCatchBlocks()) {
                stack[top++] = tryCatch.getHandler().getIndex();
            }
        }
        for (int i = 0; i < reachable.length; ++i) {
            if (!reachable[i]) {
                program.deleteBasicBlock(i);
            }
        }
        program.pack();
    }
}
