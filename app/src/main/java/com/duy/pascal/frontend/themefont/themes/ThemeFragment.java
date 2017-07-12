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

package com.duy.pascal.frontend.themefont.themes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.utils.StoreUtil;
import com.duy.pascal.frontend.utils.Utils;

/**
 * Created by Duy on 17-May-17.
 */

public class ThemeFragment extends Fragment {

    public static final int FONT = 0;
    public static final int THEME = 1;
    @Nullable
    private ThemeAdapter codeThemeAdapter;
    private RecyclerView mRecyclerView;
    @Nullable
    private OnThemeSelectListener onThemeSelect;

    public static ThemeFragment newInstance() {
        Bundle args = new Bundle();
        ThemeFragment fragment = new ThemeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onThemeSelect = (OnThemeSelectListener) getActivity();
        } catch (Exception ignored) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_theme, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnDonate = view.findViewById(R.id.btn_donate);
        if (Utils.DONATED) {
            btnDonate.setText(R.string.create_new_theme);
            btnDonate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), CustomThemeActivity.class));
                }
            });
        } else {
            btnDonate.setText(R.string.more_font);
            btnDonate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StoreUtil.gotoPlayStore(getActivity(), Utils.DONATE_PACKAGE);
                }
            });
        }


        mRecyclerView = view.findViewById(R.id.recycler_view);

        codeThemeAdapter = new ThemeAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(codeThemeAdapter);
        codeThemeAdapter.setOnThemeSelectListener(onThemeSelect);
    }


    public RecyclerView.Adapter getAdapter() {
        return codeThemeAdapter;
    }

    public void notifyDataSetChanged() {
        if (codeThemeAdapter != null) {
            codeThemeAdapter.notifyDataSetChanged();
        }
    }

    public interface OnThemeSelectListener {
        void onThemeSelect(String name);
    }

}