package com.duy.pascal.interperter.ast.runtime.value;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineInfo;

public interface RuntimeValue {

    /**
     * @return value of variable or function, or constant
     */
    @NonNull
    Object getValue(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;

    /**
     * @return type of variable, function, or constant
     */
    @NonNull
    RuntimeType getRuntimeType(ExpressionContext context) throws Exception;

    /**
     * @return line of code
     */
    LineInfo getLineNumber();

    void setLineNumber(LineInfo lineNumber);

    /*
     * return null if not a compile-time constant.
     */
    @Nullable
    Object compileTimeValue(CompileTimeContext context) throws Exception;

    @Nullable
    RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws Exception;

    /**
     * reference of variable, set and get value
     */
    @Nullable
    AssignableValue asAssignableValue(ExpressionContext context);

}
