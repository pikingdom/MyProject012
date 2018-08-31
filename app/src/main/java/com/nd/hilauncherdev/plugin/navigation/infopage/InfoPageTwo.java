package com.nd.hilauncherdev.plugin.navigation.infopage;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.AttributeSet;
import android.widget.TextView;

import com.felink.sdk.common.ThreadUtil;
import com.nd.hilauncherdev.framework.view.recyclerview.CommonAdapter;
import com.nd.hilauncherdev.framework.view.recyclerview.base.ViewHolder;
import com.nd.hilauncherdev.plugin.navigation.R;
import com.nd.hilauncherdev.plugin.navigation.infopage.model.NewsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/29.
 */

public class InfoPageTwo extends BaseFrameLayout{
    protected List<NewsBean> data;
    private Handler handler = new Handler();
    public InfoPageTwo(@NonNull Context context) {
        super(context);
    }

    public InfoPageTwo(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InfoPageTwo(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected CommonAdapter getCommonAdapter() {
        if(data == null){
            data = new ArrayList<NewsBean>();
        }
        CommonAdapter adapter = new CommonAdapter<NewsBean>(getContext(),R.layout.item_grid,data) {
            @Override
            protected void convert(ViewHolder holder, NewsBean newsBean, int position) {
                TextView tv = holder.getView(R.id.title);
                tv.setText(newsBean.title);
            }
        };
        return adapter;
    }

    @Override
    protected void init() {
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
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final List<NewsBean> list = new ArrayList<NewsBean>();
                for(int i=0;i<5;i++){
                    NewsBean bean = new NewsBean();
                    bean.title = "1111111111"+i;
                    list.add(bean);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(list);
                    }
                });
            }
        });
    }

    private void onSuccess(List<NewsBean> list){
        hasNext = false;
        mLoadMoreWrapper.setShowLoadMore(hasNext);
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
