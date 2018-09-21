package com.fastaccess.permission.base.callback;

import android.support.annotation.NonNull;

public interface OnPermissionCallback {

    void onPermissionGranted(@NonNull String[] permissionName);//用户允许权限了

    void onPermissionDeclined(@NonNull String[] permissionName);//用户拒绝权限了

    void onPermissionPreGranted(@NonNull String permissionsName); // 在请求权限之前已经授权过了 不知为什么已经允许了

    void onPermissionNeedExplanation(@NonNull String permissionName);//用户拒绝权限后 且没有打勾不再提示  单个权限中用

    void onPermissionReallyDeclined(@NonNull String permissionName);//当某个权限拒绝且不再提示的时候 调用 可被调用多次（多个权限多次）

    void onPermissionReallyDeclined();
    void onNoPermissionNeeded();//请求权限 23之前 直接返回这个方法
}
