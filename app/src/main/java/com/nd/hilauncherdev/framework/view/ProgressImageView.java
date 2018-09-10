package com.nd.hilauncherdev.framework.view;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nd.hilauncherdev.plugin.navigation.R;

/**
 * Created by codeest on 16/9/27.
 */

public class ProgressImageView extends LinearLayout {

    private ImageView imageView;
    public ProgressImageView(Context context) {
        this(context,null);
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageView = (ImageView) findViewById(R.id.iv_progress);
    }

    public void start() {
        setVisibility(View.VISIBLE);
        Animatable animatable = (Animatable) imageView.getDrawable();
        if (!animatable.isRunning()) {
            animatable.start();
        }
    }

    public void stop() {
        Animatable animatable = (Animatable) imageView.getDrawable();
        if (animatable.isRunning()) {
            animatable.stop();
        }
        setVisibility(View.GONE);
    }
}
