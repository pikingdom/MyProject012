package com.nd.hilauncherdev.plugin.navigation.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nd.hilauncherdev.framework.common.view.NativeInterface;
import com.nd.hilauncherdev.framework.common.view.SafeWebView;
import com.nd.hilauncherdev.framework.common.view.baseDetail.BaseDetail;


/**
 * Created by Administrator on 2018/8/29.
 */

public class NavigationWebView extends BaseDetail{
    private SafeWebView mWebView;
    private boolean hasLoad = false;
    private String url = "";
    public NavigationWebView(Context context,String url) {
        this(context);
        this.url = url;
    }
    public NavigationWebView(Context context) {
        super(context,DETAIL_FULL);
    }

    public NavigationWebView(Context context, AttributeSet attrs) {
        this(context);
    }

    private View findViews() {
        mWebView = new SafeWebView(getContext());
//        container.addView(mWebView);
        mWebView.disableAccessibility(getContext().getApplicationContext());
        initWebSettings();
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                Log.e("zhenghonglin","i:"+i);
                if(i >= 50){
                    hasLoad = true;
                    onNetDataSuccess();
                }
            }
        });
        mWebView.addJavascriptInterface(new NativeInterface(getContext()), "AndroidNative");
        return mWebView;
    }

    private void initWebSettings() {
        WebSettings webSettings = mWebView.getSettings();
        if (webSettings == null) return;
        // 支持 Js 使用
        webSettings.setJavaScriptEnabled(true);
        // 开启DOM缓存
        webSettings.setDomStorageEnabled(true);
        // 开启数据库缓存
        webSettings.setDatabaseEnabled(true);
        // 支持自动加载图片
        webSettings.setLoadsImagesAutomatically(hasKitkat());
        // 设置 WebView 的缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 支持启用缓存模式
        webSettings.setAppCacheEnabled(true);
        // 关闭密码保存提醒功能
        webSettings.setSavePassword(false);
        // 支持缩放
        webSettings.setSupportZoom(true);
        // 设置 UserAgent 属性
        webSettings.setUserAgentString("");
        // 允许加载本地 html 文件/false
        webSettings.setAllowFileAccess(true);
        // 允许通过 file url 加载的 Javascript 读取其他的本地文件,Android 4.1 之前默认是true，在 Android 4.1 及以后默认是false,也就是禁止
        webSettings.setAllowFileAccessFromFileURLs(false);
        // 允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源，
        // Android 4.1 之前默认是true，在 Android 4.1 及以后默认是false,也就是禁止
        // 如果此设置是允许，则 setAllowFileAccessFromFileURLs 不起做用
        webSettings.setAllowUniversalAccessFromFileURLs(false);

        /**
         *  关于缓存目录:
         *
         *  我自测发现以下规律，如果你有涉及到目录操作，需要自己做下验证。
         *  Android 4.4 以下：/data/data/包名/cache
         *  Android 4.4 - 5.0：/data/data/包名/app_webview/cache/
         */
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("zhenghonglin","webview onDetachedFromWindow");
    }

    @Override
    protected void netRequest() {
        if(!hasLoad){
            mWebView.loadUrl(url);
        } else {
            onNetDataSuccess();
        }
    }

    @Override
    protected View getDetailView() {
        return findViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebView.resumeTimers();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers();
    }

    @Override
    public void onLauncherStart() {
        super.onLauncherStart();
        if(!hasLoad){
            mWebView.loadUrl(url);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeView(mWebView);
        if (mWebView != null) {
            mWebView.onDestroy();
        }
        mWebView = null;
    }

    public void onNetDataFail(String msg) {
        super.onNetDataFail(msg);
    }

    public void onNetDataSuccess() {
        super.onNetDataSuccess();
    }

    @Override
    public void onPageSelected() {
//        webView.loadUrl("http://www.jd.com");
        if(!hasLoad){
            loadData();
        }
    }

    @Override
    public void onPageUnSelected() {

    }

    private boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= 19;
    }
}
