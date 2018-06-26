package com.coderyu.androiddevmonitor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected final int mItemLayoutId;
    protected Context mContext;
    protected List<T> mDatas;

    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId, LoadMoreListener loadMoreListener) {
        this.mContext = context;
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView, parent);

        convert(viewHolder, getItem(position), position);
        if (position == mDatas.size() - 1 && loadMoreListener != null) {
            loadMoreListener.loadMoreListener();
        }
        return viewHolder.getConvertView();
    }

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
    }

    public abstract void convert(ViewHolder helper, T item, int position);

    public LoadMoreListener loadMoreListener;

    public interface LoadMoreListener {
        void loadMoreListener();
    }
}
