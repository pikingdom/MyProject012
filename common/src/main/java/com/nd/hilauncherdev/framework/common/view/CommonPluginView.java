package com.nd.hilauncherdev.framework.common.view;

import android.content.Intent;
import android.view.KeyEvent;

/**
 * Created by Administrator on 2018/9/28.
 */

public interface CommonPluginView {

    void onResume();

    void onPause();

    void onBackPressed();

    void onStart();

    void onStop();

    boolean onKeyDown(int keyCode, KeyEvent event);

    void onNewIntent(Intent intent);

    void onDestroy();
}
