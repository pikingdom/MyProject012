package com.nd.hilauncherdev.framework.common.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nd.hilauncherdev.common.jar.R;

/**
 * Created by Administrator on 2018/9/4.
 */

public class GlideUtil {

    public static void load(Context context,
                            String url,
                            ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.default_img_bg)
                .into(imageView);
    }

    public static void load(Context context,
                            int resId,
                            ImageView imageView) {
        Glide.with(context)
                .load(resId)
                .placeholder(R.drawable.default_img_bg)
                .into(imageView);
    }

    public static void resumeRequests(Context context){
        Glide.with(context).resumeRequests();
    }

    public static void pauseRequests(Context context){
        Glide.with(context).pauseRequests();
    }

}
