package com.nd.hilauncherdev.plugin.navigation.infopage;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.nd.hilauncherdev.framework.view.recyclerview.MultiItemTypeAdapter;
import com.nd.hilauncherdev.framework.view.recyclerview.RecycleViewDivider;
import com.nd.hilauncherdev.framework.view.recyclerview.wrapper.LoadMoreWrapper;
import com.nd.hilauncherdev.plugin.navigation.R;

/**
 * Created by Administrator on 2018/8/29.
 */

public abstract class BaseFrameLayout extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener {
    protected RecyclerView mRecyclerView;
    protected SwipeRefreshLayout mRefreshLayout;
    protected LoadMoreWrapper mLoadMoreWrapper;

    protected int pageIndex = 1;
    protected int pageSize = 10;
    protected boolean hasNext = false;

    protected boolean hasLoad = false;
    public BaseFrameLayout(@NonNull Context context) {
        this(context,null);
    }

    public BaseFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.navigation_list_view,this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL,2,getContext().getResources().getColor(R.color.divide_color)));

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        mRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mRefreshLayout.setOnRefreshListener(this);

        mLoadMoreWrapper = new LoadMoreWrapper(getCommonAdapter());
        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        mLoadMoreWrapper.setShowLoadMore(false);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if(mRefreshLayout.isRefreshing()){
                    return;
                }
                if(hasNext){
                    pageIndex++;
                    loadData();
                }
            }
        });
        mRecyclerView.setAdapter(mLoadMoreWrapper);
        init();
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
//        loadData();
    }

    protected abstract MultiItemTypeAdapter getCommonAdapter();

    protected abstract void init();

    public abstract void loadData();

    public abstract boolean hasLoad();

}
