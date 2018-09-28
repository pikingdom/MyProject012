package com.nd.hilauncherdev.plugin.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nd.hilauncherdev.framework.common.view.baseDetail.BaseDetail;
import com.nd.hilauncherdev.plugin.weather.helper.WeatherSpHelper;
import com.nd.hilauncherdev.plugin.weather.model.City;
import com.nd.hilauncherdev.plugin.weather.model.Conditions;
import com.nd.hilauncherdev.plugin.weather.model.Forecast;
import com.nd.hilauncherdev.plugin.weather.tools.WeatherHelper;
import com.tsy.sdk.myokhttp.MyOkHttp;

import java.util.Calendar;
import java.util.List;


/**
 * Created by Administrator on 2018/8/29.
 */

public class WeatherView extends BaseDetail{

    private View view;

    TextView current_weather_temperature;
    TextView current_weather_code_text;
    TextView current_weather_range;
    TextView tv_humidity;
    TextView tv_wind_direction;
    TextView tv_wind_speed;
    TextView tv_visibility;
    LinearLayout forecast_ll;
    TextView weather_recent_update_tv;

    private City city;

    private List<Forecast> forecastList;

    private Conditions conditions;

    private WeatherDataUpdateListener weatherDataUpdateListener;
    public static final String ACTION_WEATHER_UPDATE_UI = "com.nd.hilauncherdev.weather.provider.weather.updateui";

    public WeatherView(@NonNull Context context) {
        this(context,null);
    }

    public WeatherView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setWeatherDataUpdateListener(WeatherDataUpdateListener listener){
        this.weatherDataUpdateListener = listener;
    }

    public String getCityName(){
        if(city != null){
            return city.getCity();
        }
        return "";
    }

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_WEATHER_UPDATE_UI)) {
                initDataFromSp();
                onNetDataSuccess();
            }
        }
    };

    private void initDataFromSp(){
        City city = WeatherSpHelper.getCityFromSp();
        Conditions conditions = WeatherSpHelper.getConditionsFromSp();
        List<Forecast> forecastList = WeatherSpHelper.getForecastListFromSp();
        this.city = city;
        this.conditions = conditions;
        this.forecastList = forecastList;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_WEATHER_UPDATE_UI);
        getContext().registerReceiver(mIntentReceiver, filter);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getContext().unregisterReceiver(mIntentReceiver);
    }

    @Override
    protected void netRequest() {
        if(!hasLoad()){
            //加载数据
            City city = WeatherSpHelper.getCityFromSp();
            if(city != null){
                this.city = city;
                this.conditions = WeatherSpHelper.getConditionsFromSp();
                this.forecastList = WeatherSpHelper.getForecastListFromSp();
                onNetDataSuccess();
            } else {
                WeatherSpHelper.updateWeatherData();
            }
        } else {
            onNetDataSuccess();
        }
    }

    @Override
    public void onNetDataSuccess() {
        super.onNetDataSuccess();
        //刷新数据
        Log.e("zhenghonglin","weather success");
        updateUI();
    }

    private void updateUI() {
        if(weatherDataUpdateListener != null && city != null){
            weatherDataUpdateListener.onCityNameCallback(city.getCity());
        }
        if (conditions != null) {
            current_weather_temperature.setText(conditions.getDegrees() + "");
            current_weather_code_text.setText(conditions.getWeatherText());

            tv_humidity.setText(conditions.getRelativeHumidity() + "%");
            tv_wind_direction.setText(conditions.getWindDir());
            tv_wind_speed.setText(conditions.getWindSpeed() + "公里/小时");
            tv_visibility.setText(conditions.getVisibility() + "公里");
        }
        if (forecastList != null && forecastList.size() > 0) {
            Forecast todayForecast = forecastList.get(0);
            String degrssUnit = getContext().getString(R.string.weather_degree_unit);
            String range = todayForecast.getMinTemperature() + degrssUnit + "-" + todayForecast.getMaxTemperature() + degrssUnit;
            current_weather_range.setText(range);
        }
        updateUI_forcast();
        updateUI_recent_update();
    }

    private void updateUI_recent_update(){
        long lastUpdate = WeatherSpHelper.getRefreshTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(lastUpdate);
        int minute = c.get(Calendar.MINUTE);
        String minuteStr=minute+"";
        if(minute<10){
            minuteStr = "0"+minute;
        }
        String hourAndMinute = c.get(Calendar.HOUR_OF_DAY)+":"+minuteStr;
        weather_recent_update_tv.setText(String.format(getContext().getString(R.string.weather_view_recent_update),hourAndMinute));
    }

    private void updateUI_forcast(){
        if(forecastList == null || forecastList.size() ==0){
            return;
        }
        String[] array = getContext().getResources().getStringArray(R.array.calendar_dynamic_icon_a_week);
        long[] dateArray = new long[]{System.currentTimeMillis(),
                86400000L + System.currentTimeMillis(),
                172800000L + System.currentTimeMillis()};
        String[] dateShow = new String[dateArray.length];
        Calendar ca = Calendar.getInstance();
        for (int i = 0; i < dateArray.length; i++) {
            ca.setTimeInMillis(dateArray[i]);
            int dayOfWeek = ca.get(Calendar.DAY_OF_WEEK);
            String dayOfWeekStr = array[dayOfWeek - 1];
            dateShow[i] = dayOfWeekStr;
        }
        String degrssUnit = getContext().getString(R.string.weather_degree_unit);
        for (int index = 0; index < forecast_ll.getChildCount() && index < forecastList.size(); index++) {
            LinearLayout linearLayout = (LinearLayout) forecast_ll.getChildAt(index) ;
            Forecast forecast = forecastList.get(index);
            //设置星期几
            TextView dateView = (TextView) linearLayout.findViewById(R.id.forecast_week) ;
            dateView.setText(dateShow[index]);
            ImageView ivIcon = (ImageView) linearLayout.findViewById(R.id.forecast_icon) ;
            ivIcon.setImageDrawable(WeatherHelper.getWeatherIcon(getContext(), forecast.getDayIcon()));
            TextView forecast_tmp = (TextView) linearLayout.findViewById(R.id.forecast_tmp) ;
            String range = forecast.getMinTemperature() + degrssUnit + "/" + forecast.getMaxTemperature() + degrssUnit;
            forecast_tmp.setText(range);
        }
    }

    @Override
    protected View getDetailView() {
        view = LayoutInflater.from(getContext()).inflate(R.layout.weather_view,null);
        return view;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViews();
    }

    private void findViews(){
        current_weather_temperature = (TextView) findViewById(R.id.current_weather_temperature);
        current_weather_code_text = (TextView) findViewById(R.id.current_weather_code_text);
        current_weather_range = (TextView) findViewById(R.id.current_weather_range);
        forecast_ll = (LinearLayout) findViewById(R.id.forecast_ll);

        tv_humidity = (TextView) findViewById(R.id.tv_humidity);
        tv_wind_direction = (TextView) findViewById(R.id.tv_wind_direction);
        tv_wind_speed = (TextView) findViewById(R.id.tv_wind_speed);
        tv_visibility = (TextView) findViewById(R.id.tv_visibility);
        weather_recent_update_tv = (TextView) findViewById(R.id.weather_recent_update_tv);
    }

    public void startLoc() {
        //开始定位 外部调用
        mRefreshLayout.setRefreshing(true);
        MyOkHttp.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        },3000);
        WeatherSpHelper.startLocation(getContext(),true);
    }

    public interface WeatherDataUpdateListener{
        public void onCityNameCallback(String name);
    }
}
