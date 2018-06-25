package com.coderyu.androiddevmonitor;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.coderyu.androiddevmonitor.manager.MonitorManager;

/**
 * Created by coder_yu on 18/6/24.
 */

public class MonitorWindow {
    private static Context mContext;

    /**
     * 按下的时间戳
     */
    private static long mDownTime;
    private static int CLICK_DURATION = 300;
    private static float lastX;
    private static float lastY;
    private static int lastRawX;
    private static int lastRawY;
    private static int firstX;
    private static int firstY;
    private static int maxOffsetX;
    private static int maxOffsetY;
    private static long lastTimeDown;
    private static int mLastL;
    private static int mLastT;
    private static int minOffsetX;
    private static int minOffsetY;
    private static int mLastR;
    private static int mLastB;

    public static void init(Context context) {
        mContext = context;
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        windowManager.addView(rootView(mContext), params(point));
    }

    private static ViewGroup.LayoutParams params(Point point) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = point.x;
        params.height = point.y - 500;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.TOP;

        return params;
    }

    private static View rootView(Context context) {
        final View rootView = LayoutInflater.from(context).inflate(R.layout.window_monitor, null);

        final View btnOpen = rootView.findViewById(R.id.monitor_btn_open);
        View tvClose = rootView.findViewById(R.id.monitor_tv_close);
        final View openView = rootView.findViewById(R.id.monitor_openview);

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
                        if (deltaX < 5 && deltaY < 5 && duringTime < 500) {
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

    private static void moveLayout(View v, int deltaX, int deltaY) {
        mLastL = v.getLeft() + deltaX;
        mLastT = v.getTop() + deltaY;
        if (mLastL < minOffsetX) {
            mLastL = minOffsetX;
        } else if (mLastL > maxOffsetX) {
            mLastL = maxOffsetX;
        }
        if (mLastT < minOffsetY) {
            mLastT = minOffsetY;
        } else if (mLastT > maxOffsetY) {
            mLastT = maxOffsetY;
        }
        mLastR = mLastL + v.getWidth();
        mLastB = mLastT + v.getHeight();
        v.layout(mLastL, mLastT, mLastR, mLastB);
    }

    private static float delta() {
        return SystemClock.currentThreadTimeMillis() - mDownTime;
    }

}
