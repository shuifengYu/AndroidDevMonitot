package com.coderyu.androiddevmonitor.logmonitor;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coderyu.androiddevmonitor.Monitor;
import com.coderyu.androiddevmonitor.R;
import com.coderyu.androiddevmonitor.adapter.CommonAdapter;
import com.coderyu.androiddevmonitor.adapter.ViewHolder;
import com.coderyu.androiddevmonitor.utils.DeviceUtil;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by coder_yu on 18/6/24.
 */

public class LogMonitor extends Monitor {

    private static final String MONITOR_NAME = "LOG";
    private static final int D = Color.parseColor("#4fb937");
    private static final int I = Color.parseColor("#3257a3");
    private static final int W = Color.parseColor("#743a2d");
    private static final int E = Color.parseColor("#fc1a26");
    private static final int ALL = 0;
    private static final int MESSAGE_NOTIFY_ADAPTER = 1;

    private static LogMonitor sInstance;
    private int mCurrColor = ALL;
    private LogQueue mLogQueue;
    private LogQueue mShowQueue;
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
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtil.getScreenHeight(context) / 2));
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
                    showColor(ALL);
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
        mShowQueue = new LogQueue();
        mAdapter = new CommonAdapter<LogQueue.LogQueueItem>(mContext, mShowQueue, R.layout.item_logmonitor) {
            @Override
            public void convert(ViewHolder helper, LogQueue.LogQueueItem item, int position) {
                TextView tvContent = helper.getView(R.id.item_logmonitor_tv_content);
                tvContent.setText(item.getMessage());
                tvContent.setTextColor(item.getColor());
            }
        };
        return mAdapter;
    }

    private void showColor(int color) {
        if (mCurrColor == color) {
            return;
        }
        mCurrColor = color;
        mShowQueue.clear();
        mShowQueue.addAll(getLosByColor(color));
    }

    private Collection<? extends LogQueue.LogQueueItem> getLosByColor(int color) {
        if (color == ALL) {
            return mLogQueue;
        }
        Iterator<LogQueue.LogQueueItem> iterator = mLogQueue.iterator();
        LogQueue colorQueue = new LogQueue();
        while (iterator.hasNext()) {
            LogQueue.LogQueueItem it = iterator.next();
            if (it.getColor() != color) {
                continue;
            }
            colorQueue.add(it);
        }
        return colorQueue;
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

        LogQueue.LogQueueItem logQueueItem = new LogQueue.LogQueueItem(color, sInstance.getLogMessage(TAG, message));
        sInstance.mLogQueue.add(logQueueItem);
        if (isShowingColor(logQueueItem.getColor())) {
            sInstance.mShowQueue.add(logQueueItem);
            // TODO: 18/6/26 加个缓存时间，防止太快刷新
            sInstance.notifyDateSetChanged();
        }
    }

    private static boolean isShowingColor(int color) {
        return sInstance.mCurrColor == ALL || sInstance.mCurrColor == color;
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
