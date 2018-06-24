package com.coderyu.androiddevmonitor.logmonitor;

import android.graphics.Color;

import com.coderyu.androiddevmonitor.IMonitor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by coder_yu on 18/6/24.
 */

public class LogMonitor implements IMonitor {
    public static final int D = Color.parseColor("#ff1122");
    public static final int I = Color.parseColor("#ff11FF");
    public static final int W = Color.parseColor("#111122");
    public static final int E = Color.parseColor("#ff4422");
    private static LogQueue mLogQueue;

    private static SimpleDateFormat sdf;

    @Override
    public void show() {

    }

    public static void d(String TAG, String message) {
        add(D, getLogMessage(TAG, message));
    }

    public static void i(String TAG, String message) {
        add(I, getLogMessage(TAG, message));
    }

    public static void w(String TAG, String message) {
        add(W, getLogMessage(TAG, message));
    }

    public static void e(String TAG, String message) {
        add(E, getLogMessage(TAG, message));
    }

    public synchronized static void add(int color, String logMessage) {
        mLogQueue.add(new LogQueue.LogQueueItem(color, logMessage));
    }

    private static String getLogMessage(String TAG, String message) {
        return time() + "TAG:" + TAG + "," + message;
    }

    private static String time() {
        if (sdf == null) {
            sdf = new SimpleDateFormat("MM-dd HH:mm:ss SSS-->");
        }
        return sdf.format(new Date());
    }

}
