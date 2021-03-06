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

package com.duy.pascal.interperter.exceptions.runtime.arith;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.Spanned;

import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineInfo;

public class PascalArithmeticException extends RuntimePascalException {
    public ArithmeticException error;

    public PascalArithmeticException(LineInfo line, ArithmeticException e) {
        super(line);
        this.error = e;
    }

    /**
     * Eg. Sqrt(-2) return NaN, throw exception number lower zero
     *
     * @param resId - localized message
     */
    public PascalArithmeticException(@StringRes int resId, Object... args) {
        super(resId, args);
    }

    @Override
    public String getMessage() {
        return "Arithmetic Exception: " + error.getMessage();
    }

    @Override
    public Spanned getLocalizedMessage(@NonNull Context context) {
        return super.getLocalizedMessage(context);
    }
}
