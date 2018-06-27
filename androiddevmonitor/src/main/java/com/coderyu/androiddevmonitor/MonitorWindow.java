package com.coderyu.androiddevmonitor;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by coder_yu on 18/6/24.
 */

public class MonitorWindow {
    private static Context mContext;
    private static MonitorWindow sInstance;

    /**
     * 按下的时间戳
     */
    private int CLICK_DURATION = 300;
    private int lastRawX;
    private int lastRawY;
    private int firstX;
    private int firstY;
    private int maxOffsetX;
    private int maxOffsetY;
    private long lastTimeDown;
    private int mLastL;
    private int mLastT;
    private int mLastR;
    private int mLastB;

    WindowManager mWindowManager;
    ViewGroup mRootView;
    private ViewGroup mMonitorContainer;

    private MonitorWindow(Context context) {
        mContext = context.getApplicationContext();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mRootView = rootView(mContext);
        Point point = new Point();
        mWindowManager.getDefaultDisplay().getSize(point);
        mWindowManager.addView(mRootView, params(point));
    }

    public static void init(Context context) {
        if (sInstance != null) {
        }
        if (context == null) {
            throw new RuntimeException("Context can not be null!!!");
        }
        sInstance = new MonitorWindow(context);
    }

    private ViewGroup.LayoutParams params(Point point) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = point.x;
        params.height = point.y - 500;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.BOTTOM;

        return params;
    }

    private ViewGroup rootView(Context context) {
        final ViewGroup rootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.window_monitor, null);

        final View btnOpen = rootView.findViewById(R.id.monitor_btn_open);
        View tvClose = rootView.findViewById(R.id.monitor_tv_close);

        final View openView = rootView.findViewById(R.id.monitor_openview);
        mMonitorContainer = (ViewGroup) openView.findViewById(R.id.monitor_main);
        btnOpen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int rawX = (int) event.getRawX();
                int rawY = (int) event.getRawY();

                float deltaX, deltaY;
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        lastTimeDown = System.currentTimeMillis();
                        lastRawX = rawX;
                        lastRawY = rawY;
                        firstX = rawX;
                        firstY = rawY;
                        maxOffsetX = ((ViewGroup) v.getParent()).getWidth() - v.getWidth();
                        maxOffsetY = ((ViewGroup) v.getParent()).getHeight() - v.getHeight();
                        break;
                    case MotionEvent.ACTION_UP:
                        deltaX = Math.abs(lastRawX - firstX);
                        deltaY = Math.abs(lastRawY - firstY);
                        float duringTime = System.currentTimeMillis() - lastTimeDown;
                        if (deltaX < 5 && deltaY < 5 && duringTime < CLICK_DURATION) {
                            btnOpen.setVisibility(View.GONE);
                            openView.setVisibility(View.VISIBLE);
                            MonitorManager.getInstance().startMonitor();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        deltaX = rawX - lastRawX;
                        deltaY = rawY - lastRawY;
                        moveLayout(v, (int) deltaX, (int) deltaY);
                        lastRawX = rawX;
                        lastRawY = rawY;
                        break;
                }

                return true;
            }
        });


        tvClose.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           openView.setVisibility(View.GONE);
                                           btnOpen.setVisibility(View.VISIBLE);
                                           MonitorManager.getInstance().stopMonitor();
                                       }
                                   }
        );
        return rootView;
    }

    private void moveLayout(View v, int deltaX, int deltaY) {
        mLastL = v.getLeft() + deltaX;
        mLastT = v.getTop() + deltaY;
        if (mLastL > maxOffsetX) {
            mLastL = maxOffsetX;
        }
        if (mLastT > maxOffsetY) {
            mLastT = maxOffsetY;
        }
        mLastR = mLastL + v.getWidth();
        mLastB = mLastT + v.getHeight();
        v.layout(mLastL, mLastT, mLastR, mLastB);
    }


    public void show(Monitor monitor) {
        monitor.show(mMonitorContainer);
    }

    public static MonitorWindow getInstance() {
        return sInstance;
    }

    public void stop() {

    }
}
