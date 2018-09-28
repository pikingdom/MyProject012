package com.nd.hilauncherdev.plugin.weather;

import android.app.Activity;
import android.os.Bundle;

import com.tsy.sdk.myokhttp.MyOkHttp;

/**
 * Created by Administrator on 2018\8\28 0028.
 */

public class WeatherMainActivity extends Activity {
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
        weatherActivityView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        weatherActivityView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        weatherActivityView.onDestroy();
    }
}
