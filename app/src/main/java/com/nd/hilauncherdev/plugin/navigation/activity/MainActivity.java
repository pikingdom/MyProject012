package com.nd.hilauncherdev.plugin.navigation.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.nd.hilauncherdev.plugin.navigation.infopage.help.InvenoHelper;
import com.nd.hilauncherdev.plugin.navigation.infopage.model.NewsInfo;
import com.nd.hilauncherdev.plugin.navigation.util.FileUtil;
import com.nd.hilauncherdev.plugin.navigation.widget.NavigationView;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018\8\28 0028.
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyOkHttp.getInstance().setApplicationConext(getApplicationContext());
        NavigationView navigationView = new NavigationView(this);
        setContentView(navigationView);
        navigationView.setActivity(this);
        navigationView.onLauncherStart();
//        setContentView(R.layout.navigation_activity_main);
    }

    public void Test001(View view){
        final String url = "http://opensdk.inveno.com/gate/api/list?product_id=xoslauncher&promotion=openplatform&uid=010115052811093812010000189\n" +
                "03105&request_time=1527835981&tk=414f81259e24ed1a55d2c2e551f05ffa&api_ver=3.0.\n" +
                "0&aid=c549d7fafad988c3&imei=866693020470360&network=1&app_ver=1.0.8&brand=H\n" +
                "UAWEI&model=HUAWEI%252BGRA-TL00&platform=android&app_lan=zh_CN&scenario=0\n" +
                "x010100&content_type=0x00000001&display=0x00000001&link_type=0x00000001&opera\n" +
                "tion=1&count=10&osv=4";

//        String url = "http://opensdk.inveno.com/gate/api/list";
//        String jsonParams = "latitude=26.113803&app_lan=zh_CN&language=zh_CN&mcc=460&platform=android&network=1&mode=1&uid=01011808212025247801001905839609&lacn=22790&request_time=1535676586&osv=8.1.0&content_type=0x00000003&scenario=0x010100&product_id=xoslauncher&model=Redmi+Note+5&brand=xiaomi&cell_id=134272260&longitude=119.236470&mnc=01&display=0x0000000f&count=10&link_type=0x00000003&api_ver=3.0.0&tk=14fc7dfbf8dd1ce0881b94e3ea1e1eeb&app_ver=1.0.15&imei=868773032475652&sdk_ver=3.0.4&aid=bfd8b2b15b1f3932&operation=1&promotion=openplatform&";
        MyOkHttp.getInstance().postInveno().url(InvenoHelper.GET_LIST_URL).jsonParams(InvenoHelper.getListJsonParams(InvenoHelper.SCENARIO_GLOBAL,1,10)).enqueue(new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
//                parseInfo();
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
//        List<NewsInfo> list = parseInfo(getApplicationContext());
//        if(list != null){
//            Log.e("zhenghonglin","size:"+list.size());
//        }
    }

    public static List<NewsInfo> parseInfo(Context context) {
        String infoTxt = FileUtil.readAssetsContent(context,"info.txt");
        //解析
        try {
            List<NewsInfo> list = new ArrayList<NewsInfo>();
            JSONObject jsonObject = new JSONObject(infoTxt);
            int code = jsonObject.getInt("code");
            if(code == 200){
                Log.e("zhenghonglin","code:"+code);
                JSONArray dataArray = jsonObject.getJSONArray("data");
                if(dataArray != null && dataArray.length()>0){
                    for(int i=0;i<dataArray.length();i++){
                        JSONObject itemJsonObject = dataArray.getJSONObject(i);
                        NewsInfo newsInfo = new NewsInfo();
                        newsInfo.origin_url = itemJsonObject.getString("origin_url");
                        newsInfo.summary = itemJsonObject.getString("summary");
                        newsInfo.source = itemJsonObject.getString("source");
                        newsInfo.share_url = itemJsonObject.getString("share_url");
                        newsInfo.display=itemJsonObject.getString("display");
                        newsInfo.publish_time=itemJsonObject.getString("publish_time");
                        JSONArray listImageArray = itemJsonObject.optJSONArray("list_images");
                        if(listImageArray != null && listImageArray.length()>0){
                            for(int j=0;j<listImageArray.length();j++){
                                newsInfo.addImage2List(listImageArray.getJSONObject(j).getString("img_url"));
                            }
                        }
                        list.add(newsInfo);
                    }
                }
                return list;
            } else {
                return list;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void Test002(View view){
        String product_id = "7103d5e4f464";
        String request_time = System.currentTimeMillis()/1000+"";
        String url =  "http://uid.inveno.com/gate/getuid";
        String jsonParam = "product_id=xoslauncher&promotion=7103d5e4f464&request_time=1527835981&tk=4" +
                "14f81259e24ed1a55d2c2e551f05ffa&api_ver=3.0.0&aid=c549d7fafad988c3&imei=866693" +
                "020470360&network=1&app_ver=1.0.8&brand=HUAWEI&model=HUAWEI%252BGRA-TL0" +
                "0&platform=android&app_lan=zh_CN";
        String url1 = String.format(url,product_id,request_time);
        MyOkHttp.getInstance().postInveno().url(InvenoHelper.GET_UID_URL).jsonParams(InvenoHelper.getUidJsonParams()).enqueue(new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                Log.e("zhenghonglin","1response:"+response+","+System.currentTimeMillis());
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Log.e("zhenghonglin","2response:"+error_msg);
            }
        });
    }

    public void Test003(View view){
        MyOkHttp.getInstance().postH().tag(this).url("http://api.zlauncher.cn/action.ashx/commonaction/1").jsonParams("")
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("zhenghonglin","activity onDestroy");
    }

}
