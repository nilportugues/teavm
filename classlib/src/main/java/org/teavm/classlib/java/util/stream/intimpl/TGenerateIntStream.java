/*
 *  Copyright 2017 Alexey Andreev.
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
package org.teavm.classlib.java.util.stream.intimpl;

import java.util.function.IntPredicate;
import java.util.function.IntSupplier;

public class TGenerateIntStream extends TSimpleIntStreamImpl {
    private IntSupplier s;

    public TGenerateIntStream(IntSupplier s) {
        this.s = s;
    }

    @Override
    protected boolean next(IntPredicate consumer) {
        while (consumer.test(s.getAsInt())) {
            // go on
        }
        return true;
    }
}