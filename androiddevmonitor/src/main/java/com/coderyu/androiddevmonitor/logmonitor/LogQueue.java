package com.coderyu.androiddevmonitor.logmonitor;

import java.util.LinkedList;

/**
 * Created by coder_yu on 18/6/24.
 */

public class LogQueue {
    public static final int MAX_COUNT = 500;
    public static final int MIN_COUNT = 20;
    private int mMaxCount = MAX_COUNT;
    private LinkedList<LogQueueItem> mQueue;

    public void setMaxCount(int count) {
        if (!isValidCount(count)) {
            return;
        }
        mMaxCount = count;
    }

    private boolean isValidCount(int count) {
        return count >= MIN_COUNT && count <= MAX_COUNT;
    }

    public synchronized boolean add(LogQueueItem item) {
        if (mQueue.size() >= mMaxCount) {
            mQueue.removeFirst();
        }
        return mQueue.add(item);
    }

    public static class LogQueueItem {
        private int mColor;
        private String mMessage;

        LogQueueItem(int color, String message) {
            this.mColor = color;
            this.mMessage = message;
        }
    }

}
