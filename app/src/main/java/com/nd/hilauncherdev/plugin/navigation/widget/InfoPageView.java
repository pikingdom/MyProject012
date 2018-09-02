package com.nd.hilauncherdev.plugin.navigation.widget;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nd.hilauncherdev.plugin.navigation.R;
import com.nd.hilauncherdev.plugin.navigation.infopage.BaseRecyclerList;
import com.nd.hilauncherdev.plugin.navigation.infopage.NewsPage;
import com.nd.hilauncherdev.plugin.navigation.infopage.NewsPage2;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/29.
 */

public class InfoPageView extends BasePageView {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<View> mViewContainer = new ArrayList<>();
    private String[] titles = {"推荐","222"};

    public InfoPageView(Context context) {
        this(context,null);
    }

    public InfoPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.navigation_info_page_view,this);
        findViews();
        initData();
    }

    private void initData() {
        mViewContainer.add(new NewsPage(getContext()));
        mViewContainer.add(new NewsPage2(getContext()));

        final InfoViewPagerAdapter adapter = new InfoViewPagerAdapter();
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(mViewContainer.size());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BaseRecyclerList fragment = (BaseRecyclerList) mViewContainer.get(position);
                if(!fragment.hasLoad()){
                    fragment.loadData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void findViews() {
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    @Override
    public void onLauncherStart() {
        super.onLauncherStart();
        refreshCurrent();
    }

    private void refreshCurrent(){
        int curr = 0;
        mViewPager.setCurrentItem(curr);
        BaseRecyclerList currentLayout = (BaseRecyclerList) mViewContainer.get(curr);
        if(!currentLayout.hasLoad()){
            currentLayout.loadData();
        }
    }

    class InfoViewPagerAdapter extends PagerAdapter{

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

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
