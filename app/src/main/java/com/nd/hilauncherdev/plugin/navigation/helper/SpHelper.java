package com.nd.hilauncherdev.plugin.navigation.helper;

import com.nd.hilauncherdev.plugin.navigation.constant.SpNameConstant;
import com.nd.hilauncherdev.plugin.navigation.util.SPUtil;

/**
 * Created by Administrator on 2018\9\5 0005.
 */

public class SpHelper {
    public static String getRecommendIconForFanyue(){
        SPUtil spUtil = new SPUtil(SpNameConstant.LAUNCHER_CONFIG_SP_NAME);
        return spUtil.getString("fanyue_navigation_icons_info","");
    }
}
