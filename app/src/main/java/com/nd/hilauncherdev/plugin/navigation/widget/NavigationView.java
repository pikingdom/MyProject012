package com.nd.hilauncherdev.plugin.navigation.widget;

import android.content.Context;
import android.view.LayoutInflater;

import com.nd.hilauncherdev.plugin.navigation.R;

public class NavigationView extends BasePageView{


	public NavigationView(Context context, String pkgName, int channelType, String countryCode, String CUID, String channelId, ClassLoader loader, String appId, String baseDir) {
	    this(context);
	}

	public NavigationView(Context context) {
		super(context);
		initView();
	}
	protected void initView() {
		LayoutInflater.from(getContext()).inflate(R.layout.launcher_navigation, this);
	}
	

}
