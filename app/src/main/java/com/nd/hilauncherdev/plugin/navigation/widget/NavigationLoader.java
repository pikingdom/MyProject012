package com.nd.hilauncherdev.plugin.navigation.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.nd.hilauncherdev.plugin.navigation.constant.SPConstant;
import com.nd.hilauncherdev.plugin.navigation.helper.NavSpHelper;
import com.nd.hilauncherdev.framework.common.util.FileUtil;
import com.nd.hilauncherdev.plugin.navigation.util.LauncherBranchController;
import com.nd.hilauncherdev.plugin.navigation.util.SPUtil;
import com.nd.hilauncherdev.plugin.navigation.util.SystemUtil;
import com.nd.hilauncherdev.framework.common.util.reflect.ReflectInvoke;
import com.nd.hilauncherdev.plugin.navigation.widget.model.WebSiteItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018\9\5 0005.
 */

public class NavigationLoader {

    /** 系统目录下的推荐图标缓存目录 */
    public static final String SYSTEM_APP_ICON_PATH = "/system/etc/custominfo/";
    private final static String RECOMMEND_PATH = Environment.getDataDirectory() + "/data/" + NavigationView.getPkgName() + "/files/recommend.txt";

    /**
     * @desc 获取定制版的推荐图标
     * @author linliangbin
     * @time 2017/2/27 10:06
     */
    public static final int INDEX_ITEM_ACTION_TYPE = 0;
    public static final int INDEX_ITEM_URL_OR_PKG = 1;
    public static final int INDEX_ITEM_NAME = 2;
    public static final int INDEX_ITEM_ICON = 3;


    // ------------导航数据JSON的TAG------------//
    private final static String TAG_VERSION = "version";
    private final static String TAG_REC_VERSION = "recommendVesion";
    private final static String TAG_ICON_VERSION = "iconversion";
    private final static String TAG_CODE = "Code";
    private final static String TAG_NAVIGATION = "navigation";
    private final static String TAG_NAVIGATION_CATEGORY_TITLE = "title";
    private final static String TAG_NAVIGATION_CATEGORY_ITEMS = "items";
    private final static String TAG_NAVIGATION_CATEGORY_ICON_URL = "icon";
    private final static String TAG_NAVIGATION_ITEM_NAME = "name";
    private final static String TAG_NAVIGATION_ITEM_URL = "url";
    private final static String TAG_NAVIGATION_ITEM_RED = "red";
    private final static String TAG_NAVIGATION_ITEM_COLOR = "color";
    private final static String TAG_NAVIGATION_ITEM_BOLD = "bold";
    private final static String TAG_NAVIGATION_ITEM_ICON_V6 = "iconFortV6";
    private final static String TAG_NAVIGATION_ITEM_ID = "id";
    private final static String TAG_NAVIGATIN_ITEM_SOURCE_ID = "AdSource";
    private final static String TAG_NAVIGATIN_ITEM_CALLBACK = "CallBack";
    private final static String TAG_NAVIGATIN_ITEM_OPEN_TYPE = "OpenType";
    private final static String TAG_NAVIGATIN_ITEM_NEED_SESSION = "NeedSession";
    private final static String TAG_NAVIGATION_RECOMMAND = "icons";

    /**
     * 获取推荐网址数据
     * 从服务器上返回的json数据(暂时只支持从本地获取推荐网址)
     * @return
     */
    public static List<WebSiteItem> getRecommendedSites(Context ctx, int totalCount) {
        JSONArray favoriteArray = getAllDataFromLocalFile();// 从本地读取已经从服务器上获取到的数据
        List<WebSiteItem> list = null;
        if (favoriteArray != null && favoriteArray.length() > 0) {
            list = getRecommendedSitesFromJson(favoriteArray);
        }
//        if (list == null || list.size() == 0) {
//            list = getRecommendedSitesFromCode(ctx,totalCount/2,needLocal);
//            /** 已经添加本地图标，避免重复添加 */
//            needLocal =false;
//        }
        if(list == null){
            list = new ArrayList<WebSiteItem>();
        }

        //获取定制版本数据
        List<WebSiteItem> customList = getRecommendSitesFroCustomLauncher(ctx);
        if(customList == null){
            customList = new ArrayList<WebSiteItem>();
        }

        if(customList.size() > totalCount){
            return customList.subList(0,totalCount);
        }

        int CUSTOME_SIZE = totalCount / 2;
        if(customList.size() + list.size() >= totalCount){
            CUSTOME_SIZE = totalCount;
        }
        for(int i =0; i<list.size() && customList.size() < CUSTOME_SIZE; i++){
            customList.add(list.get(i));
        }
        return customList;
    }

