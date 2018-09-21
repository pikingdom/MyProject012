package com.example.testpermission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2018/9/21.
 */

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MyAppHelper.hasAllPermissionsAllow(this)){
            setContentView(R.layout.activity_mian);
        } else {
            Intent intent = new Intent(this,TestPermissionActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
