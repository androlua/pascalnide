/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.frontend.sample;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.view.code_view.CodeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Duy on 20-Apr-17.
 */

class CodeHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.img_play)
    View btnPlay;
    @BindView(R.id.img_edit)
    View btnEdit;
    @BindView(R.id.img_copy)
    View btnCopy;
    @BindView(R.id.code_view)
    CodeView codeView;


    public CodeHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}