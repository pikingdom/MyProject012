package com.nd.hilauncherdev.plugin.navigation.base;

import java.util.List;

/**
 * Created by Administrator on 2018\9\1 0001.
 */

public interface BaseListInterface<T> {

    /**
     * 网络数据失败
     * @param msg
     */
    public void onNetDataFail(String msg);

    /**
     * 网络数据成功
     * @param list  获取的列表数据
     * @param next  是否还有下一页数据
     */
    public void onNetDataSuccess(List<T> list, boolean next);


    /**
     * 开始加载数据 从当前pageindex 开始加载  外部调用一般先调用 hasLoad()
     */
    public void loadData();

    /**
     * 是否加载过数据  在 onNetDataSuccess 中第一页数据成功返回后设置为true
     * @return
     */
    public boolean hasLoad();

}
