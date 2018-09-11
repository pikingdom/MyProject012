package com.nd.hilauncherdev.plugin.navigation.helper;

import com.nd.hilauncherdev.plugin.navigation.util.SPUtil;
import com.nd.hilauncherdev.plugin.navigation.weather.model.City;
import com.nd.hilauncherdev.plugin.navigation.weather.model.Conditions;
import com.nd.hilauncherdev.plugin.navigation.weather.model.Forecast;
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

    public static String getLonAndLat() {
        SPUtil spUtil = new SPUtil();
        return spUtil.getString(LON_AND_LAT);
    }

    public static long getRefreshTime() {
        SPUtil spUtil = new SPUtil();
        return spUtil.getLong(WEATHER_REFRESH_TIME,System.currentTimeMillis());
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
}