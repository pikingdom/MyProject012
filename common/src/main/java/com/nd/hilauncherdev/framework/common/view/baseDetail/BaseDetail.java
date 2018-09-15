package com.nd.hilauncherdev.framework.common.view.baseDetail;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.nd.hilauncherdev.framework.common.view.ProgressImageView;
import com.nd.hilauncherdev.framework.common.view.baselist.BasePageInterface;
import com.nd.hilauncherdev.framework.common.view.baselist.BaseView;
import com.nd.hilauncherdev.framework.common.view.baselist.ViewLife;
import com.nd.hilauncherdev.plugin.common.R;
import com.tsy.sdk.myokhttp.MyOkHttp;

/**
 * Created by Administrator on 2018/8/29.
 */

public abstract class BaseDetail<T> extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener,BaseDetailInterface,BaseView,ViewLife,BasePageInterface {


    protected ProgressImageView viewLoading;
    protected View viewError;

    protected boolean hasLoad = false;

    protected static final int STATE_MAIN = 0x00;
    protected static final int STATE_LOADING = 0x01;
    protected static final int STATE_ERROR = 0x02;
    protected int currentState = STATE_MAIN;
    protected SwipeRefreshLayout mRefreshLayout;
    protected FrameLayout container;
    public BaseDetail(@NonNull Context context) {
        this(context,null);
    }

    public BaseDetail(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseDetail(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.common_detail_view,this);
        container = (FrameLayout) findViewById(R.id.container);
        View.inflate(getContext(), R.layout.common_view_progress, this);
        viewLoading = (ProgressImageView) findViewById(R.id.view_loading);
        viewLoading.setVisibility(View.GONE);

        View.inflate(getContext(), R.layout.common_loading_error,this);
        viewError = findViewById(R.id.view_error);
        viewError.setVisibility(View.GONE);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        mRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mRefreshLayout.setOnRefreshListener(this);
        container.addView(getDetailView());
    }


    @Override
    public void onRefresh() {
        onRefreshLoadData();
    }

    private void onRefreshLoadData() {
        hasLoad = false;
        loadData();
    }
    @Override
    public void stateMain() {
        if (currentState == STATE_MAIN)
            return;
        hideCurrentView();
        currentState = STATE_MAIN;
        container.setVisibility(View.VISIBLE);
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
        viewError.setVisibility(View.VISIBLE);
    }

    private void hideCurrentView() {
        switch (currentState) {
            case STATE_MAIN:
//                container.setVisibility(View.GONE);
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
    public void onNetDataSuccess(){
        if(mRefreshLayout.isRefreshing()){
            mRefreshLayout.setRefreshing(false);
        }
        stateMain();
        hasLoad = true;
    }

    @Override
    public boolean hasLoad() {
        return hasLoad;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        MyOkHttp.getInstance().cancel(this);
    }

    //子类列表实现=======================
    protected abstract void netRequest();

    protected abstract View getDetailView();

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
}