    /**
     * 读取本地保存的所有数据
     *
     * @return
     */
    private static JSONArray getAllDataFromLocalFile() {
        // 服务器返回说明已经是最新数据，则读取放FILES文件夹里面的数据
        SPUtil spUtil = new SPUtil();
        String localData = spUtil.getString(SPConstant.NAVIGATION_SITES_JSON);
        if (TextUtils.isEmpty(localData)) {
            return null;
        }
        try {
            return new JSONArray(localData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从json中获取顶部导航数据
     * @return
     */
    private static List<WebSiteItem> getRecommendedSitesFromJson(JSONArray array) {
        try {
            List<WebSiteItem> list = new ArrayList<WebSiteItem>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                WebSiteItem item = new WebSiteItem();
                item.url = object.getString(TAG_NAVIGATION_ITEM_URL);
                item.name = object.getString(TAG_NAVIGATION_ITEM_NAME);
                item.iconURL = object.getString(TAG_NAVIGATION_ITEM_ICON_V6);
                item.CallBack = object.optInt(TAG_NAVIGATIN_ITEM_CALLBACK);
                item.AdSourceId = object.optInt(TAG_NAVIGATIN_ITEM_SOURCE_ID);
                item.openType = object.optInt(TAG_NAVIGATIN_ITEM_OPEN_TYPE);
                item.needSession = object.optBoolean(TAG_NAVIGATIN_ITEM_NEED_SESSION);
                if (object.has(TAG_NAVIGATION_ITEM_ID)) {
                    item.siteId = object.getString(TAG_NAVIGATION_ITEM_ID);
                }
                list.add(item);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<WebSiteItem> getRecommendSitesFroCustomLauncher(Context context){
        List<WebSiteItem> list = new ArrayList<WebSiteItem>();
        String iconStrings = NavSpHelper.getRecommendIconForFanyue();
        Log.i("llbeing","iconStrings:"+iconStrings);
        if(TextUtils.isEmpty(iconStrings)){
            return list;
        }
        String itemStrings[] = iconStrings.split("##");
        if(itemStrings.length <= 0){
            return list;
        }
        for(String itemString : itemStrings){
            try {
                String itemParams[] = itemString.split("\\|");
                /**每个ITEM  四个属性 */
                if(itemParams.length != 4){
                    continue;
                }
                WebSiteItem webItem = new WebSiteItem();
                if("1".equals(itemParams[INDEX_ITEM_ACTION_TYPE])){
                    webItem.actionType = WebSiteItem.ACTION_TYPE_OPEN_APP;
                }
                webItem.iconType = WebSiteItem.TYPE_LOCAL_FILE_ICON;
                try {
                    if(LauncherBranchController.isShenlong(context)){
                        webItem.iconPath = Environment.getDataDirectory() + "/data/" + context.getPackageName() + "/files/custominfo/" + itemParams[INDEX_ITEM_ICON];
                    }else{
                        webItem.iconPath = Environment.getExternalStorageDirectory() + "/" + ReflectInvoke.getBaseDirName(NavigationView.getLauncher()) + "/custominfo/" + itemParams[INDEX_ITEM_ICON];
                    }
                    if(!FileUtil.isFileExits(webItem.iconPath)){
                        webItem.iconPath = SYSTEM_APP_ICON_PATH + itemParams[INDEX_ITEM_ICON];
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }

                /** 打开动作 */
                if(webItem.actionType == WebSiteItem.ACTION_TYPE_OPEN_APP){

                    String intentString = "";
                    if(!TextUtils.isEmpty(itemParams[INDEX_ITEM_URL_OR_PKG])){
                        String pkgClass[] = itemParams[INDEX_ITEM_URL_OR_PKG].split("/");
                        if(pkgClass.length == 2){
                            webItem.appPkg = pkgClass[0];
                            webItem.appClass = pkgClass[1];
                            Intent intent1 = new Intent();
                            intent1.setClassName(webItem.appPkg,webItem.appClass);
                            intentString = intent1.toUri(0);
                            if(!SystemUtil.isApkInstalled(context,webItem.appPkg)){
                                continue;
                            }
                        }else{
                            webItem.appPkg = itemParams[INDEX_ITEM_URL_OR_PKG];
                            Intent intent = SystemUtil.getAppLaunchIntent(context,itemParams[INDEX_ITEM_URL_OR_PKG]);
                            if(intent == null){
                                continue;
                            }
                            intentString = intent.toUri(0);
                        }
                    }else{
                        webItem.appPkg = itemParams[INDEX_ITEM_URL_OR_PKG];
                        Intent intent = SystemUtil.getAppLaunchIntent(context,itemParams[INDEX_ITEM_URL_OR_PKG]);
                        if(intent == null){
                            continue;
                        }
                        intentString = intent.toUri(0);
                    }
                    webItem.url = intentString;
                    if(FileUtil.isFileExits(webItem.iconPath) && !TextUtils.isEmpty(itemParams[INDEX_ITEM_ICON])){
                        webItem.iconType = WebSiteItem.TYPE_LOCAL_FILE_ICON;
                    }else{
                        webItem.iconType = WebSiteItem.TYPE_USE_APP_ICON;
                    }
                }else{
                    webItem.url = itemParams[INDEX_ITEM_URL_OR_PKG];
                    if(FileUtil.isFileExits(webItem.iconPath) && !TextUtils.isEmpty(itemParams[INDEX_ITEM_ICON])){
                        webItem.iconType = WebSiteItem.TYPE_LOCAL_FILE_ICON;
                    }else{
                        continue;
                    }
                }
                webItem.name = itemParams[INDEX_ITEM_NAME];

                list.add(webItem);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return list;
    }
}
