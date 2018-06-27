package com.coderyu.androiddevmonitor.logmonitor;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coderyu.androiddevmonitor.Monitor;
import com.coderyu.androiddevmonitor.R;
import com.coderyu.androiddevmonitor.adapter.CommonAdapter;
import com.coderyu.androiddevmonitor.adapter.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by coder_yu on 18/6/24.
 */

public class LogMonitor extends Monitor {

    private static final String MONITOR_NAME = "LOG";
    private static final int D = Color.parseColor("#ff1122");
    private static final int I = Color.parseColor("#ff11FF");
    private static final int W = Color.parseColor("#111122");
    private static final int E = Color.parseColor("#ff4422");
    private static final int MESSAGE_NOTIFY_ADAPTER = 1;

    private static LogMonitor sInstance;
    private int mCurrColor = 0;
    private LogQueue mLogQueue;
    private BaseAdapter mAdapter;

    private Handler mHandler;
    private static SimpleDateFormat sdf;

    public static LogMonitor create(Context context) {
        if (sInstance == null) {
            sInstance = new LogMonitor(context);
        }
        return sInstance;
    }

    private LogMonitor(Context context) {
        super(context, MONITOR_NAME);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_NOTIFY_ADAPTER:
                        mAdapter.notifyDataSetChanged();
                        break;
                    default:
                }
            }
        };
    }

    @Override
    protected View view(final Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.window_logmonitor, null);
        View vD = rootView.findViewById(R.id.logmonitor_tv_d);
        View vI = rootView.findViewById(R.id.logmonitor_tv_i);
        View vW = rootView.findViewById(R.id.logmonitor_tv_w);
        View vE = rootView.findViewById(R.id.logmonitor_tv_e);
        View vAll = rootView.findViewById(R.id.logmonitor_tv_all);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "v:" + v.getId(), Toast.LENGTH_SHORT).show();
                int i = v.getId();
                if (i == R.id.logmonitor_tv_d) {
                    showColor(D);
                } else if (i == R.id.logmonitor_tv_i) {
                    showColor(I);
                } else if (i == R.id.logmonitor_tv_w) {
                    showColor(W);
                } else if (i == R.id.logmonitor_tv_e) {
                    showColor(E);
                } else if (i == R.id.logmonitor_tv_all) {
                    showColor(0);
                }
            }
        };
        vD.setOnClickListener(listener);
        vI.setOnClickListener(listener);
        vW.setOnClickListener(listener);
        vE.setOnClickListener(listener);
        vAll.setOnClickListener(listener);
        ListView listView = (ListView) rootView.findViewById(R.id.logmonitor_lv);

        listView.setAdapter(initAdapter());
        return rootView;
    }

    private ListAdapter initAdapter() {
        mLogQueue = new LogQueue();
        mAdapter = new CommonAdapter<LogQueue.LogQueueItem>(mContext, mLogQueue, R.layout.item_logmonitor) {
            @Override
            public void convert(ViewHolder helper, LogQueue.LogQueueItem item, int position) {
                TextView tvContent = helper.getView(R.id.item_logmonitor_tv_content);
                if (mCurrColor != 0 && item.getColor() != mCurrColor) {
                    tvContent.setVisibility(View.GONE);
                } else {
                    tvContent.setVisibility(View.VISIBLE);
                    tvContent.setText(item.getMessage());
                    tvContent.setTextColor(item.getColor());
                }
            }
        };
        return mAdapter;
    }

    private void showColor(int color) {
        mCurrColor = color;
    }

    public static void d(String TAG, String message) {
        add(D, TAG, message);
    }

    public static void i(String TAG, String message) {
        add(I, TAG, message);
    }

    public static void w(String TAG, String message) {
        add(W, TAG, message);
    }

    public static void e(String TAG, String message) {
        add(E, TAG, message);
    }

    public synchronized static void add(int color, String TAG, String message) {
        if (sInstance == null) {
            return;
        }

        sInstance.mLogQueue.add(new LogQueue.LogQueueItem(color, sInstance.getLogMessage(TAG, message)));
        // TODO: 18/6/26 加个缓存时间，防止太快刷新
        sInstance.notifyDateSetChanged();

    }

    private void notifyDateSetChanged() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mAdapter.notifyDataSetChanged();
        } else {
            mHandler.sendEmptyMessage(MESSAGE_NOTIFY_ADAPTER);
        }
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
