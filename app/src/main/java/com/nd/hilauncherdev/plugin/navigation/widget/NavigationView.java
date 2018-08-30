package com.nd.hilauncherdev.plugin.navigation.widget;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.nd.hilauncherdev.plugin.navigation.R;
import com.nd.hilauncherdev.plugin.navigation.widget.openpage.PageCountSetter;

import java.util.ArrayList;
import java.util.List;

public class NavigationView extends RelativeLayout implements NavigationLauncherInterface{

	/** 0屏位置 **/
	private static final int NAVI_INDEX = 0;
	/** 资讯屏位置 **/
	private static final int INFO_INDEX = 1;
	private ViewPager mViewPager;

	private NavigationPageView navigationPageView;
	private InfoPageView infoPageView;

	private Context context;

	private ArrayList<View> mViewContainer = new ArrayList<>();
	private Activity mLauncher;

	public NavigationView(Context context, String pkgName, int channelType, String countryCode, String CUID, String channelId, ClassLoader loader, String appId, String baseDir) {
	    this(context);
	}

	public NavigationView(Context context) {
		super(context);
		this.context = context;
		initView();
	}
	protected void initView() {
		LayoutInflater.from(getContext()).inflate(R.layout.launcher_navigation_container, this);
		navigationPageView = new NavigationPageView(getContext());
		infoPageView = new InfoPageView(getContext());
		mViewContainer.add(navigationPageView);
		mViewContainer.add(infoPageView);
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
				View pageView = mViewContainer.get(position);
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
		if (mViewPager.getCurrentItem() != INFO_INDEX) {
			mViewPager.setCurrentItem(INFO_INDEX);
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

	}

	@Override
	public void onShowingNavigationView() {
		// 相当于 onSnapToNavigation
	}

	@Override
	public void onLeavingNavigation() {
		//相当于 onSnapToWorkspace
	}

	@Override
	public void setActivity(Activity activity) {
		mLauncher = activity;
		PageCountSetter.getInstance().setPageCount(mLauncher, mViewContainer.size());
		PageCountSetter.getInstance().setPageIndex(mLauncher, INFO_INDEX);
	}

	@Override
	public void handleNavigationWhenLauncherOnPause() {
		//相当于activity onPause
	}

	@Override
	public void handleBackKeyToNavigation() {
		mViewPager.setCurrentItem(NAVI_INDEX);
	}

	@Override
	public void handleNavigationWhenLauncherOnResume() {
		//相当于activity onResume
	}

	@Override
	public void onBackKeyDown() {
//		setToInfoView();
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
}
