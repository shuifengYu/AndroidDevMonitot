package com.coderyu.androiddevmonitor;

import android.content.Context;

import com.coderyu.androiddevmonitor.logmonitor.LogMonitor;

import java.util.ArrayList;

/**
 * Created by coder_yu on 18/6/25.
 */

public class MonitorManager {

    private int mMonitorIndex = 0;

    private ArrayList<Monitor> mMonitors;
    private static MonitorManager sInstance;

    private MonitorManager(Context context, int monitorType) {
        addMonitors(context, monitorType);
    }

    public static void init(Context context, int monitorType) {
        MonitorWindow.init(context);
        sInstance = new MonitorManager(context, monitorType);
    }


    public static MonitorManager getInstance() {
        return sInstance;
    }

    private void addMonitors(Context context, int monitorType) {
        mMonitors = new ArrayList<>();
        if ((monitorType & MonitorType.LOG) != 0) {
            mMonitors.add(LogMonitor.create(context));
        }
    }

    public Monitor currMonitor() {
        return mMonitors.get(mMonitorIndex);
    }

}
