package com.nd.hilauncherdev.plugin.navigation.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nd.hilauncherdev.plugin.navigation.R;
import com.nd.hilauncherdev.plugin.navigation.constant.SPConstant;
import com.nd.hilauncherdev.plugin.navigation.helper.ZLauncherUrl;
import com.nd.hilauncherdev.plugin.navigation.util.SPUtil;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import com.tsy.sdk.myokhttp.util.MyOKhttpHeler;

import org.json.JSONObject;


/**
 * Created by Administrator on 2018/8/29.
 */

public class NavigationSearchView extends BasePageView {

    private RelativeLayout search_rl;
    private ImageView search_img;
    private TextView search_tv;
    private RecyclerView recyclerView;
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
    }

    private void initData() {

    }

    public void loadData(){

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
                                spUtil.putInt(SPConstant.NAVIGATION_SITES_VER,jsonObject.getInt("currentversion"));
                                onNetDataSuccess();
                            } else {

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

    private void onNetDataSuccess(){

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
