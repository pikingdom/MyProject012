package com.nd.hilauncherdev.plugin.navigation.helper;

/**
 * Created by Administrator on 2018\8\22 0022.
 */

public class ZLauncherUrl {

    public static final String HOST = "http://api.zlauncher.cn/";

    /**
     * 获取服务端当前日期时间(1)
     */
    public static final String COMMONACTION_1 = HOST+"action.ashx/commonaction/1";

    /**
     * 获取后台配置的参数信息(2)
     */
    public static final String COMMONACTION_2 = HOST+"action.ashx/commonaction/2";

    /**
     * 获取消息推送(3)
     */
    public static final String COMMONACTION_3 = HOST+"action.ashx/commonaction/3";

    /**
     * 获取天气城市(101)
     */
    public static final String WEATHER_101 = HOST+"action.ashx/weather/101";
    /**
     *获取实况天气(102)
     */
    public static final String WEATHER_102 = HOST+"action.ashx/weather/102";
    /**
     * 获取天气预报(103)
     */
    public static final String WEATHER_103 = HOST+"action.ashx/weather/103";

    /**
     * 获取下载地址
     */
    public static final String DOWNLOADURL = HOST+"soft/download.aspx?Identifier=%s";
}
