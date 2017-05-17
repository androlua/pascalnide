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

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code.CompileManager;
import com.duy.pascal.frontend.code_editor.EditorActivity;
import com.duy.pascal.frontend.file.ApplicationFileManager;

import java.io.IOException;
import java.util.Arrays;


public class ActivitySplashScreen extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST = 11;
    private static final String TAG = ActivitySplashScreen.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        // Here, this is the current activity
        PreferenceManager.setDefaultValues(this, R.xml.setting_editor, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        } else {
            startMainActivity();
        }
        try {
            String[] fontses = getAssets().list("fonts");
            Log.i(TAG, "onCreate: " + Arrays.toString(fontses));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startMainActivity();
                } else {
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void startMainActivity() {
       /* try {
            JavaClassLoader javaClassLoader = new JavaClassLoader(this);
            Class classMath = (Class) javaClassLoader.loadClass("java.util.ArrayList");
            Object o = classMath.newInstance();
            Method[] declaredMethods = o.getClass().getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                Log.i(TAG, "startMainActivity: " + declaredMethod);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        Intent data = getIntent();
        String action = data.getAction();
        String type = data.getType();
        final Intent intentEdit = new Intent(ActivitySplashScreen.this, EditorActivity.class);
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.equals("text/plain")) {
                handleActionSend(data, intentEdit);
            }
        } else if (Intent.ACTION_VIEW.equals(action) && type != null) {
            handleActionView(data, intentEdit);
        } else if (action.equalsIgnoreCase("run_from_shortcut")) {
            handleRunProgram(data);
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intentEdit.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0, 0);
                startActivity(intentEdit);
                finish();
            }
        }, 400);
    }

    private void handleRunProgram(Intent data) {
        Intent runIntent = new Intent(this, ExecuteActivity.class);
        runIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        runIntent.putExtra(CompileManager.FILE_PATH,
                data.getStringExtra(CompileManager.FILE_PATH));
        overridePendingTransition(0, 0);
        startActivity(runIntent);
        finish();
    }

    private void handleActionView(Intent from, Intent to) {
        if (from.getData().toString().endsWith(".pas")) {
            Uri uriPath = from.getData();
            Log.d(TAG, "handleActionView: " + uriPath.getPath());
            to.putExtra(CompileManager.FILE_PATH, uriPath.getPath());
        }
    }

    private void handleActionSend(Intent from, Intent to) {
        String text = from.getStringExtra(Intent.EXTRA_TEXT);

        ApplicationFileManager fileManager = new ApplicationFileManager(this);
        //create new temp file
        String filePath = fileManager.createNewFile(ApplicationFileManager.getApplicationPath() +
                "new_" + Integer.toHexString((int) System.currentTimeMillis()) + ".pas");
        fileManager.saveFile(filePath, text);
        to.putExtra(CompileManager.FILE_PATH, filePath);
    }
}
