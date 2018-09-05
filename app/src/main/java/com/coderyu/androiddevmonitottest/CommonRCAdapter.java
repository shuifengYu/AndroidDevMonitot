package com.coderyu.androiddevmonitottest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yushuifeng on 2018/4/8.
 */


public abstract class CommonRCAdapter<T> extends RecyclerView.Adapter<CommonRCAdapter.ViewHolder> {


    protected Context mContext;
    private int mLayoutId;
    private List<T> mDatas;

    public CommonRCAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.get(mContext, mLayoutId, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, mDatas.get(position), position);
    }

    public abstract void convert(ViewHolder holder, T t, int position);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> mViews;
        private View mConvertView;
        private Context mContext;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mConvertView = itemView;
            mViews = new SparseArray<>();
        }

        public static ViewHolder get(Context context, int layoutId, ViewGroup parent) {
            View inflate = LayoutInflater.from(context).inflate(layoutId, parent, false);
            return new ViewHolder(inflate);
        }

        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public ViewHolder setText(int viewId, String text) {
            ((TextView) getView(viewId)).setText(text);
            return this;
        }

        public ViewHolder setImageResource(int viewId, int resId) {
            ImageView imageView = getView(viewId);
            imageView.setImageResource(resId);
            return this;
        }

        public ViewHolder setViewClickListener(int viewId, View.OnClickListener listener) {
            getView(viewId).setOnClickListener(listener);
            return this;
        }

        public ViewHolder setItemClickListener(View.OnClickListener listener) {
            mConvertView.setOnClickListener(listener);
            return this;

        }

        public ViewHolder setItemLongClickListener(View.OnLongClickListener listener) {
            mConvertView.setOnLongClickListener(listener);
            return this;

        }
    }

    public void add(T data) {
        add(mDatas.size(), data);
    }

    public void add(int index, T data) {
        mDatas.add(index, data);
        notifyItemInserted(index);
    }

    public void remove(T data) {
        int i = mDatas.indexOf(data);
        if (i >= 0) {
            notifyItemRemoved(i);
            mDatas.remove(i);
        }
    }
}
