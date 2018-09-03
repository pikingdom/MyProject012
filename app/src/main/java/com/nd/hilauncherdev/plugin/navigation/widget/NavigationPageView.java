package com.nd.hilauncherdev.plugin.navigation.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;


/**
 * Created by Administrator on 2018/8/29.
 */

public class NavigationPageView extends BasePageView {

    private WebView webView;
    public NavigationPageView(Context context) {
        this(context,null);
    }

    public NavigationPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.RED);
//        LayoutInflater.from(getContext()).inflate(R.layout.webview_page,this);
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

//        AgentWebX5 mAgentWebX5 = AgentWebX5.with((Activity) getContext())//
//                .setAgentWebParent(this, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
//                .useDefaultIndicator()//
//                .defaultProgressBarColor()
//                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
//                .interceptUnkownScheme()
//                .setSecutityType(AgentWebX5.SecurityType.strict)
//                .createAgentWeb()//
//                .ready()
//                .go("http://www.jd.com");

//        webView = (WebView) findViewById(R.id.webview);
        webView = new WebView(getContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        addView(webView,lp);
//        webView.loadUrl("http://www.jd.com");

        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setAppCacheEnabled(true);
//        //设置 缓存模式
//        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//        // 开启 DOM storage API 功能
//        webView.getSettings().setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            //覆盖shouldOverrideUrlLoading 方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl("http://www.jd.com");



    }

}
