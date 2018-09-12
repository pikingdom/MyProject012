package com.nd.hilauncherdev.plugin.weather;

import android.app.Activity;
import android.os.Bundle;

import com.tsy.sdk.myokhttp.MyOkHttp;

/**
 * Created by Administrator on 2018\8\28 0028.
 */

public class WeatherMainActivity extends Activity {

    private WeatherView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyOkHttp.getInstance().setApplicationConext(getApplicationContext());
        setContentView(R.layout.weather_main_activity);
        webView = (WeatherView) findViewById(R.id.weatherview);
        webView.loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.onDestroy();
    }
}
