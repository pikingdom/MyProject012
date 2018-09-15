package com.nd.hilauncherdev.plugin.navigation.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nd.hilauncherdev.framework.common.util.DensityUtil;
import com.nd.hilauncherdev.framework.common.util.GlideUtil;
import com.nd.hilauncherdev.framework.common.view.baseDetail.BaseDetailInterface;
import com.nd.hilauncherdev.framework.common.view.recyclerview.CommonAdapter;
import com.nd.hilauncherdev.framework.common.view.recyclerview.MultiItemTypeAdapter;
import com.nd.hilauncherdev.framework.common.view.recyclerview.base.ViewHolder;
import com.nd.hilauncherdev.kitset.util.reflect.NavigationKeepForReflect;
import com.nd.hilauncherdev.plugin.navigation.R;
import com.nd.hilauncherdev.plugin.navigation.constant.SPConstant;
import com.nd.hilauncherdev.plugin.navigation.helper.ZLauncherUrl;
import com.nd.hilauncherdev.plugin.navigation.util.LauncherBranchController;
import com.nd.hilauncherdev.plugin.navigation.util.LauncherCaller;
import com.nd.hilauncherdev.plugin.navigation.util.SPUtil;
import com.nd.hilauncherdev.plugin.navigation.util.SystemUtil;
import com.nd.hilauncherdev.plugin.navigation.util.ToastUtil;
import com.nd.hilauncherdev.plugin.navigation.widget.model.WebSiteItem;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import com.tsy.sdk.myokhttp.util.MyOKhttpHeler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018\9\8 0008.
 */

public class NavigationSitesView extends FrameLayout implements BaseDetailInterface {

    private RecyclerView recyclerView;
    private List<WebSiteItem> data;
    private boolean hasLoad = false;
    private RecyclerView hostRecyclerView;

    public NavigationSitesView(Context context) {
        this(context,null);
    }

    public NavigationSitesView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NavigationSitesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.navigation_favorite_sites_view,this);
        findViews();
        initData();
        loadLocalData();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int wmode = MeasureSpec.getMode(widthMeasureSpec);
