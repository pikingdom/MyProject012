package com.nd.hilauncherdev.framework.view.baseDetail;

import java.util.List;

/**
 * Created by Administrator on 2018\9\1 0001.
 */

public interface BaseDetailInterface<T> {

    /**
     * 网络数据失败
     * @param msg
     */
    public void onNetDataFail(String msg);

    /**
     * 网络数据成功
     */
    public void onNetDataSuccess();


    /**
     * 开始加载数据
     */
    public void loadData();

    /**
     * 是否加载过数据
     * @return
     */
    public boolean hasLoad();

}
