package com.tsy.sdk.myokhttp.util;

import android.content.Context;
import android.os.Build;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/9/30.
 */

public class ScreenUtil {

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        } return result;
    }

    /**
     * 华为手机是否有刘海
     * @param context
     * @return
     */
    public static boolean hasHuaweiNotchInScreen(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (Exception e) {

        } finally {
            return ret;
        }
    }

    public static boolean hasVivoNotchInScreen(Context context) {
        boolean ret = false;

        try {
            if(!TelephoneUtil.isVivoPhone() || Build.VERSION.SDK_INT < 26) {
                return ret;
            }

            ClassLoader cl = context.getClassLoader();
            Class FtFeature = cl.loadClass("android.util.FtFeature");
            Method get = FtFeature.getMethod("isFeatureSupport", new Class[]{Integer.TYPE});
            ret = ((Boolean)get.invoke(FtFeature, new Object[]{Integer.valueOf(32)})).booleanValue();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return ret;
    }

    public static boolean hasOppoNotchInScreen(Context context) {
        boolean ret = false;

        try {
            if(!TelephoneUtil.isOppoPhone() || Build.VERSION.SDK_INT < 26) {
                return ret;
            }

            ret = context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return ret;
    }


    public static boolean hasNotchInScreen(Context context) {
        return hasHuaweiNotchInScreen(context) || hasVivoNotchInScreen(context) || hasOppoNotchInScreen(context);
    }

}
