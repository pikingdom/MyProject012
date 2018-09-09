package com.nd.hilauncherdev.plugin.navigation.base;

/**
 * Created by Administrator on 2018\9\1 0001.
 */

public interface BasePageInterface {


    /**
     * 开始加载数据  外部自己调用loaData 是否加载 由子view决定
     */
    public void onPageSelected();

    /**
     * 离开页面的时候回调
     */
    public void onPageUnSelected();

}
