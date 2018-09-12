package com.nd.hilauncherdev.plugin.weather.tools;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2018\9\11 0011.
 */

public class WeatherHelper {

    public static final String IOS_DIR = "weather_icon/";

    /**
     * return 根据下标返回白天天气图标
     * @param context
     * @return
     */
    public static Drawable getWeatherIcon(Context context, int weatherType) {
        String fileName = weatherType+".png";
        return getDrawableFromAssets(context, fileName);
    }

    public static Drawable getDrawableFromAssets(Context context, String fileName){
        return getDrawableFromAssetsFile(context, IOS_DIR + fileName);
    }

    /**
     * 从Assets中读取图片
     */
    public static Drawable getDrawableFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        Drawable drawable = null;
        AssetManager am = context.getResources().getAssets();
        InputStream is = null;
        try {
            is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            drawable = new BitmapDrawable(context.getResources(), image);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return drawable;
    }
}
