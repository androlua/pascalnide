/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.backend.ast.runtime_value.value;

/**
 * Created by Duy on 13-Jun-17.
 */

public class NullValue {
    private static NullValue NULL;

    private NullValue() {
    }

    public static NullValue get() {
        if (NULL == null) {
            NULL = new NullValue();
        }
        return NULL;
    }

    @Override
    public String toString() {
        return "znull";
    }

    @Override
    public boolean equals(Object obj) {
        return obj == null || obj instanceof NullValue;
    }
}
