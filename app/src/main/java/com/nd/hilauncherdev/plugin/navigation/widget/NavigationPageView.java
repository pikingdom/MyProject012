package com.nd.hilauncherdev.plugin.navigation.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2018/8/29.
 */

public class NavigationPageView extends BasePageView {

    public NavigationPageView(Context context) {
        this(context,null);
    }

    public NavigationPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.BLUE);
    }
}
