package com.nd.hilauncherdev.plugin.navigation.infopage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.nd.hilauncherdev.framework.view.recyclerview.MultiItemTypeAdapter;
import com.nd.hilauncherdev.framework.view.baselist.BaseRecyclerList;
import com.nd.hilauncherdev.plugin.navigation.constant.SPConstant;
import com.nd.hilauncherdev.plugin.navigation.infopage.adapter.InvenoNewAdapter;
import com.nd.hilauncherdev.plugin.navigation.infopage.help.InvenoHelper;
import com.nd.hilauncherdev.plugin.navigation.infopage.model.NewsInfo;
import com.nd.hilauncherdev.plugin.navigation.util.LauncherCaller;
import com.nd.hilauncherdev.plugin.navigation.util.SPUtil;
import com.nd.hilauncherdev.plugin.navigation.util.ThreadUtil;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/29.
 */

public class NewsPage extends BaseRecyclerList {

    private String scenarioType = "";

    public NewsPage(@NonNull Context context) {
        super(context);
    }

    public NewsPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsPage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NewsPage(@NonNull Context context,String scenarioType) {
        super(context);
        this.scenarioType = scenarioType;
    }

    public NewsPage(@NonNull Context context,Builder builder) {
        super(context);
        this.scenarioType = builder.scenarioType;
        if(builder.list != null && builder.list.size()>0){
            for(View view:builder.list){
                mHeaderAndFooterWrapper.addHeaderView(view);
            }
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        final InvenoNewAdapter adapter = new InvenoNewAdapter(getContext(),data);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                NewsInfo newsInfo = adapter.getDatas().get(position);
                if(newsInfo != null){
                    submitClick(newsInfo);
                    LauncherCaller.openUrl(getContext().getApplicationContext(),newsInfo.origin_url);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter;
    }

    @Override
    protected void onScrollStateIdle() {
        //上报展示量
        int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
        for(int i=firstPosition;i<lastPosition;i++){
            NewsInfo newsInfo = (NewsInfo) getItem(i);
            if(newsInfo != null){
                submitPv(newsInfo);
            }
        }
    }

    private void submitPv(NewsInfo newsInfo){
        final List<String> list = newsInfo.pv_url;
        ThreadUtil.executeSubmit(new Runnable() {
            @Override
            public void run() {
                if(list != null && list.size()>0){
                    for(int i=0;i<list.size();i++){
                        MyOkHttp.getInstance().get().url(list.get(i)).tag(NewsPage.this).execute();
                    }
                }
            }
        });
    }

    private void submitClick(NewsInfo newsInfo){
        final List<String> list = newsInfo.click_url;
        ThreadUtil.executeSubmit(new Runnable() {
            @Override
            public void run() {
                if(list != null && list.size()>0){
                    for(int i=0;i<list.size();i++){
                        MyOkHttp.getInstance().get().url(list.get(i)).tag(NewsPage.this).execute();
                    }
                }
            }
        });
    }

    @Override
    public void netRequest() {
        SPUtil spUtil = new SPUtil();
        String uid = spUtil.getString(SPConstant.INVENO_UID_KEY);
        if(TextUtils.isEmpty(uid)){
            requestUidAndList();
        } else {
            requestList();
        }
    }

    private void requestUidAndList(){
        MyOkHttp.getInstance().postInveno().url(InvenoHelper.GET_UID_URL).jsonParams(InvenoHelper.getUidJsonParams()).enqueue(new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                Log.e("zhenghonglin","1response:"+response+","+System.currentTimeMillis());
                InvenoHelper.parseUid(response);
                SPUtil spUtil = new SPUtil();
                String netUid = spUtil.getString(SPConstant.INVENO_UID_KEY);
                if(TextUtils.isEmpty(netUid)){
                    onNetDataFail("");
                } else {
                    requestList();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Log.e("zhenghonglin","2response:"+error_msg);
                onNetDataFail("");
            }
        });
    }

    private void requestList(){
        MyOkHttp.getInstance().postInveno().tag(this).url(InvenoHelper.GET_LIST_URL).jsonParams(InvenoHelper.getListJsonParams(scenarioType,pageIndex,pageSize)).enqueue(new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                List<NewsInfo> list = InvenoHelper.parseInfo(response);
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

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("zhenghonglin","newspage onDetachedFromWindow");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyOkHttp.getInstance().cancel(this);
    }

    public static class Builder{
        private String scenarioType;
        private List<View> list;

        public Builder setScenarioType(String str){
            this.scenarioType = str;
            return this;
        }

        public Builder addHeadView(View view){
            if(list == null){
                list = new ArrayList<View>();
            }
            list.add(view);
            return this;
        }

        public NewsPage build(Context context){
            return new NewsPage(context,this);
        }
    }

}
