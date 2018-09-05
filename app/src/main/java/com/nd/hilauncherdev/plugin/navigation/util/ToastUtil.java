package com.nd.hilauncherdev.plugin.navigation.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.tsy.sdk.myokhttp.MyOkHttp;

/**
 * Created by Administrator on 2018\9\5 0005.
 */

public class ToastUtil {

    private static Toast toast;


    public static void show(String content){
        if(TextUtils.isEmpty(content)){
            return;
        }
        showToast(content);
    }

    public static void show(int resId){
        Context context = MyOkHttp.getInstance().getApplicationConext();
        showToast(context.getString(resId));
    }

    private static void showToast(String content){
        if(toast == null){
            toast = new Toast(MyOkHttp.getInstance().getApplicationConext());
        }
        toast.setText(content);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
