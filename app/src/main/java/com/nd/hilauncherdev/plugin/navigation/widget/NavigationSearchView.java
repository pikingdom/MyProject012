package com.nd.hilauncherdev.plugin.navigation.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.nd.hilauncherdev.plugin.navigation.R;
import com.nd.hilauncherdev.framework.common.view.baselist.BasePageInterface;
import com.nd.hilauncherdev.plugin.navigation.bean.HotwordItemInfo;
import com.nd.hilauncherdev.plugin.navigation.infopage.NewsPage;
import com.nd.hilauncherdev.plugin.navigation.infopage.help.InvenoHelper;
import com.nd.hilauncherdev.plugin.navigation.loader.NaviWordLoader;
import com.nd.hilauncherdev.plugin.navigation.util.SystemUtil;
import com.nd.hilauncherdev.plugin.navigation.widget.search.hotword.ServerHotwordGenerator;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2018/8/29.
 */

public class NavigationSearchView extends BasePageView implements BasePageInterface ,View.OnClickListener{

    private RelativeLayout search_rl;
    private ImageView search_img;
    private TextView search_tv;
    private TextSwitcher search_ts;
    private ServerHotwordGenerator hotwordGenerator;

    private FrameLayout container;
    private NavigationSitesView navigationSitesView;
    private NewsPage newsPage;
    private boolean hasLoad = false;

    private ViewSwitcher.ViewFactory mFactory = new ViewSwitcher.ViewFactory() {

        @Override
        public View makeView() {
            // Create a new TextView
            TextView t = new TextView(getContext());
            t.setTextColor(Color.parseColor("#88000000"));
            t.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
            FrameLayout.LayoutParams lp= new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            t.setLayoutParams(lp);
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
            return t;
        }
    };

    public NavigationSearchView(Context context) {
        this(context,null);
    }

    public NavigationSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.navigation_search_page_view,this);
        findViews();
        initData();
    }

    private void findViews() {
        search_rl = (RelativeLayout) findViewById(R.id.search_rl);
        search_img = (ImageView) findViewById(R.id.search_img);
        search_tv = (TextView) findViewById(R.id.search_tv);
        search_ts = (TextSwitcher) findViewById(R.id.search_ts);
        search_ts.setFactory(mFactory);
        search_rl.setOnClickListener(this);

        Animation in = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
        search_ts.setInAnimation(in);
        search_ts.setOutAnimation(out);
        search_ts.setText(getContext().getString(R.string.navigation_search_default_key));

        container = (FrameLayout) findViewById(R.id.container);
        navigationSitesView = new NavigationSitesView(getContext());
//        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(900, MeasureSpec.EXACTLY);
//        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(400, MeasureSpec.EXACTLY);
//        navigationSitesView.measure(widthMeasureSpec,heightMeasureSpec);
        newsPage = new NewsPage.Builder().setScenarioType(InvenoHelper.SCENARIO_RECOMMENT).addHeadView(navigationSitesView).build(getContext());
        container.addView(newsPage);
    }

    private void initData() {

    }

    public void loadData(){

    }

    @Override
    public void onLauncherStart() {
        super.onLauncherStart();
        navigationSitesView.onLauncherStart();
    }

    public void onNetDataFail(String msg) {

    }
    public void onNetDataSuccess(){
        //重新刷新
        hasLoad = false;
        loadData();
    }

    @Override
    public void onPageSelected() {
        Log.e("sitesview","1111:"+navigationSitesView.getWidth()+","+navigationSitesView.getHeight());
        if(!newsPage.hasLoad()){
            newsPage.loadData();
        }
        startHotwordSwitch();
    }

    @Override
    public void onPageUnSelected() {
        stopHotwordSwitch();
    }

    private void onNetDataFail(){

    }

    private boolean isSwitchering = false;

    public void startHotwordSwitch() {
        if (!isSwitchering && isHotwordAvailable()) {
            if (null != search_ts) {
                if (hotwordGenerator != null) {
                    HotwordItemInfo current = hotwordGenerator.getCurrentHotword();
                    if (current != null) {
                        search_ts.setCurrentText(current.name);
                        search_ts.setTag(current);
                    }
                }
            }
            startNextTask(2000);
            isSwitchering = true;
        }
    }

    public void stopHotwordSwitch() {
        if (isSwitchering) {
            handler.removeMessages(MSG_NEXT_TEXT_SWITER);
            isSwitchering = false;
        }
    }
    public static final int MSG_NEXT_TEXT_SWITER = 100;

    public void startNextTask(int next) {
        handler.removeMessages(MSG_NEXT_TEXT_SWITER);
        Message nextMsg = new Message();
        nextMsg.what = MSG_NEXT_TEXT_SWITER;
        handler.sendMessageDelayed(nextMsg, next);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (MSG_NEXT_TEXT_SWITER == msg.what) {
                updateHotwordSwitcher();
            }
        }
    };

    public void updateHotwordSwitcher() {
        if(hotwordGenerator != null && hotwordGenerator.isCounterHit()){
            return;
        }
        if (null != search_ts) {
            if (hotwordGenerator != null) {
                HotwordItemInfo current = hotwordGenerator.popupNextHotword();
                if (current != null) {
                    if(hotwordGenerator != null){
                        hotwordGenerator.increaseCounter();
                    }
                    search_ts.setText(current.name);
                    search_ts.setTag(current);
//                    PluginUtil.invokeSubmitEvent(BaseNavigationSearchView.activity, AnalyticsConstant.NAVIGATION_DZ_SEARCH_PAGE_SEARCH_INPUT, "zs");
                }
            }
        }
        startNextTask(4000);
    }

    public boolean isHotwordAvailable() {
        if (hotwordGenerator != null) {
            return hotwordGenerator.isHotwordsAvailable();
        }
        return false;
    }

    @Override
    public void setHotWordView(List<Object> list) {
        super.setHotWordView(list);
        ArrayList<HotwordItemInfo> itemList = NaviWordLoader.convertHotwordList(list);
        if (hotwordGenerator != null) {
            hotwordGenerator.appendHotwords(itemList);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v == search_rl){
            startSearchActivity("");
        }
    }

    private void startSearchActivity(CharSequence text) {
        Intent intent = new Intent();
        intent.setClassName(getContext(), "com.nd.hilauncherdev.launcher.navigation.SearchActivity");
        if (!TextUtils.isEmpty(text)) {
            intent.putExtra("defaultWord", text);
        }
        intent.putExtra("from", "open_from_navigation");
        SystemUtil.startActivityForResultSafely(NavigationView.getLauncher(), intent, SystemUtil.REQUEST_SEARCH_ACTIVITY_POSITION);
//        PluginUtil.invokeSubmitEvent(BaseNavigationSearchView.activity, AnalyticsConstant.SEARCH_PERCENT_CONVERSION_SEARCH_INLET, "1");
    }
}
