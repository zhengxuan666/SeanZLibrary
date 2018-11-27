package com.seanz.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.seanz.demo.R;
import com.seanz.library.base.BaseActivity;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected int getStatusBarColor() {
        return 0;
    }

    @Override
    protected void setScreenManager() {

    }

    @Override
    protected int getActivityLayoutId() {
        return 0;
    }
}
