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

    public static void init(Context context) {
        mContext = context;
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        windowManager.addView(rootView(windowManager,mContext), params(point));
    }

    private static ViewGroup.LayoutParams params(Point point) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = point.x;
        params.height = point.y-500;

        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.TOP;

        return params;
    }

    private static View rootView(final WindowManager windowManager, Context context) {
        final View rootView = LayoutInflater.from(context).inflate(R.layout.window_monitor, null);

        final View btnOpen = rootView.findViewById(R.id.monitor_btn_open);
        final View btnClose = rootView.findViewById(R.id.monitor_btn_close);
         btnClose.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View v) {
                windowManager.removeView(rootView);
             }
         });
        View tvClose = rootView.findViewById(R.id.monitor_tv_close);
        final View openView = rootView.findViewById(R.id.monitor_openview);

        btnOpen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    mDownTime = SystemClock.currentThreadTimeMillis();
                    lastX = event.getX();
                    lastY = event.getY();
                } else if (action == MotionEvent.ACTION_UP) {
                    if (delta() <= CLICK_DURATION) {
                        btnOpen.setVisibility(View.GONE);
                        openView.setVisibility(View.VISIBLE);
                    }
                } else if (action == MotionEvent.ACTION_MOVE) {
                    int deltaX = (int) (event.getX() - lastX);
                    int deltaY = (int) (event.getY() - lastY);
                    lastX = event.getX();
                    lastY = event.getY();
//                    v.animate().translationXBy(deltaX);
//                    v.animate().translationYBy(deltaY);
//                    v.setTranslationX(v.getTranslationX()+deltaX);
//                    v.setTranslationY(v.getTranslationY()+deltaY);
                    v.layout(v.getLeft()+deltaX,v.getTop()+deltaY,v.getRight()+deltaX,v.getBottom()+deltaY);
//                    v.setLeft((int) (v.getLeft() + deltaX));
//                    v.setTop((int) (v.getTop() + deltaY));
                }
                return true;
            }
        });

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openView.setVisibility(View.GONE);
                btnOpen.setVisibility(View.VISIBLE);
            }
        });
        return rootView;
    }


    private static float delta() {
        return SystemClock.currentThreadTimeMillis() - mDownTime;
    }

}
