package com.nd.hilauncherdev.plugin.navigation.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nd.hilauncherdev.plugin.navigation.R;

/**
 * Created by Administrator on 2018/9/4.
 */

public class GlideUtil {

    public static void load(Context context,
                            String url,
                            ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.theme_shop_v6_theme_no_find_small);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    public static void resumeRequests(Context context){
        Glide.with(context).resumeRequests();
    }

    public static void pauseRequests(Context context){
        Glide.with(context).pauseRequests();
    }

}
