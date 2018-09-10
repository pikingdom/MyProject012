package com.nd.hilauncherdev.plugin.navigation.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2018/9/4.
 */

public class GlideUtil {

    public static void load(Context context,
                            String url,
                            ImageView imageView) {
        Glide.with(context)
                .load(url)
//                .placeholder(R.drawable.theme_shop_v6_theme_no_find_small)
                .into(imageView);
    }

    public static void load(Context context,
                            int resId,
                            ImageView imageView) {
        Glide.with(context)
                .load(resId)
//                .placeholder(R.drawable.theme_shop_v6_theme_no_find_small)
                .into(imageView);
    }

    public static void resumeRequests(Context context){
        Glide.with(context).resumeRequests();
    }

    public static void pauseRequests(Context context){
        Glide.with(context).pauseRequests();
    }

}
