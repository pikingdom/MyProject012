package com.nd.hilauncherdev.plugin.navigation.widget;

import android.app.Activity;
import android.content.Context;

import java.util.List;

/**
 * 桌面-零屏间的调用接口
 * <p>
 * Created by linliangbin on 2016/9/13.
 */
public interface NavigationLauncherInterface {
    /**
     * 初始化开放屏(zero)
     * */
    public void init(String channel);

    /**
     * 初始化搜狐fragment
     * 桌面将零屏添加到activity后回调
     */
    //桌面反射调用过来
    public void initSohuFragment();

    public void setThemeChoose(int position);
    /**
     * 直接设置开放屏(zero)滚动到第几屏，无动画
     * **/
    public void jumpToPage(int which);

    /**
     * 直接设置开放屏(zero)滚动到第几屏，有动画
     * **/
    public void scrollToPage(int which);

    public void setCUID(String CUID);

    public void setHotWordView(List<Object> list);

    public void upgradePlugin(String url, int ver, boolean isWifiAutoDownload);

    /**
     * @desc 桌面滑动或是返回键进入显示零屏回调
     * @author linliangbin
     * @time 2017/4/11 9:48
     */
    public void onShowingNavigationView();


    /**
     * @desc 离开零屏回到桌面回调
     * @author linliangbin
     * @time 2017/4/11 9:47
     */
    public void onLeavingNavigation();

    public void setActivity(Activity activity);

    /**
     * 桌面onPause时回调，主要处理零屏屏暂停时的处理
     */
    public void handleNavigationWhenLauncherOnPause();

    /**
     * 桌面通过返回键进入零屏，目前主要处理返回键时进入零屏的动作
     */
    public void handleBackKeyToNavigation();

    /**
     * 桌面onResume时回调，主要处理零屏屏设置发生改变时的处理
     */
    public void handleNavigationWhenLauncherOnResume();

    public void onBackKeyDown();

    public void hideVideoView();
    public void onLauncherStart();

    public void onShow();
    public void refreshPaintAndView();
    public void unregisterReceiver();
    public void updateAndRefreshSiteDetail();
}
