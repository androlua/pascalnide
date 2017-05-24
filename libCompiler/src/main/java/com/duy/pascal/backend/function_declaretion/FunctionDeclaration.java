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

package com.duy.pascal.backend.function_declaretion;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.define.OverridingFunctionBodyException;
import com.duy.pascal.backend.exceptions.define.SameNameException;
import com.duy.pascal.backend.exceptions.syntax.ExpectedTokenException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.basic.ColonToken;
import com.duy.pascal.backend.tokens.basic.CommaToken;
import com.duy.pascal.backend.tokens.basic.ForwardToken;
import com.duy.pascal.backend.tokens.basic.SemicolonToken;
import com.duy.pascal.backend.tokens.basic.VarToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.tokens.grouping.ParenthesizedToken;
import com.js.interpreter.NamedEntity;
import com.js.interpreter.VariableDeclaration;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.codeunit.library.UnitPascal;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.expressioncontext.ExpressionContextMixin;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.runtime.FunctionOnStack;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FunctionDeclaration extends AbstractCallableFunction {
    final public ExpressionContextMixin declarations;
    /**
     * name of function or procedure
     */
    public String name;
    public Executable instructions;
    /**
     * this is the store class of function
     */
    public VariableDeclaration resultDefinition;

    public LineInfo line;

    /* These go together ----> */
    public String[] argumentNames;

    public RuntimeType[] argumentTypes;
    private boolean isProcedure = false;
    private boolean bodyDeclared;

    public FunctionDeclaration(ExpressionContext parent, GrouperToken grouperToken,
                               boolean isProcedure) throws ParsingException {
        this.declarations = new FunctionExpressionContext(this, parent);
        this.line = grouperToken.peek().getLineInfo();
        this.isProcedure = isProcedure;
        name = grouperToken.nextWordValue();

        getArgumentsForDeclaration(grouperToken, isProcedure);
        Token next = grouperToken.peek();
        if (isProcedure == next instanceof ColonToken) {
            throw new ParsingException(next.getLineInfo(),
                    "Functions must have a return operator, and procedures cannot have one");
        }
        if (!isProcedure) {
            next = grouperToken.take();
            //define variable result of function, the name of variable same as name function
            resultDefinition = new VariableDeclaration(name,
                    grouperToken.getNextPascalType(declarations), line);
            this.declarations.declareVariable(resultDefinition);
        }

        grouperToken.assertNextSemicolon(next);

        instructions = null;
        NamedEntity n = parent.getConstantDefinition(name);
        if (n != null) {
            throw new SameNameException(n, this);
        }
        n = parent.getVariableDefinition(name);
        if (n != null) {
            throw new SameNameException(n, this);
        }
    }

    public FunctionDeclaration(ExpressionContext p) {
        this.declarations = new FunctionExpressionContext(this, p);
        this.argumentNames = new String[0];
        this.argumentTypes = new RuntimeType[0];
    }

    public String getName() {
        return name;
    }

    public void parseFunctionBody(GrouperToken i) throws ParsingException {
        Token next = i.peekNoEOF();
        if (next instanceof ForwardToken) {
            Token take = i.take();
            i.assertNextSemicolon(take);
        } else {
            if (instructions != null) {
                throw new OverridingFunctionBodyException(this, i.getLineInfo());
            }
            while (!bodyDeclared) {
                declarations.addNextDeclaration(i);
            }
        }
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Object call(VariableContext parentcontext,
                       RuntimeExecutableCodeUnit<?> main, Object[] arguments)
            throws RuntimePascalException {
        if (this.declarations.root() instanceof UnitPascal) {
            parentcontext = main.getLibrary((UnitPascal) declarations.root());
        }
        return new FunctionOnStack(parentcontext, main, this, arguments).execute();
    }

    private void getArgumentsForDeclaration(GrouperToken i, boolean is_procedure)
            throws ParsingException { // need
        List<WordToken> namesList = new ArrayList<>();
        List<RuntimeType> typesList = new ArrayList<>();
        Token next = i.peek();
        if (next instanceof ParenthesizedToken) {
            ParenthesizedToken argumentsToken = (ParenthesizedToken) i.take();
            while (argumentsToken.hasNext()) {
                int j = 0; // counts number added of this operator
                next = argumentsToken.take();
                boolean is_varargs = false;
                if (next instanceof VarToken) {
                    is_varargs = true;
                    next = argumentsToken.take();
                }
                while (true) {
                    namesList.add((WordToken) next);
                    j++;
                    next = argumentsToken.take();
                    if (next instanceof CommaToken) {
                        next = argumentsToken.take();
                    } else {
                        break;
                    }
                }

                if (!(next instanceof ColonToken)) {
                    throw new ExpectedTokenException(":", next);
                }
                DeclaredType type;
                type = argumentsToken.getNextPascalType(declarations);

                while (j > 0) {
                    typesList.add(new RuntimeType(type, is_varargs));
                    j--;
                }
                if (argumentsToken.hasNext()) {
                    next = argumentsToken.take();
                    if (!(next instanceof SemicolonToken)) {
                        throw new ExpectedTokenException(";", next);
                    }
                }
            }
        }
        argumentTypes = typesList.toArray(new RuntimeType[typesList.size()]);
        argumentNames = new String[namesList.size()];
        for (int j = 0; j < argumentNames.length; j++) {
            WordToken n = namesList.get(j);
            argumentNames[j] = n.name;
            // TODO: 30-Apr-17
//            declarations.declareVariable(new VariableDeclaration(n.name, argumentTypes[j].declType, n.lineInfo));
        }

    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    @Nullable
    public DeclaredType returnType() {
        return resultDefinition == null ? null : resultDefinition.type;
    }

    public boolean headerMatches(AbstractFunction other) throws ParsingException {
        if (name.equals(other.name())
                && Arrays.equals(argumentTypes, other.argumentTypes())) {
            if (resultDefinition == null && other.returnType() == null) {
                return true;
            }
            if (resultDefinition == null || other.returnType() == null
                    || !resultDefinition.equals(other.returnType())) {
                System.err.println("Warning: " +
                        "Overriding previously declared return operator for function " + name);
            }
            return true;
        }
        return false;
    }

    @Override
    public String getEntityType() {
        return isProcedure ? "procedure" : "function";
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    public boolean isProcedure() {
        return isProcedure;
    }

    private class FunctionExpressionContext extends ExpressionContextMixin {
        FunctionDeclaration function;

        public FunctionExpressionContext(FunctionDeclaration function, ExpressionContext parent) {
            super(parent.root(), parent);
            this.function = function;
        }

        @Override
        public Executable handleUnrecognizedStatementImpl(Token next, GrouperToken container)
                throws ParsingException {
            return null;
        }

        @Override
        public boolean handleUnrecognizedDeclarationImpl(Token next, GrouperToken container)
                throws ParsingException {
            if (next instanceof ForwardToken) {
                container.assertNextSemicolon(next);
                bodyDeclared = true;
                return true;
            }
            return false;
        }


        @Override
        public void handleBeginEnd(GrouperToken i) throws ParsingException {
            bodyDeclared = true;
            instructions = i.getNextCommand(declarations);
            i.assertNextSemicolon(i.next);
        }

        @Override
        public VariableDeclaration getVariableDefinitionLocal(String ident) {
            VariableDeclaration unitVariableDecl = super.getVariableDefinitionLocal(ident);
            if (unitVariableDecl != null) {
                return unitVariableDecl;
            }
            for (int i = 0; i < argumentNames.length; i++) {
                if (argumentNames[i].equals(ident)) {
                    return new VariableDeclaration(argumentNames[i], argumentTypes[i].declType, function.line);
                }
            }
            return null;
        }
    }

}
