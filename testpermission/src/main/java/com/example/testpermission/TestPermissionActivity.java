package com.example.testpermission;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;

import java.util.Arrays;

/**
 * Created by Administrator on 2018\9\19 0019.
 */

public class TestPermissionActivity extends AppCompatActivity implements OnPermissionCallback {
    //https://blog.csdn.net/u011200604/article/details/52874599
//    private final static String[] MULTI_PERMISSIONS = new String[]{
//            Manifest.permission.GET_ACCOUNTS,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//    };
//    private final static String[] MULTI_PERMISSIONS = new String[]{
//            Manifest.permission.READ_CALENDAR,
//            Manifest.permission.READ_CONTACTS,
//            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.GET_ACCOUNTS,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//    };
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
    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionHelper = PermissionHelper.getInstance(this);
        permissionHelper
                .setForceAccepting(true) // default is false. its here so you know that it exists.
                .request( MULTI_PERMISSIONS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(reCheck) {
//            openSetting = false;
//            permissionHelper
//                    .setForceAccepting(true) // default is false. its here so you know that it exists.
//                    .request( MULTI_PERMISSIONS);
//            if (MyAppHelper.hasAllPermissionsAllow(this)) {
//                startMain();
//                finish();
//            }
            reCheck = false;
            permissionHelper
                    .setForceAccepting(true) // default is false. its here so you know that it exists.
                    .request( MULTI_PERMISSIONS);
        }
        Log.i("onPermissionGranted", "onResume");
    }

    private boolean reCheck = false;
    @Override
    protected void onPause() {
        super.onPause();
        if(openSetting){
            openSetting = false;
            reCheck = true;
        }
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        Log.i("onPermissionGranted", "Permission(s) " + Arrays.toString(permissionName) + " Granted");
//        setContentView(R.layout.activity_sample);
        startMain();
        finish();
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
        Log.i("onPermissionDeclined", "Permission(s) " + Arrays.toString(permissionName) + " Declined");
        finish();
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
        Log.i("onPermissionPreGranted", "Permission( " + permissionsName + " ) preGranted");
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        Log.i("NeedExplanation", "Permission( " + permissionName + " ) needs Explanation");
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        Log.i("ReallyDeclined", "Permission " + permissionName + " can only be granted from settingsScreen");
    }

    private boolean openSetting = false;
    @Override
    public void onPermissionReallyDeclined() {
        Log.i("ReallyDeclined", "11111111111111");
        //打开系统权限页面
        openSetting = true;
        PermissionHelper.openSettingsScreen(this);
    }

    @Override
    public void onNoPermissionNeeded() {
        Log.i("onNoPermissionNeeded", "Permission(s) not needed");
//        setContentView(R.layout.activity_sample);
        startMain();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        permissionHelper.onActivityForResult(requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startMain(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
