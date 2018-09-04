package com.nd.hilauncherdev.plugin.navigation.infopage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.nd.hilauncherdev.framework.view.recyclerview.CommonAdapter;
import com.nd.hilauncherdev.framework.view.recyclerview.base.ViewHolder;
import com.nd.hilauncherdev.plugin.navigation.R;
import com.nd.hilauncherdev.plugin.navigation.base.BaseRecyclerList;
import com.nd.hilauncherdev.plugin.navigation.infopage.model.NewsBean;
import com.tsy.sdk.myokhttp.MyOkHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/29.
 */

public class NewsPage2 extends BaseRecyclerList {
    public NewsPage2(@NonNull Context context) {
        super(context);
    }

    public NewsPage2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsPage2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<NewsBean>(getContext(), R.layout.item_grid,data) {
            @Override
            protected void convert(ViewHolder holder, NewsBean newsBean, int position) {
                TextView tv = holder.getView(R.id.title);
                tv.setText(newsBean.title);
            }
        };
        return adapter;
    }


    @Override
    public void netRequest() {
        final List<NewsBean> list = new ArrayList<NewsBean>();
        for(int i=0;i<5;i++){
            NewsBean bean = new NewsBean();
            bean.title = "1111111111"+i;
            list.add(bean);
        }
        MyOkHttp.mHandler.post(new Runnable() {
            @Override
            public void run() {
                onNetDataSuccess(list,true);
            }
        });
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("zhenghonglin","newspage2 onDetachedFromWindow");
    }
}
