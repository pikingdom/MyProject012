package com.nd.hilauncherdev.plugin.navigation.infopage.model;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/31.
 */

public class NewsInfo {

    public static final String TYPE_1 = "0x00000001";
    public static final String TYPE_2 = "0x00000002";
    public static final String TYPE_4 = "0x00000004";
    public static final String TYPE_8 = "0x00000008";

    public String origin_url;
    public String summary;
    public String source;
    public String display;
    public String share_url;
    public String publish_time;
    public List<String> list_images;
    public List<String> pv_url;
    public List<String> click_url;

    public void addPvUrl(String str){
        if(TextUtils.isEmpty(str)){
            return;
        }
        if(pv_url == null){
            pv_url = new ArrayList<String>();
        }
        pv_url.add(str);
    }

    public void addClickUrl(String str){
        if(TextUtils.isEmpty(str)){
            return;
        }
        if(click_url == null){
            click_url = new ArrayList<String>();
        }
        click_url.add(str);
    }

    public void addImage2List(String img_url){
        if(TextUtils.isEmpty(img_url)){
            return;
        }
        if(list_images == null){
            list_images = new ArrayList<String>();
        }
        list_images.add(img_url);
    }

    public String getHeadUrl(){
        return getUrl(0);
    }

    public String getUrl(int index){
        if(list_images == null || list_images.size() ==0 || index>=list_images.size() || index<0){
            return "";
        }
        return list_images.get(index);
    }
}
