package com.nd.hilauncherdev.plugin.navigation.infopage.help;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.nd.hilauncherdev.plugin.navigation.infopage.model.NewsInfo;
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

    public static String getListJsonParams(int pageIndex,int pageCount){
        StringBuilder builder = new StringBuilder(getUidJsonParams());
//        String ua = "Mozilla/5.0 (Linux; Android 7.0; STF-AL10 Build/HUAWEISTF-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043508 Safari/537.36 V1_AND_SQ_7.2.0_730_YYB_D QQ/7.2.0.3270 NetType/4G WebP/0.3.0 Pixel/1080";
        String ua = "android";
        builder .append("&uid=01011808212025247801001905839609")
                .append("&scenario=0x010100")
        .append("&content_type=0x00000003")
        .append("&display=0x0000000f")
//        .append("&display=0x00000001")
        .append("&link_type=0x00000003")
        .append("&operation="+pageIndex)
        .append("&count="+pageCount)
        .append("&ua="+ua);
        return builder.toString();

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
