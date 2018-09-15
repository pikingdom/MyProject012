package com.nd.hilauncherdev.plugin.navigation.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.nd.hilauncherdev.plugin.navigation.widget.NavigationView;
import com.tsy.sdk.myokhttp.MyOkHttp;

/**
 * Created by Administrator on 2018\8\28 0028.
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyOkHttp.getInstance().setApplicationConext(getApplicationContext());
        NavigationView navigationView = new NavigationView(this);
        setContentView(navigationView);
        navigationView.setActivity(this);
        navigationView.onLauncherStart();
//        setContentView(R.layout.navigation_activity_main);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("zhenghonglin","activity onDestroy");
    }

}
