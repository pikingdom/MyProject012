package com.nd.hilauncherdev.plugin.weather.helper;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.nd.hilauncherdev.framework.common.util.ThreadUtil;
import com.nd.hilauncherdev.plugin.weather.WeatherView;
import com.nd.hilauncherdev.plugin.weather.loc.LocationService;
import com.nd.hilauncherdev.plugin.weather.model.City;
import com.nd.hilauncherdev.plugin.weather.model.Conditions;
import com.nd.hilauncherdev.plugin.weather.model.Forecast;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.util.MyOKhttpHeler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/11.
 */

public class WeatherSpHelper {

    /**
     * 定位信息  经度,纬度
     */
    public static final String LON_AND_LAT = "lon_and_lat";

    /**
     * 城市详情
     */
    public static final String CITY_INFO_JSON = "city_info_json";
    /**
     * 天气详情
     */
    public static final String WEATHER_CONDITION_JSON = "weather_condition_json";
    /**
     * 天气预报
     */
    public static final String WEATHER_FORCAST_JSON = "weather_forcast_json";

    /**
     * 上次刷新时间 refresh
     */
    public static final String WEATHER_REFRESH_TIME = "weather_refresh_time";

    /**
     * 上次定位时间 refresh
     */
    public static final String WEATHER_LOC_TIME = "weather_loc_time";

    public static String getLonAndLat() {
        SPUtil spUtil = new SPUtil();
        return spUtil.getString(LON_AND_LAT);
    }

    public static long getRefreshTime() {
        SPUtil spUtil = new SPUtil();
        return spUtil.getLong(WEATHER_REFRESH_TIME,0);
    }

    public static long getLocTime() {
        SPUtil spUtil = new SPUtil();
        return spUtil.getLong(WEATHER_LOC_TIME,0);
    }

    public static City getCityFromSp(){
        SPUtil spUtil = new SPUtil();
        String cityJson = spUtil.getString(CITY_INFO_JSON);
        try {
            City city = parseCity(cityJson,false);
            return city;
        }catch (Exception e){
            return null;
        }
    }

    public static Conditions getConditionsFromSp(){
        SPUtil spUtil = new SPUtil();
        String conditionsJson = spUtil.getString(WEATHER_CONDITION_JSON);
        try {
            Conditions conditions = parseConditions(conditionsJson,false);
            return conditions;
        }catch (Exception e){
            return null;
        }
    }

    public static List<Forecast> getForecastListFromSp(){
        SPUtil spUtil = new SPUtil();
        String forecastJson = spUtil.getString(WEATHER_FORCAST_JSON);
        try {
            List<Forecast> list = parseForecastList(forecastJson,false);
            return list;
        }catch (Exception e){
            return null;
        }
    }


    public static City parseCity(String response, boolean save2sp) throws JSONException {
        if (MyOKhttpHeler.isEmpty(response)) return null;
        String content = response;
        City city = new City();
        JSONObject jsonObject = new JSONObject(content);
        JSONObject cityJsonObjec = jsonObject.getJSONArray("HeWeather6").getJSONObject(0);
        JSONObject basicJsonBojec = cityJsonObjec.getJSONArray("basic").getJSONObject(0);
        city.setWoeid(basicJsonBojec.getString("cid"));
        city.setCity(basicJsonBojec.getString("location"));
        city.setProvince(basicJsonBojec.getString("admin_area"));
        city.setCountry(basicJsonBojec.getString("cnty"));
        city.setLatitude(basicJsonBojec.getDouble("lat"));
        city.setLongitude(basicJsonBojec.getDouble("lon"));
        if (save2sp) {
            SPUtil spUtil = new SPUtil();
            spUtil.putString(CITY_INFO_JSON, response);
        }
        return city;
    }

    public static Conditions parseConditions(String response, boolean save2sp) throws JSONException {
        if (MyOKhttpHeler.isEmpty(response)) return null;
        Conditions conditions = new Conditions();
        String content = response;
        JSONObject jsonObject = new JSONObject(content);
        JSONObject weatherJsonObjec = jsonObject.getJSONArray("HeWeather6").getJSONObject(0);
        conditions.setWeatherText(weatherJsonObjec.getJSONObject("now").getString("cond_txt"));
        conditions.setWeatherIcon(weatherJsonObjec.getJSONObject("now").getInt("cond_code"));
        conditions.setDegrees(weatherJsonObjec.getJSONObject("now").getInt("tmp"));
        conditions.setRealFeelDegrees(weatherJsonObjec.getJSONObject("now").getInt("fl"));
        conditions.setRelativeHumidity(weatherJsonObjec.getJSONObject("now").getInt("hum"));
        conditions.setWindSpeed(weatherJsonObjec.getJSONObject("now").getInt("wind_spd"));
        conditions.setWindDir(weatherJsonObjec.getJSONObject("now").getString("wind_dir"));
        conditions.setVisibility(weatherJsonObjec.getJSONObject("now").getInt("vis"));
        conditions.setPressure(weatherJsonObjec.getJSONObject("now").getInt("pres"));
        if (save2sp) {
            SPUtil spUtil = new SPUtil();
            spUtil.putString(WEATHER_CONDITION_JSON, response);
        }
        return conditions;
    }

