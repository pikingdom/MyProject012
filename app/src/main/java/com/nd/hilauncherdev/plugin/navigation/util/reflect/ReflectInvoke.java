package com.nd.hilauncherdev.plugin.navigation.util.reflect;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.nd.hilauncherdev.kitset.util.reflect.NavigationKeepForReflect;


/**
 * 需要兼容旧版本以及定制版本
 * Created by linliangbin on 2017/3/17 15:28.
 */

public class ReflectInvoke {
    public static void setNavigationViewChildCount(Context launcher, int index) {
        try {
            NavigationKeepForReflect.setNavigationViewChildCount_V8198(launcher, index);
        } catch (Throwable t) {
            Reflect.on(launcher).call("setNavigationViewChildCount", index);
        }
    }

    public static void setNavigationViewChildIndex(Context launcher, int index) {
        try {
            NavigationKeepForReflect.setNavigationViewChildIndex_V8198(launcher, index);
        } catch (Throwable t) {
            Reflect.on(launcher).call("setNavigationViewChildIndex", index);
        }
    }
}
