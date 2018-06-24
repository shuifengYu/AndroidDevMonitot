package com.coderyu.androiddevmonitottest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.coderyu.androiddevmonitor.MonitorWindow;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MonitorWindow.init(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
