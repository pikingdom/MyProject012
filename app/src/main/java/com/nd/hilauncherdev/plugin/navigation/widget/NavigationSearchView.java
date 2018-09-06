package com.nd.hilauncherdev.plugin.navigation.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nd.hilauncherdev.framework.view.recyclerview.CommonAdapter;
import com.nd.hilauncherdev.framework.view.recyclerview.base.ViewHolder;
import com.nd.hilauncherdev.plugin.navigation.R;
import com.nd.hilauncherdev.plugin.navigation.base.BasePageInterface;
import com.nd.hilauncherdev.plugin.navigation.constant.SPConstant;
import com.nd.hilauncherdev.plugin.navigation.helper.ZLauncherUrl;
import com.nd.hilauncherdev.plugin.navigation.util.DensityUtil;
import com.nd.hilauncherdev.plugin.navigation.util.GlideUtil;
import com.nd.hilauncherdev.plugin.navigation.util.SPUtil;
import com.nd.hilauncherdev.plugin.navigation.widget.model.WebSiteItem;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import com.tsy.sdk.myokhttp.util.MyOKhttpHeler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2018/8/29.
 */

public class NavigationSearchView extends BasePageView implements BasePageInterface {

    private RelativeLayout search_rl;
    private ImageView search_img;
    private TextView search_tv;
    private RecyclerView recyclerView;
    private List<WebSiteItem> data;
    private boolean hasLoad = false;
    public NavigationSearchView(Context context) {
        this(context,null);
    }

    public NavigationSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.navigation_search_page_view,this);
        findViews();
        initData();
    }

    private void findViews() {
        search_rl = (RelativeLayout) findViewById(R.id.search_rl);
        search_img = (ImageView) findViewById(R.id.search_img);
        search_tv = (TextView) findViewById(R.id.search_tv);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),5);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int grid_margin = DensityUtil.dip2px(getContext(),3);
                outRect.left = grid_margin;
                outRect.right = grid_margin;
                outRect.top = grid_margin;
                outRect.bottom = grid_margin;            }
        });
    }

    private void initData() {
        data = new ArrayList<WebSiteItem>();
        CommonAdapter adapter = new CommonAdapter<WebSiteItem>(getContext(),R.layout.navigation_favorite_sites_item,data) {
             @Override
             protected void convert(ViewHolder holder, WebSiteItem webSiteItem, int position) {
                 ImageView icon_img = holder.getView(R.id.icon_img);
                 if(webSiteItem.iconType == WebSiteItem.TYPE_SERVER_ICON){
                     GlideUtil.load(getContext(),webSiteItem.iconURL,icon_img);
                 } else if(webSiteItem.iconType == WebSiteItem.TYPE_LOCAL_FILE_ICON){
                     GlideUtil.load(getContext(),webSiteItem.iconPath,icon_img);
                 }
                 TextView icon_tv = holder.getView(R.id.icon_tv);
                 icon_tv.setText(webSiteItem.name);
             }
        };
        recyclerView.setAdapter(adapter);
    }

    public void loadData(){
        final List<WebSiteItem> list = NavigationLoader.getRecommendedSites(getContext(),10);
        MyOkHttp.mHandler.post(new Runnable() {
            @Override
            public void run() {
                hasLoad = true;
                if(list != null && list.size() >=0){
                    data.addAll(list);
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onLauncherStart() {
        super.onLauncherStart();
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
                                spUtil.putInt(SPConstant.NAVIGATION_SITES_VER,jsonObject.getInt("currentversion"+1));
                                onNetDataSuccess();
                            }
                        }catch (Exception e){
                            onNetDataFail();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        onNetDataFail();
                    }
                });
    }

    public void onNetDataFail(String msg) {

    }
    public void onNetDataSuccess(){
        //重新刷新
        hasLoad = false;
        loadData();
    }

    @Override
    public void onPageSelected() {
        if(!hasLoad){
            loadData();
        }
    }

    private void onNetDataFail(){

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
