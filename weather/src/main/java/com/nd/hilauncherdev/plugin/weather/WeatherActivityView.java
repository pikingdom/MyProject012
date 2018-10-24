package com.nd.hilauncherdev.plugin.weather;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nd.hilauncherdev.framework.common.view.CommonPluginView;

/**
 * Created by Administrator on 2018/9/28.
 */

public class WeatherActivityView extends FrameLayout implements WeatherView.WeatherDataUpdateListener,CommonPluginView {
    private WeatherView webView;
    private TextView city_tv;
    private TextView loc_tv;
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
        webView.setWeatherDataUpdateListener(this);
        city_tv = (TextView) findViewById(R.id.city_tv);
        loc_tv = (TextView) findViewById(R.id.loc_tv);
        loc_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.startLoc();
            }
        });
    }

    public void loadData(){
        webView.loadData();
    }
    @Override
    public void onResume() {
        webView.onResume();
    }
    @Override
    public void onPause() {
        webView.onPause();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }
    @Override
    public void onDestroy() {
        webView.onDestroy();
    }

    @Override
    public void onCityNameCallback(String name) {
        city_tv.setText(name);
    }
}
