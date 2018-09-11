package com.nd.hilauncherdev.plugin.navigation.weather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.nd.hilauncherdev.framework.view.baseDetail.BaseDetail;
import com.nd.hilauncherdev.plugin.navigation.R;
import com.nd.hilauncherdev.plugin.navigation.helper.WeatherSpHelper;
import com.nd.hilauncherdev.plugin.navigation.helper.ZLauncherUrl;
import com.nd.hilauncherdev.plugin.navigation.util.SPUtil;
import com.nd.hilauncherdev.plugin.navigation.weather.model.City;
import com.nd.hilauncherdev.plugin.navigation.weather.model.Conditions;
import com.nd.hilauncherdev.plugin.navigation.weather.model.Forecast;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import com.tsy.sdk.myokhttp.util.MyOKhttpHeler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by Administrator on 2018/8/29.
 */

public class NavigationWeatherView extends BaseDetail{

    private View view;

    private City city;

    private List<Forecast> forecastList;

    private Conditions conditions;

    public NavigationWeatherView(@NonNull Context context) {
        this(context,null);
    }

    public NavigationWeatherView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
                                spUtil.putString(WeatherSpHelper.WEATHER_CONDITION_JSON, response);
                                spUtil.putString(WeatherSpHelper.WEATHER_FORCAST_JSON, response);
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
    }

    @Override
    protected View getDetailView() {
        view = LayoutInflater.from(getContext()).inflate(R.layout.navigation_weather_view,null);
        return view;
    }
}
