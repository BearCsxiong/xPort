package com.base.project.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.base.project.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
