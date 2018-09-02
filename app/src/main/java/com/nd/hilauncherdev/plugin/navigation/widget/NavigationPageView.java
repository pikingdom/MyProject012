package com.nd.hilauncherdev.plugin.navigation.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.just.agentwebX5.AgentWebX5;
import com.just.agentwebX5.DefaultWebClient;


/**
 * Created by Administrator on 2018/8/29.
 */

public class NavigationPageView extends BasePageView {

    public NavigationPageView(Context context) {
        this(context,null);
    }

    public NavigationPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.RED);

//        mAgentWeb = AgentWeb.with((Activity) getContext())
//                .setAgentWebParent(this, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
//                .useDefaultIndicator()
//                .setWebChromeClient(mWebChromeClient)
//                .setWebViewClient(mWebViewClient)
//                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
//                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
//                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
//                .createAgentWeb()
//                .ready()
//                .go("https://m.jd.com/");


//        AgentWeb mAgentWeb = AgentWeb.with((Activity) getContext())
//                .setAgentWebParent(this, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
//                .useDefaultIndicator()
//                .createAgentWeb()
//                .ready()
//                .go("http://www.jd.com");

        AgentWebX5 mAgentWebX5 = AgentWebX5.with((Activity) getContext())//
                .setAgentWebParent(this, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()//
                .defaultProgressBarColor()
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
                .interceptUnkownScheme()
                .setSecutityType(AgentWebX5.SecurityType.strict)
                .createAgentWeb()//
                .ready()
                .go("http://www.jd.com");
    }

}
