package com.nd.hilauncherdev.plugin.weather;

import android.os.Bundle;

import com.android.dynamic.plugin.PluginActivity;
import com.tsy.sdk.myokhttp.MyOkHttp;

/**
 * Created by Administrator on 2018\8\28 0028.
 */

public class WeatherMainActivity extends PluginActivity {
    private WeatherActivityView weatherActivityView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyOkHttp.getInstance().setApplicationConext(getApplicationContext());
        weatherActivityView = new WeatherActivityView(this);
        setContentView(weatherActivityView);
        weatherActivityView.loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(weatherActivityView != null){
            weatherActivityView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(weatherActivityView != null){
            weatherActivityView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(weatherActivityView != null){
            weatherActivityView.onDestroy();
        }
    }
}