//        int wsize = MeasureSpec.getSize(widthMeasureSpec);
//
//        int hmode = MeasureSpec.getMode(heightMeasureSpec);
//        int hsize = MeasureSpec.getSize(heightMeasureSpec);
//        View view = getChildAt(0);
//        view.measure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST));
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.e("sitesview",wmode+","+wsize+","+hmode+","+hsize+","+view.getMeasuredHeight());
    }

    private void initData() {
        data = new ArrayList<WebSiteItem>();
        CommonAdapter adapter = new CommonAdapter<WebSiteItem>(getContext(), R.layout.navigation_favorite_sites_item,data) {
            @Override
            protected void convert(ViewHolder holder, WebSiteItem webSiteItem, int position) {
                ImageView icon_img = holder.getView(R.id.icon_img);
                if(webSiteItem.iconType == WebSiteItem.TYPE_SERVER_ICON){
                    GlideUtil.load(getContext(),webSiteItem.iconURL,icon_img);
                } else if(webSiteItem.iconType == WebSiteItem.TYPE_LOCAL_FILE_ICON){
                    GlideUtil.load(getContext(),webSiteItem.iconPath,icon_img);
                }
                ImageView icon_img_mask = holder.getView(R.id.icon_img_mask);
                icon_img_mask.setImageResource(R.drawable.navigation_favorite_icon_mask);
//                GlideUtil.load(getContext(),R.drawable.navigation_favorite_icon_mask,icon_img_mask);
                TextView icon_tv = holder.getView(R.id.icon_tv);
                icon_tv.setText(webSiteItem.name);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                WebSiteItem item = data.get(position);
                if (null == item)
                    return;
                if(item.actionType == WebSiteItem.ACTION_TYPE_OPEN_APP){
                    try {
                        if(!SystemUtil.isApkInstalled(getContext(),item.appPkg)){
                            ToastUtil.show(R.string.activity_not_found);
                        }else{
                            try {
                                if(!NavigationKeepForReflect.processXiaoMi7OpenApp(Intent.parseUri(item.url,0))){
                                    SystemUtil.startActivitySafely(getContext(),Intent.parseUri(item.url,0));
                                }
                            }catch (Throwable t){
                                t.printStackTrace();
                                SystemUtil.startActivitySafely(getContext(),Intent.parseUri(item.url,0));
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    CvAnalysis.submitClickEvent(mContext,CvAnalysisConstant.NAVIGATION_SCREEN_INTO,
//                            positionId,resId,CvAnalysisConstant.RESTYPE_LINKS);
                    if(LauncherBranchController.isNavigationForCustomLauncher()){
                        NavigationKeepForReflect.eventNavigationApp_V8508(item.appPkg,position);
                    }
                }else{
                    Log.e("clarkzheng","4444444444444444");
                    if(!TextUtils.isEmpty(item.url)){
                        LauncherCaller.openUrl(getContext().getApplicationContext(),item.url);
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final Rect rect = new Rect();
        final int grid_margin = DensityUtil.dip2px(getContext(),1);
        final int grid_margin1 = DensityUtil.dip2px(getContext(),1);
        rect.left = grid_margin1;
        rect.right = grid_margin1;
        rect.top = grid_margin;
        rect.bottom = grid_margin;
        NavigationSitesGridLayoutManger gridLayoutManager = new NavigationSitesGridLayoutManger(getContext(),5,rect.top+rect.bottom);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = rect.left;
                outRect.right = rect.right;
                outRect.top = rect.top;
                outRect.bottom = rect.bottom;
            }
        });
    }

    private void loadLocalData(){
        final List<WebSiteItem> list = NavigationLoader.getRecommendedSites(getContext(),10);
        if(list != null && list.size() >0){
            onNetDataSuccess(list);
        }
    }

    @Override
    public void loadData(){
        final List<WebSiteItem> list = NavigationLoader.getRecommendedSites(getContext(),10);
        if(list == null || list.size() == 0){
            netRequest();
        } else {
            onNetDataSuccess(list);
        }
    }

    @Override
    public boolean hasLoad() {
        return hasLoad;
    }

    public void onLauncherStart() {
        netRequest();
        Log.e("sitesview","w:"+getWidth()+"h:"+getHeight());
    }

    public void netRequest() {
        //从服务端获取数据 配置2
        final SPUtil spUtil = new SPUtil();
        int ver = spUtil.getInt(SPConstant.NAVIGATION_SITES_VER,0);
        MyOkHttp.getInstance().postH().url(ZLauncherUrl.COMMONACTION_2)
                .addParam("paramname","NavigationRecommendedSites")
                .addParam("ver",ver+"")
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        try {
                            if(!MyOKhttpHeler.isEmpty(response)){
                                JSONObject jsonObject = new JSONObject(response);
                                String content = jsonObject.getString("content");
                                spUtil.putString(SPConstant.NAVIGATION_SITES_JSON,content);
                                spUtil.putInt(SPConstant.NAVIGATION_SITES_VER,jsonObject.getInt("currentversion")+1);
                                final List<WebSiteItem> list = NavigationLoader.getRecommendedSites(getContext(),10);
                                if(list != null && list.size() >0){
                                    onNetDataSuccess(list);
                                } else {
                                    onNetDataFail("");
                                }
                            } else {
                                onNetDataFail("");
                            }
                        }catch (Exception e){
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
    public void onNetDataFail(String msg) {
        recyclerView.getAdapter().notifyDataSetChanged();
        if(hostRecyclerView != null){
            hostRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onNetDataSuccess() {
        recyclerView.getAdapter().notifyDataSetChanged();
        if(hostRecyclerView != null){
            hostRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    public void onNetDataSuccess(List<WebSiteItem> list){
        //重新刷新
        hasLoad = true;
        data.clear();
        data.addAll(list);
        onNetDataSuccess();
    }

    public void setHostRecyclerView(RecyclerView hostRecyclerView) {
//        this.hostRecyclerView = hostRecyclerView;
    }
}
