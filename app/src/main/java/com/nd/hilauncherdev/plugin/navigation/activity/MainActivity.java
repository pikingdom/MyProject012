package com.nd.hilauncherdev.plugin.navigation.activity;

import android.app.Activity;
import android.os.Bundle;

import com.nd.hilauncherdev.plugin.navigation.widget.NavigationView;

/**
 * Created by Administrator on 2018\8\28 0028.
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new NavigationView(getApplicationContext()));
    }
}
