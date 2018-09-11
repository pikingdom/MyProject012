package com.nd.hilauncherdev.plugin.navigation.weather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nd.hilauncherdev.framework.view.baseDetail.BaseDetail;
import com.nd.hilauncherdev.plugin.navigation.R;
import com.nd.hilauncherdev.plugin.navigation.helper.WeatherSpHelper;
import com.nd.hilauncherdev.plugin.navigation.helper.ZLauncherUrl;
import com.nd.hilauncherdev.plugin.navigation.util.SPUtil;
import com.nd.hilauncherdev.plugin.navigation.weather.model.City;
import com.nd.hilauncherdev.plugin.navigation.weather.model.Conditions;
import com.nd.hilauncherdev.plugin.navigation.weather.model.Forecast;
import com.nd.hilauncherdev.plugin.navigation.weather.tools.WeatherHelper;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import java.util.Calendar;
import java.util.List;


/**
 * Created by Administrator on 2018/8/29.
 */

public class NavigationWeatherView extends BaseDetail{

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

    public NavigationWeatherView(@NonNull Context context) {
        this(context,null);
    }

    public NavigationWeatherView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public String getCityName(){
        if(city != null){
            return city.getCity();
        }
        return "";
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
                MyOkHttp.getInstance().postH().tag(this).url(ZLauncherUrl.WEATHER_101)
                        .addParam("location", WeatherSpHelper.getLonAndLat()).enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        onNetDataSuccess();
                    }

                    @Override
                    public void onJsonParse(int statusCode,String response) throws Exception {
                        //json解析 非ui线程
                        City city = WeatherSpHelper.parseCity(response,false);
                        if(city == null){
                            onFailure(statusCode,"");
                        } else {
                            String conditionResponse =MyOkHttp.getInstance().postH().addParam("location",city.getWoeid()).url(ZLauncherUrl.WEATHER_102).executeStr();
                            Conditions conditions = WeatherSpHelper.parseConditions(conditionResponse,false);

                            String forecastResponse = MyOkHttp.getInstance().postH().addParam("location",city.getWoeid()).url(ZLauncherUrl.WEATHER_103).executeStr();
                            List<Forecast> forecastList = WeatherSpHelper.parseForecastList(forecastResponse,false);
                            if(conditions != null && forecastList != null){
                                SPUtil spUtil = new SPUtil();
                                spUtil.putString(WeatherSpHelper.CITY_INFO_JSON, response);
                                spUtil.putString(WeatherSpHelper.WEATHER_CONDITION_JSON, conditionResponse);
                                spUtil.putString(WeatherSpHelper.WEATHER_FORCAST_JSON, forecastResponse);
                                spUtil.putLong(WeatherSpHelper.WEATHER_REFRESH_TIME, System.currentTimeMillis());
                                NavigationWeatherView.this.city = city;
                                NavigationWeatherView.this.conditions = conditions;
                                NavigationWeatherView.this.forecastList = forecastList;
                            } else {
                                onFailure(statusCode,"");
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }
                });
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
        view = LayoutInflater.from(getContext()).inflate(R.layout.navigation_weather_view,null);
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
}
