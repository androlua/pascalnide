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

package com.duy.pascal.interperter.systemfunction.io;


import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.systemfunction.builtin.IMethodDeclaration;
import com.duy.pascal.interperter.ast.node.Node;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime.references.PascalReference;
import com.duy.pascal.interperter.ast.runtime.value.FunctionCall;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.boxing.ArrayBoxer;
import com.duy.pascal.interperter.libraries.file.FileLib;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.VarargsType;

import java.io.File;

/**
 * Casts an object to the class or the interface represented
 */
public class WritelnFileFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes =
            {new RuntimeType(BasicType.Text, true),
                    new VarargsType(new RuntimeType(BasicType.create(Object.class), false))};

    @Override
    public Name getName() {
        return Name.create("WriteLn");

    }

    @Override
    public FunctionCall generateCall(LineInfo line, RuntimeValue[] arguments,
                                     ExpressionContext f) throws Exception {
        return new WriteLineFileCall(arguments[0], arguments[1], line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line, RuntimeValue[] values, ExpressionContext f) throws Exception {
        return generateCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    public Type returnType() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    private class WriteLineFileCall extends FunctionCall {
        private RuntimeValue args;
        private LineInfo line;
        private RuntimeValue filePreference;

        WriteLineFileCall(RuntimeValue filePreferences, RuntimeValue args, LineInfo line) {
            this.filePreference = filePreferences;
            this.args = args;
            this.line = line;
        }

        @NonNull
        @Override
        public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
            return null;
        }

        @NonNull
        @Override
        public LineInfo getLineNumber() {
            return line;
        }

        @Override
        public void setLineNumber(LineInfo lineNumber) {

        }

        @Override
        public Object compileTimeValue(CompileTimeContext context) {
            return null;
        }

        @Override
        public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
                throws Exception {
            return new WriteLineFileCall(filePreference, args, line);
        }

        @Override
        public Node compileTimeConstantTransform(CompileTimeContext c)
                throws Exception {
            return new WriteLineFileCall(filePreference, args, line);
        }

        @Override
        protected Name getFunctionName() {
            return Name.create("WriteLn");
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
                throws RuntimePascalException {
            FileLib fileLib = main.getDeclaration().getContext().getFileHandler();
            ArrayBoxer arrayBoxer = (ArrayBoxer) args;
            Object[] values = (Object[]) arrayBoxer.getValue(f, main);

            PascalReference<File> file = (PascalReference<File>) filePreference.getValue(f, main);

            fileLib.writeFile(file.get(), values);
            fileLib.writeFile(file.get(), "\n");
            return null;
        }

    }
}
