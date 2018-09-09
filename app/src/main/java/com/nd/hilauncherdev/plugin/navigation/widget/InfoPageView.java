package com.nd.hilauncherdev.plugin.navigation.widget;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nd.hilauncherdev.plugin.navigation.R;
import com.nd.hilauncherdev.plugin.navigation.base.BasePageInterface;
import com.nd.hilauncherdev.plugin.navigation.base.BaseRecyclerList;
import com.nd.hilauncherdev.plugin.navigation.base.ViewLife;
import com.nd.hilauncherdev.plugin.navigation.helper.WebViewUrl;
import com.nd.hilauncherdev.plugin.navigation.infopage.NewsPage;
import com.nd.hilauncherdev.plugin.navigation.infopage.help.InvenoHelper;
import com.tsy.sdk.myokhttp.MyOkHttp;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/29.
 */

public class InfoPageView extends BasePageView implements BasePageInterface {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<View> mViewContainer = new ArrayList<>();
    private String[] titles = {"推荐","国际","唯品会","爱阅读"};

    private boolean hasLoad = false;

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
        mViewContainer.add(new NewsPage(getContext(), InvenoHelper.SCENARIO_RECOMMENT));
        mViewContainer.add(new NewsPage(getContext(), InvenoHelper.SCENARIO_GLOBAL));
        mViewContainer.add(new NavigationWebView(getContext(), WebViewUrl.VIP_URL));
        mViewContainer.add(new NavigationWebView(getContext(), WebViewUrl.iyd_URL));

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
                final BasePageInterface fragment = (BasePageInterface) mViewContainer.get(position);
                MyOkHttp.mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragment.onPageSelected();
                    }
                },300);

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

    @Override
    public void onResume() {
        super.onResume();
        if(mViewContainer !=null && mViewContainer.size()>0){
            for(View view:mViewContainer){
                ViewLife viewLife = (ViewLife) view;
                viewLife.onResume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mViewContainer !=null && mViewContainer.size()>0){
            for(View view:mViewContainer){
                ViewLife viewLife = (ViewLife) view;
                viewLife.onPause();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mViewContainer !=null && mViewContainer.size()>0){
            for(View view:mViewContainer){
                ViewLife viewLife = (ViewLife) view;
                viewLife.onDestroy();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("zhenghonglin","infopage onDetachedFromWindow");
    }


    @Override
    public void onPageSelected() {
        BasePageInterface fragment = (BasePageInterface) mViewContainer.get(mViewPager.getCurrentItem());
        fragment.onPageSelected();
    }

    @Override
    public void onPageUnSelected() {

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
