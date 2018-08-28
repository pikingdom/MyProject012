package com.nd.hilauncherdev.plugin.navigation.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class BasePageView extends RelativeLayout implements
		NavigationForLauncherInvoker {

	public BasePageView(Context context) {
		super(context);
	}

	public BasePageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onLauncherLocation() {

	}

	@Override
	public void onSnapToWorkspace() {

	}

	@Override
	public void onSnapToNavigation() {

	}

	@Override
	public void onLauncherStart(String pkgName, int channelType,
                                String countryCode) {

	}

	@Override
	public void onShow() {

	}

	@Override
	public void onLauncherOnStart() {

	}

	@Override
	public void onBackKeyDown() {

	}

	@Override
	public void setActivity(Activity activity) {

	}

	@Override
	public void refreshTypeface() {

	}

	@Override
	public void handleBackKeyToNavigation() {

	}

	@Override
	public void scrollToPage(int which) {

	}

	@Override
	public void jumpToPage(int which) {

	}
}
