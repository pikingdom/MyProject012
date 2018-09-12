package com.nd.hilauncherdev.plugin.weather.model;

/**
 * Created by Administrator on 2018/9/11.
 */

public class Conditions {
    /**
     * 天气预报
     */
    private String weatherText ;

    /**
     * 天气图标
     */
    private int weatherIcon ;

    /**
     * 摄氏度温度
     */
    private float degrees;

    /**
     * 体感温度
     */
    private float realFeelDegrees;

    /**
     * 相对湿度
     */
    private float relativeHumidity ;

    /**
     * 风速 km/h
     */
    private float windSpeed ;



    /**
     * 风向
     */

    private String windDir ;

    /**
     * 可见度 km
     */
    private float visibility ;

    /**
     * 气压 mb
     */
    private float pressure ;

    public String getWeatherText() {
        return weatherText;
    }

    public void setWeatherText(String weatherText) {
        this.weatherText = weatherText;
    }

    public int getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(int weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public float getDegrees() {
        return degrees;
    }

    public void setDegrees(float degrees) {
        this.degrees = degrees;
    }

    public float getRealFeelDegrees() {
        return realFeelDegrees;
    }

    public void setRealFeelDegrees(float realFeelDegrees) {
        this.realFeelDegrees = realFeelDegrees;
    }

    public float getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(float relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public float getVisibility() {
        return visibility;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }
}
