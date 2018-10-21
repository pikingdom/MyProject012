package com.nd.hilauncherdev.plugin.navigation.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nd.hilauncherdev.framework.common.view.baselist.BasePageInterface;
import com.nd.hilauncherdev.framework.common.view.baselist.ViewLife;
import com.nd.hilauncherdev.plugin.navigation.R;
import com.nd.hilauncherdev.plugin.navigation.helper.NavHelper;
import com.nd.hilauncherdev.plugin.navigation.helper.NavSpHelper;
import com.nd.hilauncherdev.plugin.navigation.helper.TagHelper;
import com.nd.hilauncherdev.plugin.navigation.helper.ZLauncherUrl;
import com.nd.hilauncherdev.plugin.navigation.widget.openpage.PageCountSetter;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.DownloadResponseHandler;
import com.tsy.sdk.myokhttp.util.ScreenUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NavigationView extends FrameLayout implements NavigationLauncherInterface,ViewLife {

	/** 0屏位置 **/
	private static final int NAVI_INDEX = 0;
	/** 资讯屏位置 **/
	private static int CURRENT_INDEX = 1;
	private ViewPager mViewPager;

	private InfoPageView infoPageView;
	private NavigationSearchView navigationSearchView;

	private Context context;

	private ArrayList<View> mViewContainer = new ArrayList<>();
	private static Activity mLauncher;

	public NavigationView(Context context, String pkgName, int channelType, String countryCode, String CUID, String channelId, ClassLoader loader, String appId, String baseDir) {
	    this(context);
	}

	public NavigationView(Context context) {
		super(context);
		MyOkHttp.getInstance().setApplicationConext(context.getApplicationContext());
		this.context = context;
		initView();
	}
	protected void initView() {
		LayoutInflater.from(getContext()).inflate(R.layout.navigation_view, this);
		View statusBarView = findViewById(R.id.statusBarView);
		ViewGroup.LayoutParams layoutParams = statusBarView.getLayoutParams();
		layoutParams.height = NavHelper.getNavigationTopMargin(getContext());
		statusBarView.setLayoutParams(layoutParams);
		statusBarView.setBackgroundColor(Color.TRANSPARENT);
		if(NavSpHelper.showInfoPageView()){
			navigationSearchView = new NavigationSearchView(getContext());
			infoPageView = new InfoPageView(getContext());
			mViewContainer.add(navigationSearchView);
			mViewContainer.add(infoPageView);
			CURRENT_INDEX = 1;
		} else {
			navigationSearchView = new NavigationSearchView(getContext());
			mViewContainer.add(navigationSearchView);
			CURRENT_INDEX = 0;
		}
		initViewPager();
		setToInfoView();
	}

	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setOffscreenPageLimit(mViewContainer.size());
		PagerAdapter adapter = new PagerAdapter() {
			@Override
			public int getCount() {
				return mViewContainer.size();
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				View pageView =  mViewContainer.get(position);
				container.addView(pageView);
				return pageView;
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView(mViewContainer.get(position));
			}

		};
		mViewPager.setAdapter(adapter);
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				PageCountSetter.getInstance().setPageIndex(mLauncher, position);
				for(int i=0;i<mViewContainer.size();i++){
					final BasePageInterface fragment = (BasePageInterface) mViewContainer.get(i);
					if(i == position){
						MyOkHttp.mHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								fragment.onPageSelected();
							}
						},300);
					} else {
						fragment.onPageUnSelected();
					}
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	/**
	 * 定位到资讯屏
	 */
	private void setToInfoView() {
		if (mViewPager.getCurrentItem() != CURRENT_INDEX) {
			mViewPager.setCurrentItem(CURRENT_INDEX);
		}
	}

	//===========================桌面反射方法开始==============================
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
		mViewPager.setCurrentItem(which);
	}

	@Override
	public void scrollToPage(int which) {
		mViewPager.setCurrentItem(which);
	}

	@Override
	public void setCUID(String CUID) {

	}

	@Override
	public void setHotWordView(List<Object> list) {
	}

	@Override
	public void upgradePlugin(String url, int ver, boolean isWifiAutoDownload) {
		//下载插件
		Log.e(TagHelper.TAG, "upgradePlugin:");
		String saveDir = Environment.getExternalStorageDirectory()+"/HWHome/WifiDownload/";
		String fileName = NavHelper.NAVIGATION_PLUGIN_FILENAME;
		String downloadUrl= String.format(ZLauncherUrl.DOWNLOADURL,NavHelper.NAVIGATION_PLUGIN_PKG);
		MyOkHttp.getInstance().download().url(downloadUrl).filePath(saveDir+fileName).tag(this)
				.enqueue(new DownloadResponseHandler() {
					@Override
					public void onFinish(File downloadFile) {
						Log.e(TagHelper.TAG, "doDownload onFinish:");
					}

					@Override
					public void onProgress(long currentBytes, long totalBytes) {
						Log.e(TagHelper.TAG, "doDownload onProgress:" + currentBytes + "/" + totalBytes);
					}

					@Override
					public void onFailure(String error_msg) {
						Log.e(TagHelper.TAG, "doDownload onFailure:" + error_msg);
					}

					@Override
					public void onStart(long totalBytes) {
						super.onStart(totalBytes);
						Log.e(TagHelper.TAG, "doDownload onStart:"+totalBytes);
					}
				});
	}

	@Override
	public void onShowingNavigationView() {
		// 相当于 onSnapToNavigation
		Log.e(TagHelper.TAG,"onShowingNavigationView");
		mViewPager.setCurrentItem(CURRENT_INDEX);
		final BasePageInterface fragment = (BasePageInterface) mViewContainer.get(CURRENT_INDEX);
		if(fragment != null){
			fragment.onPageSelected();
		}
	}

	@Override
	public void onLeavingNavigation() {
		//相当于 onSnapToWorkspace
	}

	@Override
	public void setActivity(Activity activity) {
		mLauncher = activity;
		PageCountSetter.getInstance().setPageCount(mLauncher, mViewContainer.size());
		PageCountSetter.getInstance().setPageIndex(mLauncher, CURRENT_INDEX);
	}

	@Override
	public void handleNavigationWhenLauncherOnPause() {
		//相当于activity onPause
		onPause();
	}

	@Override
	public void handleNavigationWhenLauncherOnDestroy() {
		onDestroy();
	}

	@Override
	public void handleBackKeyToNavigation() {
		Log.e(TagHelper.TAG,"handleBackKeyToNavigation:"+mViewPager.getCurrentItem());
		mViewPager.setCurrentItem(CURRENT_INDEX);
	}

	@Override
	public void handleNavigationWhenLauncherOnResume() {
		//相当于activity onResume
		onResume();
	}

	@Override
	public void onBackKeyDown() {
//		setToInfoView();
		Log.e(TagHelper.TAG,"onBackKeyDown:"+mViewPager.getCurrentItem());
	}

	@Override
	public void hideVideoView() {

	}

	@Override
	public void onLauncherStart() {
		Log.e(TagHelper.TAG,"onLauncherStart:"+CURRENT_INDEX);
		for (int i = 0; i < mViewContainer.size(); i++) {
			((ViewLife) mViewContainer.get(i)).onLauncherStart();
		}
		//统计信息
		NavHelper.startCommonAnalytic();
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

//	@Override
//	public void setPageIndex(Context ctx, int index) {
//		PageCountSetter.getInstance().setPageIndex(ctx, index);
//	}
//
//	@Override
//	public void setPageCount(Context ctx, int index) {
//		PageCountSetter.getInstance().setAllPageCount(ctx, index);
//	}
	//===========================桌面反射方法结束==============================

	//===========================自定义View生命周期 begin==============================
	@Override
	public void onResume() {
		if(mViewContainer != null && mViewContainer.size() >0){
			for(View view:mViewContainer){
				ViewLife viewLife = (ViewLife) view;
				viewLife.onResume();
			}
		}
	}

	@Override
	public void onPause() {
		if(mViewContainer != null && mViewContainer.size() >0){
			for(View view:mViewContainer){
				ViewLife viewLife = (ViewLife) view;
				viewLife.onPause();
			}
		}
	}

	@Override
	public void onDestroy() {
		if(mViewContainer != null && mViewContainer.size() >0){
			for(View view:mViewContainer){
				ViewLife viewLife = (ViewLife) view;
				viewLife.onDestroy();
			}
		}
		mLauncher = null;
	}
	//===========================自定义View生命周期 end==============================
	public static Activity getLauncher(){
		return mLauncher;
	}

	public static String getPkgName(){
		if(mLauncher == null){
			return "";
		}
		return mLauncher.getPackageName();
	}
}
