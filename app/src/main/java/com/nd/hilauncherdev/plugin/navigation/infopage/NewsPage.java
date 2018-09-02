package com.nd.hilauncherdev.plugin.navigation.infopage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.nd.hilauncherdev.framework.view.recyclerview.MultiItemTypeAdapter;
import com.nd.hilauncherdev.plugin.navigation.activity.MainActivity;
import com.nd.hilauncherdev.plugin.navigation.infopage.adapter.InvenoNewAdapter;
import com.nd.hilauncherdev.plugin.navigation.infopage.model.NewsInfo;
import com.nd.hilauncherdev.plugin.navigation.util.LauncherCaller;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import java.util.List;

/**
 * Created by Administrator on 2018/8/29.
 */

public class NewsPage extends BaseRecyclerList {
    public NewsPage(@NonNull Context context) {
        super(context);
    }

    public NewsPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsPage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        final InvenoNewAdapter adapter = new InvenoNewAdapter(getContext(),data);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                LauncherCaller.openUrl(getContext().getApplicationContext(),adapter.getDatas().get(position).origin_url);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter;
    }


    @Override
    public void netRequest() {
        MyOkHttp.getInstance().postH().tag(this).url("http://api.zlauncher.cn/action.ashx/commonaction/1").jsonParams("")
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        List<NewsInfo> list = MainActivity.parseInfo(getContext().getApplicationContext());
                        if(list!=null){
                            onNetDataSuccess(list,true);
                        } else {
                            onNetDataFail("");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        onNetDataFail("");
                    }
                });
    }



}
