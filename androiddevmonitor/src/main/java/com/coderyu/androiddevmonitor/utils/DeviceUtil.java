package com.coderyu.androiddevmonitor.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;


/**
 * Created by Jason.yu on 2015/10/22.
 */

/**
 * 需要在application中初始化
 */
public class DeviceUtil {

    private static Point sScreenSize;

    /**
     * @param context
     * @return the screen height in pixels
     */
    public static int getScreenHeight(Context context) {
        return getScreenSize(context).y;
    }

    /**
     * @param context
     * @return the screen width in pixels
     */
    public static int getScreenWidth(Context context) {
        return getScreenSize(context).x;
    }

    public static Point getScreenSize(Context context) {
        if (sScreenSize != null) {
            return sScreenSize;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        sScreenSize = new Point();
        display.getSize(sScreenSize);
        return sScreenSize;
    }

}
