package com.coderyu.androiddevmonitottest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.coderyu.androiddevmonitor.MonitorManager;
import com.coderyu.androiddevmonitor.MonitorType;
import com.coderyu.androiddevmonitor.logmonitor.LogMonitor;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static int sIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MonitorManager.init(this, MonitorType.LOG);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int i = sIndex % 4;
                    if (i == 0) {
                        LogMonitor.d(TAG, "哈哈，我在测试日志显示工具，当期是" + sIndex);
                    } else if (i == 1) {
                        LogMonitor.i(TAG, "哈哈，我在测试日志显示工具，当期是" + sIndex);
                    } else if (i == 2) {
                        LogMonitor.w(TAG, "哈哈，我在测试日志显示工具，当期是" + sIndex);
                    } else if (i == 3) {
                        LogMonitor.e(TAG, "哈哈，我在测试日志显示工具，当期是" + sIndex);
                    }
                    try {
                        Thread.sleep(500);
                        sIndex++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
