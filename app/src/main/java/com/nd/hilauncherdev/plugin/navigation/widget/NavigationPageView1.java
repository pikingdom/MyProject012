package com.nd.hilauncherdev.plugin.navigation.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


/**
 * Created by Administrator on 2018/8/29.
 */

public class NavigationPageView1 extends BasePageView {

    private WebView webView;
    public NavigationPageView1(Context context) {
        this(context,null);
    }

    public NavigationPageView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.RED);
        webView = new WebView(getContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        addView(webView,lp);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
        });
        webView.loadUrl("http://www.jd.com");

    }

}
