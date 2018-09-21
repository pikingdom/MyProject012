package com.example.testpermission;

import android.Manifest;
import android.content.Context;

import com.fastaccess.permission.base.PermissionHelper;

import java.util.List;

/**
 * Created by Administrator on 2018/9/21.
 */

public class MyAppHelper {

    private final static String[] MULTI_PERMISSIONS = new String[]{
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.BODY_SENSORS,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static boolean hasAllPermissionsAllow(Context context){
        List<String> permissions = PermissionHelper.declinedPermissionsAsList(context, MULTI_PERMISSIONS);
        if (permissions.isEmpty()) {
            return true;
        }
        boolean hasAlertWindowPermission = permissions.contains(Manifest.permission.SYSTEM_ALERT_WINDOW);
        if (hasAlertWindowPermission) {
            int index = permissions.indexOf(Manifest.permission.SYSTEM_ALERT_WINDOW);
            permissions.remove(index);
        }
        if (permissions.isEmpty()) {
            return true;
        }
        return false;
    }
}
