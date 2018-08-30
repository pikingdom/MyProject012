package com.nd.hilauncherdev.plugin.navigation.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.List;

public class BasePageView extends RelativeLayout implements NavigationLauncherInterface {

	public BasePageView(Context context) {
		super(context);
	}

	public BasePageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	@Override
	public void init(String channel) {

	}

	@Override
	public void initSohuFragment() {

	}

	@Override
	public void setThemeChoose(int position) {

	}

	@Override
	public void jumpToPage(int which) {

	}

	@Override
	public void scrollToPage(int which) {

	}

	@Override
	public void setCUID(String CUID) {

	}

	@Override
	public void setHotWordView(List<Object> list) {

	}

	@Override
	public void upgradePlugin(String url, int ver, boolean isWifiAutoDownload) {

	}

	@Override
	public void onShowingNavigationView() {

	}

	@Override
	public void onLeavingNavigation() {

	}

	@Override
	public void setActivity(Activity activity) {

	}

	@Override
	public void handleNavigationWhenLauncherOnPause() {

	}

	@Override
	public void handleBackKeyToNavigation() {

	}

	@Override
	public void handleNavigationWhenLauncherOnResume() {

	}

	@Override
	public void onBackKeyDown() {

	}

	@Override
	public void hideVideoView() {

	}

	@Override
	public void onLauncherStart() {

	}

	@Override
	public void onShow() {

	}

	@Override
	public void refreshPaintAndView() {

	}

	@Override
	public void unregisterReceiver() {

	}

	@Override
	public void updateAndRefreshSiteDetail() {

	}
}
