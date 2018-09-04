package com.nd.hilauncherdev.plugin.navigation.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


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
        webView = new WebView(getContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        addView(webView,lp);
//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.jd.com");

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("zhenghonglin","webview onDetachedFromWindow");
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
}
