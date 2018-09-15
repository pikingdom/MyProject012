package com.nd.hilauncherdev.plugin.navigation.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.nd.hilauncherdev.framework.common.view.baselist.ViewLife;

import java.util.List;

public class BasePageView extends RelativeLayout implements ViewLife{

	public BasePageView(Context context) {
		super(context);
	}

	public BasePageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	@Override
	public void onLauncherStart() {

	}



	@Override
	public void onResume() {

	}

	@Override
	public void onPause() {

	}

	@Override
	public void onDestroy() {

	}
}
