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

package com.duy.pascal.ui.autocomplete.completion.model;

import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.VarargsType;

import static com.duy.pascal.ui.editor.view.AutoIndentEditText.CURSOR;

/**
 * Created by Duy on 17-Aug-17.
 */

public class FunctionDescription implements Description {

    private final Name name;
    private final ArgumentType[] argumentTypes;
    private final Type type;
    private boolean isProcedure;

    public FunctionDescription(Name name, ArgumentType[] args, Type type, boolean isProcedure) {
        this.name = name;
        this.argumentTypes = args;
        this.type = type;
        this.isProcedure = isProcedure;
    }

    @Override
    public String getInsertText() {
        if (argumentTypes != null && argumentTypes.length > 0) {
            return String.format("%s(%s", name.getOriginName(), CURSOR + ")");
        } else {
            if (isProcedure) {
                return String.format("%s(%s", name.getOriginName(), ");" + CURSOR);
            }else {
                return String.format("%s(%s", name.getOriginName(), ")" + CURSOR);
            }
        }
    }

    @Override
    public String getHeader() {
        String out = name.getOriginName() + "(";
        if (argumentTypes != null && argumentTypes.length > 0) {
            for (int i = 0; i < argumentTypes.length; i++) {
                ArgumentType arg = argumentTypes[i];
                if (arg instanceof RuntimeType) {
                    Type rawType = ((RuntimeType) arg).getRawType();
                    out += rawType.toString();
                } else if (arg instanceof VarargsType) {
                    out += "...";
                }
                if (i != argumentTypes.length - 1) out += ",";
            }
        }
        out += ")";
        return out;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return name.getOriginName();
    }

    public Type getType() {
        return type;
    }

    public Integer getKind() {
        return DescriptionImpl.KIND_FUNCTION;
    }
}
