package com.coderyu.androiddevmonitor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by coder_yu on 18/6/26.
 */

public abstract class Monitor implements IMonitor {
    protected final Context mContext;
    private String mName;
    private View mView;



    public Monitor(Context context, String name) {
        this.mContext = context;
        this.mName = name;

        this.mView = view(context);
    }


    public String getName() {
        return mName;
    }

    public View getView() {
        return this.mView;
    }

    @Override
    public void show(ViewGroup rootView) {
        if (rootView == null) {
            return;
        }
        rootView.addView(this.mView);
    }

    protected abstract View view(Context context);
}
