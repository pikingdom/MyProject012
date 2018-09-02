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
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.nd.hilauncherdev.framework.view.ProgressImageView;
import com.nd.hilauncherdev.framework.view.recyclerview.RecycleViewDivider;
import com.nd.hilauncherdev.framework.view.recyclerview.wrapper.LoadMoreWrapper;
import com.nd.hilauncherdev.plugin.navigation.R;
import com.nd.hilauncherdev.plugin.navigation.infopage.model.NewsInfo;
import com.tsy.sdk.myokhttp.MyOkHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/29.
 */

public abstract class BaseRecyclerList<T> extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener,BaseListInterface<T>,BaseView{

    protected List<T> data;//不能先new

    protected RecyclerView mRecyclerView;
    protected SwipeRefreshLayout mRefreshLayout;
    protected LoadMoreWrapper mLoadMoreWrapper;
    protected ProgressImageView ivLoading;
    protected View viewLoading;
    protected View viewError;

    protected int pageIndex = 1;
    protected int pageSize = 10;
    protected boolean hasNext = false;
    protected boolean hasLoad = false;

    protected static final int STATE_MAIN = 0x00;
    protected static final int STATE_LOADING = 0x01;
    protected static final int STATE_ERROR = 0x02;
    protected int currentState = STATE_MAIN;

    public BaseRecyclerList(@NonNull Context context) {
        this(context,null);
    }

    public BaseRecyclerList(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseRecyclerList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.navigation_list_view,this);
        View.inflate(getContext(), R.layout.view_progress, this);
        viewLoading = findViewById(R.id.view_loading);
        ivLoading = (ProgressImageView) viewLoading.findViewById(R.id.iv_progress);
        viewLoading.setVisibility(View.GONE);

        View.inflate(getContext(), R.layout.view_error,this);
        viewError = findViewById(R.id.view_error);
        viewError.setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL,2,getContext().getResources().getColor(R.color.divide_color)));

        mRecyclerView.setVisibility(View.VISIBLE);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        mRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mRefreshLayout.setOnRefreshListener(this);

        mLoadMoreWrapper = new LoadMoreWrapper(getListAdapter());
        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        mLoadMoreWrapper.setShowLoadMore(hasNext);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if(mRefreshLayout.isRefreshing()){
                    return;
                }
                if(hasNext){
                    pageIndex++;
                    onRefreshLoadData();
                }
            }
        });
        mRecyclerView.setAdapter(mLoadMoreWrapper);
    }


    @Override
    public void onRefresh() {
        pageIndex = 1;
        onRefreshLoadData();
    }

    private void onRefreshLoadData() {
        netRequest();
    }
    @Override
    public void stateMain() {
        if (currentState == STATE_MAIN)
            return;
        hideCurrentView();
        currentState = STATE_MAIN;
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean stateLoading() {
        if (currentState == STATE_LOADING)
            return false;
        hideCurrentView();
        currentState = STATE_LOADING;
        viewLoading.setVisibility(View.VISIBLE);
        ivLoading.start();
        return true;
    }

    @Override
    public void stateError() {
        if (currentState == STATE_ERROR)
            return;
        hideCurrentView();
        currentState = STATE_ERROR;
        viewError.setVisibility(View.VISIBLE);
    }

    private void hideCurrentView() {
        switch (currentState) {
            case STATE_MAIN:
                mRecyclerView.setVisibility(View.GONE);
                break;
            case STATE_LOADING:
                ivLoading.stop();
                viewLoading.setVisibility(View.GONE);
                break;
            case STATE_ERROR:
                if (viewError != null) {
                    viewError.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void loadData() {
        if(stateLoading()){
            netRequest();
        }
    }

    @Override
    public void onNetDataFail(String msg) {
        if(mRefreshLayout.isRefreshing()){
            mRefreshLayout.setRefreshing(false);
        }
        stateError();
    }

    @Override
    public void onNetDataSuccess(List<T> list,boolean next){
        if(mRefreshLayout.isRefreshing()){
            mRefreshLayout.setRefreshing(false);
        }
        stateMain();
        hasNext = next;
        mLoadMoreWrapper.setShowLoadMore(hasNext);
        if(pageIndex == 1){
            data.clear();
            hasLoad = true;
        }
        data.addAll(list);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean hasLoad() {
        return hasLoad;
    }

    private RecyclerView.Adapter getListAdapter(){
        if(data == null){
            data = new ArrayList<T>();
        }
        return getAdapter();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        MyOkHttp.getInstance().cancel(this);
        Glide.with(getContext()).pauseRequests();
    }

    //子类列表实现=======================
    protected abstract void netRequest();

    protected abstract RecyclerView.Adapter getAdapter();
    //子类列表实现=======================
}
