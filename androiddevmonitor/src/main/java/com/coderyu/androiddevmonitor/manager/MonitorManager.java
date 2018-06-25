package com.coderyu.androiddevmonitor.manager;

import android.content.Context;

import com.coderyu.androiddevmonitor.IMonitor;
import com.coderyu.androiddevmonitor.MonitorType;
import com.coderyu.androiddevmonitor.MonitorWindow;
import com.coderyu.androiddevmonitor.logmonitor.LogMonitor;

import java.util.ArrayList;

/**
 * Created by coder_yu on 18/6/25.
 */

public class MonitorManager {

    private int mMonitorIndex = 0;

    private ArrayList<IMonitor> mMonitors;
    private static MonitorManager sInstance;

    private MonitorManager(int monitorType) {
        addMonitors(monitorType);
    }

    public static void init(Context context, int monitorType) {
        MonitorWindow.init(context);
        sInstance = new MonitorManager(monitorType);
    }

    public static MonitorManager getInstance() {
        return sInstance;
    }

    private void addMonitors(int monitorType) {
        mMonitors = new ArrayList<>();
        if ((monitorType & MonitorType.LOG) != 0) {
            mMonitors.add(new LogMonitor());
        }
    }

    public void startMonitor() {
        currMonitor().show();
    }

    private IMonitor currMonitor() {
        return mMonitors.get(mMonitorIndex);
    }

    public void stopMonitor() {

    }
}
