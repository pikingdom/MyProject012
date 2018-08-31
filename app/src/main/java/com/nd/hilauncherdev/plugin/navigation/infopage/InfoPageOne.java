package com.nd.hilauncherdev.plugin.navigation.infopage;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.felink.sdk.common.ThreadUtil;
import com.nd.hilauncherdev.framework.view.recyclerview.MultiItemTypeAdapter;
import com.nd.hilauncherdev.plugin.navigation.activity.MainActivity;
import com.nd.hilauncherdev.plugin.navigation.infopage.adapter.InvenoNewAdapter;
import com.nd.hilauncherdev.plugin.navigation.infopage.model.NewsInfo;
import com.nd.hilauncherdev.plugin.navigation.util.LauncherCaller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/29.
 */

public class InfoPageOne extends BaseFrameLayout{
    protected List<NewsInfo> data;
    private Handler handler = new Handler();
    public InfoPageOne(@NonNull Context context) {
        super(context);
    }

    public InfoPageOne(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InfoPageOne(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected MultiItemTypeAdapter getCommonAdapter() {
        if(data == null){
            data = new ArrayList<NewsInfo>();
        }
        InvenoNewAdapter adapter = new InvenoNewAdapter(getContext(),data);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                LauncherCaller.openUrl(getContext().getApplicationContext(),data.get(position).origin_url);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter;
    }

    @Override
    protected void init() {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void loadData() {
        if(pageIndex == 1){
            mRefreshLayout.setRefreshing(true);
        }
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final List<NewsInfo> list = MainActivity.parseInfo(getContext().getApplicationContext());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(list);
                    }
                });
            }
        });
    }

    private void onSuccess(List<NewsInfo> list){
        hasNext = true;
        if(pageIndex == 1){
            data.clear();
            hasLoad = true;
        }
        data.addAll(list);
        if(pageIndex == 1){
            mRefreshLayout.setRefreshing(false);
        }
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }


    @Override
    public boolean hasLoad() {
        return hasLoad;
    }
}
