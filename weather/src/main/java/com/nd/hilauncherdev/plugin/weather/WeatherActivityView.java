package com.nd.hilauncherdev.plugin.weather;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2018/9/28.
 */

public class WeatherActivityView extends FrameLayout{
    private WeatherView webView;

    public WeatherActivityView(Context context) {
        this(context,null);
    }

    public WeatherActivityView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WeatherActivityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.weather_main_activity,this);
        webView = (WeatherView) findViewById(R.id.weatherview);
    }

    public void loadData(){
        webView.loadData();
    }

    public void onResume() {
        webView.onResume();
    }

    public void onPause() {
        webView.onPause();
    }

    public void onDestroy() {
        webView.onDestroy();
    }
}
