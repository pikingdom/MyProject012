package com.nd.hilauncherdev.plugin.navigation.widget;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.nd.hilauncherdev.plugin.navigation.R;

import java.util.ArrayList;

public class NavigationView extends RelativeLayout {

	/** 0屏位置 **/
	private static final int NAVI_INDEX = 0;
	/** 资讯屏位置 **/
	private static final int INFO_INDEX = 1;
	private ViewPager mViewPager;

	private NavigationPageView navigationPageView;
	private InfoPageView infoPageView;

	private ArrayList<View> mViewContainer = new ArrayList<>();

	public NavigationView(Context context, String pkgName, int channelType, String countryCode, String CUID, String channelId, ClassLoader loader, String appId, String baseDir) {
	    this(context);
	}

	public NavigationView(Context context) {
		super(context);
		initView();
	}
	protected void initView() {
		LayoutInflater.from(getContext()).inflate(R.layout.launcher_navigation_container, this);
		navigationPageView = new NavigationPageView(getContext());
		infoPageView = new InfoPageView(getContext());
		mViewContainer.add(infoPageView);
		mViewContainer.add(navigationPageView);
		initViewPager();
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
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

}
