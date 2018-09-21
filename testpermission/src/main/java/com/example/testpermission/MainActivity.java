package com.example.testpermission;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Administrator on 2018/9/21.
 */

public class MainActivity extends AppCompatActivity{

    private boolean requsetPermission = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("zhenghonglin1","onCreate");
        if(MyAppHelper.hasAllPermissionsAllow(this)){
            Log.e("zhenghonglin1","onCreate11");
            requsetPermission = false;
            setContentView(R.layout.activity_mian);
        } else {
            Log.e("zhenghonglin1","onCreate22");
            requsetPermission = true;
            Log.e("zhenghonglin1","onCreate33");
            Intent intent = new Intent(this,TestPermissionActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(requsetPermission){
            return;
        }
        Log.e("zhenghonglin1","onResume:"+requsetPermission);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("zhenghonglin1","onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("zhenghonglin1","onDestroy");
    }
}
