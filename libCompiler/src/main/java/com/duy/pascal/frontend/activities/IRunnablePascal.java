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

package com.duy.pascal.frontend.activities;

import android.content.Context;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.builtin_libraries.io.IOLib;
import com.duy.pascal.backend.builtin_libraries.io.InOutListener;
import com.duy.pascal.frontend.view.exec_screen.console.ConsoleView;

/**
 * Created by Duy on 29-May-17.
 */

public interface IRunnablePascal extends ExecHandler, InOutListener, ActivityHandler {
    @Override
    String getCurrentDirectory();

    @Override
    Context getApplicationContext();


    @Override
    void startInput(IOLib lock);

    @Override
    void print(CharSequence charSequence);

    @Override
    @Nullable
    ConsoleView getConsoleView();

    @Override
    void println(CharSequence charSequence);

    @Override
    char getKeyBuffer();

    @Override
    boolean keyPressed();

    @Override
    void clearConsole();
}