    public static List<Forecast> parseForecastList(String response, boolean save2sp) throws JSONException {
        if (MyOKhttpHeler.isEmpty(response)) return null;
        String content = response;
        List<Forecast> forecasts = new ArrayList<Forecast>();
        JSONObject jsonObject = new JSONObject(content);
        JSONObject heWeather6 = jsonObject.getJSONArray("HeWeather6").getJSONObject(0);
        JSONArray daily_forecast = heWeather6.getJSONArray("daily_forecast");
        for (int i = 0; i < daily_forecast.length(); i++) {
            JSONObject forecastJsonObject = daily_forecast.getJSONObject(i);
            Forecast forecast = new Forecast();
            forecast.setDayIcon(forecastJsonObject.getInt("cond_code_d"));
            forecast.setMaxTemperature(forecastJsonObject.getInt("tmp_max"));
            forecast.setMinTemperature(forecastJsonObject.getInt("tmp_min"));
            forecasts.add(forecast);
        }
        if(save2sp){
            SPUtil spUtil = new SPUtil();
            spUtil.putString(WEATHER_FORCAST_JSON, response);
        }
        return forecasts;
    }

    /**
     * 更新天气数据
     */
    public static void updateWeatherData(){
        City city = WeatherSpHelper.getCityFromSp();
        if(city != null){
            //上次更新时间 操作4个小时 同时更新
            long lastRefresh = getRefreshTime();
            if( (System.currentTimeMillis() - lastRefresh) > 6*60*60*1000 ){
                updateAll();
            }
        } else {
            updateAll();
        }
    }

    private static void updateAll(){
        ThreadUtil.executeMore(new Runnable() {
            @Override
            public void run() {
                try {
                    String cityResponse = MyOkHttp.getInstance().postH().url(ZLauncherUrl.WEATHER_101).addParam("location", WeatherSpHelper.getLonAndLat()).executeStr();
                    City city = WeatherSpHelper.parseCity(cityResponse,false);
                    if(city != null) {
                        String conditionResponse = MyOkHttp.getInstance().postH().addParam("location", city.getWoeid()).url(ZLauncherUrl.WEATHER_102).executeStr();
                        Conditions conditions = WeatherSpHelper.parseConditions(conditionResponse, false);
                        String forecastResponse = MyOkHttp.getInstance().postH().addParam("location", city.getWoeid()).url(ZLauncherUrl.WEATHER_103).executeStr();
                        List<Forecast> forecastList = WeatherSpHelper.parseForecastList(forecastResponse, false);
                        if (conditions != null && forecastList != null) {
                            SPUtil spUtil = new SPUtil();
                            spUtil.putString(WeatherSpHelper.CITY_INFO_JSON, cityResponse);
                            spUtil.putString(WeatherSpHelper.WEATHER_CONDITION_JSON, conditionResponse);
                            spUtil.putString(WeatherSpHelper.WEATHER_FORCAST_JSON, forecastResponse);
                            spUtil.putLong(WeatherSpHelper.WEATHER_REFRESH_TIME, System.currentTimeMillis());
                            Intent intent = new Intent(WeatherView.ACTION_WEATHER_UPDATE_UI);
                            MyOkHttp.getInstance().getApplicationConext().sendBroadcast(intent);
                        }
                    }
                }catch (Exception e){

                }
            }
        });
    }

    public static void startLocation(Context mContext,boolean rightNow){
        if(mContext == null) return;
        long lastLoc = getLocTime();
        if(!rightNow && (System.currentTimeMillis() - lastLoc) < 6*60*60*1000){
            updateWeatherData();
            return;
        }
        new SPUtil().putLong(WeatherSpHelper.WEATHER_LOC_TIME, System.currentTimeMillis());
//        updateAll();
        LocationService.getInstance(mContext.getApplicationContext()).setCallback(new LocationService.BaiduLocCallback() {
            @Override
            public void onSuccess(BDLocation bdLocation) {
                final double latitude = bdLocation.getLatitude();    //获取纬度信息
                final double longitude = bdLocation.getLongitude();    //获取经度信息
                String city = bdLocation.getCity();
                if(!TextUtils.isEmpty(city)){
                    SPUtil spUtil = new SPUtil();
                    spUtil.putString(LON_AND_LAT,longitude+","+latitude);
                    updateAll();
                } else {
                    updateAll();
                }
            }

            @Override
            public void onFail() {
                updateAll();
            }
        });
        LocationService.getInstance(mContext.getApplicationContext()).start();
    }
}