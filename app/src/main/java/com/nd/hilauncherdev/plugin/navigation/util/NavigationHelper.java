package com.nd.hilauncherdev.plugin.navigation.util;

import android.content.Context;

import com.nd.hilauncherdev.plugin.navigation.widget.openpage.PageCountSetter;

/**
 * 此类中的方法由桌面在指定的时机通过反射调用
 * 不可混淆
 * Created by linliangbin on 2016/9/13.
 */
public class NavigationHelper {

    /**
     * 处理桌面对零屏的每日统计回调
     */
    public static void handleNavigationUsingStateStastics(Context context){

    }
    /**
     * 处理新增用户对零屏的处理(所有渠道)
     */
    public static void handleNavigationForNewAllChannel(Context context){

    }

    /**
     * 处理新增用户对零屏的处理（仅安智渠道）
     */

    public static void handleNavigationForNewAnzhi(Context context){

    }
    /**
     * 处理升级时对零屏显示逻辑的处理（所有渠道）
     */
    public static void handleNavigationForUpgradeAllChannel(Context context) {

    }

    /**
     * 处理升级时对零屏显示逻辑的处理（仅安智渠道）
     */
    public static void handleNavigationForUpgradAnzhi(Context context){

    }

    /**
     * 处理零屏每次初始化之前的回调，目前主要处理搜狐新闻异常情况
     */
    public static void handleNavigationWhenInflateView(Context context){

    }

    /**
     * 获取当前开放屏屏幕数
     * @return
     */
    public static int getNavigationOpenPageCount(Context context){
        return PageCountSetter.getInstance().getPageCount();
    }

    /**
     * 获取当前是否显示快速搜索屏
     * @param context
     * @return
     */
    public static boolean isShowSearchPage(Context context){
        return false;
    }
}
