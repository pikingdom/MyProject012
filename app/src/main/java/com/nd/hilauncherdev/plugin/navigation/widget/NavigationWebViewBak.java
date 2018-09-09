package com.nd.hilauncherdev.plugin.navigation.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.nd.hilauncherdev.plugin.navigation.base.BasePageInterface;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


/**
 * Created by Administrator on 2018/8/29.
 */

public class NavigationWebViewBak extends BasePageView implements BasePageInterface {

    private WebView webView;
    public NavigationWebViewBak(Context context) {
        this(context,null);
    }

    public NavigationWebViewBak(Context context, AttributeSet attrs) {
        super(context, attrs);
        webView = new WebView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        addView(webView,lp);
//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

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

    public void onNetDataFail(String msg) {

    }

    public void onNetDataSuccess() {

    }

    @Override
    public void onPageSelected() {
        webView.loadUrl("http://www.jd.com");
    }

    @Override
    public void onPageUnSelected() {

    }
}
