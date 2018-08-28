package com.nd.hilauncherdev.plugin.navigation.widget;

import android.app.Activity;

/**
 * @desc 此类作为桌面对0屏工程的反射调用接口集合
 * @author keqinghong_dian91
 * @date 2016/2/3
 */
public interface NavigationForLauncherInvoker {
	
	/**定位服务 */
	void onLauncherLocation();
	/**切换至桌面*/
	void onSnapToWorkspace();
	/**切换至零屏*/
	void onSnapToNavigation();
	/**每日更新*/
	void onLauncherStart(String pkgName, int channelType, String countryCode);
//    void updateAndRefreshSiteDetail();
	/**桌面onresume()回调*/
    void onShow();
    /**桌面onStart()回调*/
    void onLauncherOnStart();
    /**零屏通过返回键进入桌面*/
    void onBackKeyDown();
    /**设置引用Launcher*/
    void setActivity(Activity activity);
    /**刷新字体**/
    void refreshTypeface();
    /**桌面通过返回键进入零屏*/
    void handleBackKeyToNavigation();

    void scrollToPage(int which);
    void jumpToPage(int which);
}
