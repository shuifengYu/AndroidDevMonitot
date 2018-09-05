package com.coderyu.androiddevmonitottest;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class RefreshLayoutActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private CommonRCAdapter<YourCustomerBean> mAdapter;
    private ArrayList<YourCustomerBean> mDatas;
    private int mPageNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refreshlayout);
        mDatas = new ArrayList<>();
        init();
    }

    private void init() {
        initSwipeRefreshLayout();
        initRecyclerView();
    }

    private void initRecyclerView() {
        this.mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = getAdapter();
        this.mLinearLayoutManager = new LinearLayoutManager(this);
        this.mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.mRecyclerView.setLayoutManager(mLinearLayoutManager);
        this.mRecyclerView.setAdapter(mAdapter);
        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mLinearLayoutManager.findLastVisibleItemPosition() == mDatas.size() - 1) {
                        loadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void loadMore() {
        Toast.makeText(this, "加载更多", Toast.LENGTH_SHORT).show();
        getServerData(false);
    }

    private void initSwipeRefreshLayout() {
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        this.mSwipeRefreshLayout.setOnRefreshListener(this);

        // TODO: 18/9/6 替换成自定义颜色
        this.mSwipeRefreshLayout.setColorSchemeResources(R.color.color1, R.color.color2, R.color.color3, R.color.color4);
    }

    private CommonRCAdapter<YourCustomerBean> getAdapter() {
        return new CommonRCAdapter<YourCustomerBean>(this, R.layout.your_customer_item, mDatas) {
            @Override
            public void convert(ViewHolder holder, YourCustomerBean o, int position) {
                holder.setText(R.id.your_customer_item_tv, o.key);
            }
        };
    }

    @Override
    public void onRefresh() {
        mPageNum = 1;
        mDatas.clear();
        getServerData(true);
    }

    private static int index = 0;

    private void getServerData(final boolean isRefresh) {

        // TODO: 18/9/6 替换成自己的接口请求
        Server.getData(mPageNum, new Callback() {
            public void onStart() {
                if (isRefresh) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            }

            public void onSuccess(ResponseBody body) {
                mDatas.addAll(mDatasbody.data);
                mAdapter.notifyDataSetChanged();
                mPageNum++;
            }

            public void onFailed() {
            }

            public void onFinish() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
