package com.nd.hilauncherdev.plugin.navigation.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.nd.hilauncherdev.plugin.navigation.analytic.NavAnalytics;
import com.nd.hilauncherdev.plugin.navigation.analytic.NavUMConstant;
import com.nd.hilauncherdev.plugin.navigation.constant.SPConstant;
import com.nd.hilauncherdev.plugin.navigation.util.SPUtil;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.util.ScreenUtil;

/**
 * Created by Administrator on 2018\10\7 0007.
 */

public class NavHelper {

    public static final String NAVIGATION_PLUGIN_FILENAME = "com.nd.hilauncherdev.plugin.navigation.jar";
    public static final String NAVIGATION_PLUGIN_PKG = "com.nd.hilauncherdev.plugin.navigation";


    public static final long COMMON_ANALYTIC_INTERVEL = 24*60*60*1000;//24小时

    public static int getInstallPluginVersion() {
        String dataPath = Environment.getDataDirectory() + "/data/" + MyOkHttp.getInstance().getApplicationConext().getPackageName()+"/plugin/";
        String installPath = dataPath + NAVIGATION_PLUGIN_FILENAME;
        int installedVer = getRightVersionCode(installPath);
        return installedVer;
    }

    public static int getRightVersionCode(String installPath) {
        PackageManager pm = MyOkHttp.getInstance().getApplicationConext().getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(installPath, PackageManager.GET_ACTIVITIES);
        if (null == packageInfo) {
            return 0;
        }
        return packageInfo.versionCode;
    }
    /**
     * 启动一般统计
     */
    public static void startCommonAnalytic(){
        long lastCommonAnalytic = NavSpHelper.getLastCommonAnalytic();
        if(System.currentTimeMillis() - lastCommonAnalytic < COMMON_ANALYTIC_INTERVEL){
            return;
        }
        //开始统计
        int vc = getInstallPluginVersion();
        if(vc > 0){
            NavAnalytics.submitEvent(NavUMConstant.NAV_VERSION_CODE,vc+"");
        }
        //设置新的值
        SPUtil spUtil = new SPUtil();
        spUtil.putLong(SPConstant.LAST_COMMON_ANALYTIC,System.currentTimeMillis());
    }

    public static int getNavigationTopMargin(Context context){
        int height = ScreenUtil.getStatusBarHeight(context);
        if(ScreenUtil.hasVivoNotchInScreen(context)){
            height = 0;
        }
        return height;
    }
}
