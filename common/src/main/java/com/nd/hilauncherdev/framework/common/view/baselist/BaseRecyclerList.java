package com.nd.hilauncherdev.framework.common.view.baselist;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.nd.hilauncherdev.framework.common.view.baseDetail.BaseDetailInterface;
import com.nd.hilauncherdev.framework.common.view.recyclerview.RecycleViewDivider;
import com.nd.hilauncherdev.framework.common.view.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.nd.hilauncherdev.framework.common.view.recyclerview.wrapper.LoadMoreWrapper;
import com.nd.hilauncherdev.framework.common.view.ProgressImageView;
import com.nd.hilauncherdev.plugin.common.R;
import com.nd.hilauncherdev.framework.common.util.GlideUtil;
import com.tsy.sdk.myokhttp.MyOkHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/29.
 */

public abstract class BaseRecyclerList<T> extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener,BaseListInterface<T>,BaseView,ViewLife,BasePageInterface{

    protected List<T> data;//不能先new

    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager linearLayoutManager;
    protected SwipeRefreshLayout mRefreshLayout;
    protected LoadMoreWrapper mLoadMoreWrapper;
    protected HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    protected ProgressImageView viewLoading;
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
        LayoutInflater.from(getContext()).inflate(R.layout.common_list_view,this);
        View.inflate(getContext(), R.layout.common_view_progress, this);
        viewLoading = (ProgressImageView) findViewById(R.id.view_loading);
        viewLoading.setVisibility(View.GONE);

        View.inflate(getContext(), R.layout.common_loading_error,this);
        viewError = findViewById(R.id.view_error);
        viewError.setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL,2,getContext().getResources().getColor(R.color.common_list_divide_color)));

        mRecyclerView.setVisibility(View.VISIBLE);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        mRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mRefreshLayout.setOnRefreshListener(this);

        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(getListAdapter());
        addHeaderAndFooter(mHeaderAndFooterWrapper);
        mLoadMoreWrapper = new LoadMoreWrapper(mHeaderAndFooterWrapper);
        mLoadMoreWrapper.setLoadMoreView(R.layout.common_list_loading_more_view);
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
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    onScrollStateIdle();
                    GlideUtil.resumeRequests(getContext());
                } else {
                    GlideUtil.pauseRequests(getContext());
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        pageIndex = 1;
        onRefreshLoadData();
    }

    private void onRefreshLoadData() {
        netRequest();
        SparseArrayCompat<View> list = mHeaderAndFooterWrapper.getHeaderViews();
        if(list != null){
            for(int i = 0;i < list.size(); i++) {
                Object obj = list.valueAt(i);
                if(obj instanceof BaseDetailInterface){
                    BaseDetailInterface baseDetailInterface = (BaseDetailInterface) obj;
                    if(baseDetailInterface != null){
                        baseDetailInterface.loadData();
                    }
                }
            }
        }
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
        viewLoading.start();
        return true;
    }

    @Override
    public void stateError() {
        if (currentState == STATE_ERROR)
            return;
        hideCurrentView();
        currentState = STATE_ERROR;
        if(mRecyclerView.getChildCount()<=0){
            viewError.setVisibility(View.VISIBLE);
        } else {
            viewError.setVisibility(View.GONE);
        }
    }

    private void hideCurrentView() {
        switch (currentState) {
            case STATE_MAIN:
                if(mRecyclerView.getChildCount()<=0){
                    mRecyclerView.setVisibility(View.GONE);
                }
                break;
            case STATE_LOADING:
                viewLoading.stop();
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
            SparseArrayCompat<View> list = mHeaderAndFooterWrapper.getHeaderViews();
            if(list != null){
                for(int i = 0;i < list.size(); i++) {
                    Object obj = list.valueAt(i);
                    if(obj instanceof BaseDetailInterface){
                        BaseDetailInterface baseDetailInterface = (BaseDetailInterface) obj;
                        if(baseDetailInterface!=null && !baseDetailInterface.hasLoad()){
                            baseDetailInterface.loadData();
                        }
                    }
                }
            }
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

    protected T getItem(int position){
        if(data == null || position >= data.size()){
            return null;
        }
        return  data.get(position);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        MyOkHttp.getInstance().cancel(this);
    }

    //子类列表实现=======================
    protected abstract void netRequest();

    protected abstract RecyclerView.Adapter getAdapter();

    protected  void addHeaderAndFooter(HeaderAndFooterWrapper wrapper){

    }

    protected abstract void onScrollStateIdle();
    //子类列表实现=======================


    @Override
    public void onPageSelected() {
        if(!hasLoad){
            loadData();
        }
    }

    @Override
    public void onPageUnSelected() {

    }


    @Override
    public void onLauncherStart() {

    }


    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    public RecyclerView getRecyclerView(){
        return mRecyclerView;
    }
}
