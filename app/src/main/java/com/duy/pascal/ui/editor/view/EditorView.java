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

package com.duy.pascal.ui.editor.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;

/**
 * Created by Duy on 15-Mar-17.
 */

public class EditorView extends UndoRedoSupportEditText {

    public EditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomSelectionActionModeCallback(new EditorActionCallback(this));
    }
    public EditorView(Context context) {
        super(context);
        setCustomSelectionActionModeCallback(new EditorActionCallback(this));
    }

    public EditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomSelectionActionModeCallback(new EditorActionCallback(this));

    }


    public void toast(int resId, Object... arg) {
        Toast.makeText(getContext(), getContext().getString(resId, arg), Toast.LENGTH_SHORT).show();
    }
}
