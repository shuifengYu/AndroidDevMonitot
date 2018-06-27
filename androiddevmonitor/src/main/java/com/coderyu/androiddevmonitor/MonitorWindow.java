package com.coderyu.androiddevmonitor;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.coderyu.androiddevmonitor.utils.DeviceUtil;
import com.coderyu.androiddevmonitor.utils.ViewUtil;

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
    private long lastTimeDown;

    WindowManager mWindowManager;
    ViewGroup mRootView;
    private ViewGroup mMonitorContainer;
    private WindowManager.LayoutParams mParams;

    private MonitorWindow(Context context) {
        mContext = context.getApplicationContext();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mRootView = rootView(mContext);
        Point point = new Point();
        mWindowManager.getDefaultDisplay().getSize(point);
        mWindowManager.addView(mRootView, floatParams());
    }

    public static void init(Context context) {
        if (sInstance != null) {
        }
        if (context == null) {
            throw new RuntimeException("Context can not be null!!!");
        }
        sInstance = new MonitorWindow(context);
    }


    private ViewGroup.LayoutParams floatParams() {
        mParams = new WindowManager.LayoutParams();
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        return mParams;
    }

    private ViewGroup rootView(Context context) {
        final ViewGroup rootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.window_monitor, null);

        final View btnOpen = rootView.findViewById(R.id.monitor_btn_open);
        final View llTopBar = rootView.findViewById(R.id.monitor_line_topbar);
        View tvClose = rootView.findViewById(R.id.monitor_tv_close);

        final View openView = rootView.findViewById(R.id.monitor_openview);
        mMonitorContainer = (ViewGroup) openView.findViewById(R.id.monitor_main);
        View.OnTouchListener touchListener = createTouchListener();
        btnOpen.setOnTouchListener(touchListener);
        llTopBar.setOnTouchListener(touchListener);

        tvClose.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           closeMonitor();
                                       }
                                   }
        );
        return rootView;
    }

    private View.OnTouchListener createTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int rawX = (int) event.getRawX();
                int rawY = (int) event.getRawY();

                float deltaX, deltaY;
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        firstX = rawX;
                        firstY = rawY;
                        lastTimeDown = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        deltaX = Math.abs(lastRawX - firstX);
                        deltaY = Math.abs(lastRawY - firstY);
                        float duringTime = System.currentTimeMillis() - lastTimeDown;
                        if (deltaX < 5 && deltaY < 5 && duringTime < CLICK_DURATION) {
                            openMonitor();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int [] location = new int[2];
                        mRootView.getLocationOnScreen(location);
                        float x = event.getX();
                        float y = event.getY();
                        Log.e("aaaaa  a",mParams.x+","+mParams.y+","+x+","+y);
                        mParams.x =  DeviceUtil.getScreenWidth(mContext) -rawX- mRootView.getWidth() /2;
                        mParams.y = DeviceUtil.getScreenHeight(mContext) - rawY -mRootView.getHeight()+ 40;
                        Log.e("aaaaa  b",mParams.x+","+mParams.y);
                        mWindowManager.updateViewLayout(mRootView, mParams);
                        lastRawX = rawX;
                        lastRawY = rawY;
                        break;
                }

                return true;
            }
        };
    }

    private void openMonitor() {
        ViewUtil.gone(mRootView, R.id.monitor_btn_open);
        ViewUtil.visible(mRootView, R.id.monitor_openview);
        showMonitor(MonitorManager.getInstance().currMonitor());
    }


    public void showMonitor(Monitor monitor) {
        monitor.show(mMonitorContainer);
    }

    public static MonitorWindow getInstance() {
        return sInstance;
    }

    public void closeMonitor() {
        ViewUtil.visible(mRootView, R.id.monitor_btn_open);
        ViewUtil.gone(mRootView, R.id.monitor_openview);
        mMonitorContainer.removeAllViews();
    }
}
