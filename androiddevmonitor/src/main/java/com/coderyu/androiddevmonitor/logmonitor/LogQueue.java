package com.coderyu.androiddevmonitor.logmonitor;

import java.util.LinkedList;

/**
 * Created by coder_yu on 18/6/24.
 */

public class LogQueue extends LinkedList<LogQueue.LogQueueItem> {
    public static final int MAX_COUNT = 500;
    public static final int MIN_COUNT = 20;
    private int mMaxCount = MAX_COUNT;

    public void setMaxCount(int count) {
        if (!isValidCount(count)) {
            return;
        }
        mMaxCount = count;
    }


    private boolean isValidCount(int count) {
        return count >= MIN_COUNT && count <= MAX_COUNT;
    }


    @Override
    public void add(int index, LogQueueItem element) {
        sizeEnsure();
        super.add(index, element);
    }

    public synchronized boolean add(LogQueueItem item) {
        sizeEnsure();
        return super.add(item);
    }


    @Override
    public void addLast(LogQueueItem logQueueItem) {
        sizeEnsure();
        super.addLast(logQueueItem);
    }

    private void sizeEnsure() {
        if (size() >= mMaxCount) {
            removeFirst();
        }
    }

    public static class LogQueueItem {
        private int mColor;
        private String mMessage;

        LogQueueItem(int color, String message) {
            this.mColor = color;
            this.mMessage = message;
        }

        public int getColor() {
            return mColor;
        }

        public String getMessage() {
            return mMessage;
        }
    }

}
