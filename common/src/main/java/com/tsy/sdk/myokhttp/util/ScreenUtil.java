package com.tsy.sdk.myokhttp.util;

import android.content.Context;

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

}
