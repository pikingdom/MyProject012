package com.nd.hilauncherdev.plugin.navigation.analytic;

import android.util.Log;

import com.nd.hilauncherdev.framework.common.util.reflect.Reflect;
import com.nd.hilauncherdev.plugin.navigation.helper.TagHelper;


/**
 * Created by Administrator on 2018\10\6 0006.
 */

public class NavAnalytics {

    private static final String UMAnalytics = "com.nd.hilauncherdev.kitset.umanalytic.UMAnalytics";

    public static void submitEvent(String eventId, String label) {
        try{
            Reflect.on(UMAnalytics).call("submitEvent", eventId,label);
        }catch (Exception e){

        }
    }

    public static void submitEvent(String eventId) {
        try{
            Reflect.on(UMAnalytics).call("submitEvent", eventId);
            Log.e(TagHelper.TAG,"submitEvent:"+eventId);
        }catch (Exception e){
            Log.e(TagHelper.TAG,"e:"+e.getMessage());
        }
    }
}
