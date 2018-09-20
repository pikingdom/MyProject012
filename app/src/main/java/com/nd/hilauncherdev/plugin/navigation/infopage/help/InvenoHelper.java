package com.nd.hilauncherdev.plugin.navigation.infopage.help;

import android.os.Build;
import android.text.TextUtils;

import com.nd.hilauncherdev.plugin.navigation.constant.SPConstant;
import com.nd.hilauncherdev.plugin.navigation.infopage.model.NewsInfo;
import com.nd.hilauncherdev.plugin.navigation.util.SPUtil;
import com.tsy.sdk.myokhttp.Common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018\8\31 0031.
 */

public class InvenoHelper {

    public static final String ProductId = "7103d5e4f464";
    public static final String AppKey = "a40897d1f2ec680f80f0afd9d93c99bf";
    public static final String AppSecret = "8fe2040bdce808e22cf11d39b4fc85b50f49ecde";

    /**
     * 推广渠道名
     */
    public static final String promotion = "openplatform";


    public static final String GET_UID_URL = "https://uid.inveno.com/gate/getuid";

    public static final String GET_LIST_URL = "https://opensdk.inveno.com/gate/api/list";


    public static final String SCENARIO_RECOMMENT = "0x010100"; // 推荐
    public static final String SCENARIO_YDJK = "0x010102"; // 运动健康
    public static final String SCENARIO_CJ = "0x010103"; // 财经
    public static final String SCENARIO_YX = "0x010104"; // 游戏
    public static final String SCENARIO_KJ = "0x010105"; // 科技
    public static final String SCENARIO_YL = "0x010106"; // 娱乐


    public static String getUidJsonParams(){
        String request_time = (System.currentTimeMillis()/1000)+"";
        String tk = Common.md5Hex(AppSecret+":"+""+":"+request_time);
        StringBuilder builder = new StringBuilder();
        builder.append("product_id="+ProductId)
                .append("&promotion="+promotion)
                .append("&request_time="+request_time)
                .append("&tk="+tk)
                .append("&api_ver=3.0.0")
                .append("&aid="+Common.getCuid())
                .append("&imei="+Common.getImei())
                .append("&network=1")
                .append("&app_ver="+Common.getDivideVersion())
                .append("&brand="+ Build.BRAND)
                .append("&model="+Build.MODEL)
                .append("&platform=android")
                .append("&app_lan=zh_CN");
        return builder.toString();
    }

    public static String getListJsonParams(String scenarioType,int pageIndex,int pageCount){
        StringBuilder builder = new StringBuilder(getUidJsonParams());
        SPUtil spUtil = new SPUtil();
        String uid = spUtil.getString(SPConstant.INVENO_UID_KEY,"");
        String ua = "android";
        builder .append("&uid="+uid)
                .append("&scenario="+scenarioType)
        .append("&content_type=0x00000003")
        .append("&display=0x0000000f")
        .append("&link_type=0x00000003")
        .append("&operation="+pageIndex)
        .append("&count="+pageCount)
        .append("&ua="+ua);
        return builder.toString();
    }

    public static void parseUid(String infoTxt) {
        if(TextUtils.isEmpty(infoTxt)){
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(infoTxt);
            int code = jsonObject.getInt("code");
            if(code == 200){
                String uid = jsonObject.getString("uid");
                if(!TextUtils.isEmpty(uid)){
                    SPUtil spUtil = new SPUtil();
                    spUtil.putString(SPConstant.INVENO_UID_KEY,uid);
                }
            }
        }catch (Exception e){

        }
    }


    public static List<NewsInfo> parseInfo(String infoTxt) {
        if(TextUtils.isEmpty(infoTxt)){
            return null;
        }
        //解析
        try {
            List<NewsInfo> list = new ArrayList<NewsInfo>();
            JSONObject jsonObject = new JSONObject(infoTxt);
            int code = jsonObject.getInt("code");
            if(code == 200){
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
                        JSONArray pv_urlArray = itemJsonObject.optJSONArray("pv_url");
                        if(pv_urlArray != null && pv_urlArray.length() > 0){
                            for(int k=0;k<pv_urlArray.length();k++){
                                newsInfo.addPvUrl(pv_urlArray.getString(k));
                            }
                        }

                        JSONArray click_urlArray = itemJsonObject.optJSONArray("click_url");
                        if(click_urlArray != null && click_urlArray.length() > 0){
                            for(int l=0;l<click_urlArray.length();l++){
                                newsInfo.addClickUrl(click_urlArray.getString(l));
                            }
                        }

                        list.add(newsInfo);
                    }
                }
                return list;
            } else {
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
