package com.nd.hilauncherdev.plugin.navigation.infopage;

/**
 * Created by codeest on 2016/8/2.
 * View基类
 */
public interface BaseView {

    void stateError();

    boolean stateLoading();

    void stateMain();
}
