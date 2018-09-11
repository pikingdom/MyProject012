package com.nd.hilauncherdev.plugin.navigation.weather.model;

/**
 * Created by Administrator on 2018/9/11.
 */

public class Forecast {
    /**
     * 白天的天气图标
     */
    private int dayIcon ;

    /**
     * 最高温度
     */
    private int maxTemperature;
    /**
     * 最低温度
     */
    private int minTemperature;

    public int getDayIcon() {
        return dayIcon;
    }

    public void setDayIcon(int dayIcon) {
        this.dayIcon = dayIcon;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(int maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(int minTemperature) {
        this.minTemperature = minTemperature;
    }
}
