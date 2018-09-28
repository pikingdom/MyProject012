package com.nd.hilauncherdev.plugin.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.nd.hilauncherdev.plugin.CommonPluginView;
import com.nd.hilauncherdev.plugin.navigation.R;

/**
 * Created by Administrator on 2018/9/28.
 */

public class WeatherPluginView extends FrameLayout implements CommonPluginView {
    public WeatherPluginView(@NonNull Context context) {
        this(context,null);
    }

    public WeatherPluginView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WeatherPluginView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.plugin_weather_view,this);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

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

    }
}
