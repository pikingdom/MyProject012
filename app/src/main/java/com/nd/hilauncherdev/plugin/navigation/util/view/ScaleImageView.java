package com.nd.hilauncherdev.plugin.navigation.util.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nd.hilauncherdev.plugin.navigation.R;


public class ScaleImageView extends ImageView {

    private float mScale = 1.0f;

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ScaleImageView);
        mScale = array.getFloat(R.styleable.ScaleImageView_adjustRatio, mScale);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int w = getMeasuredWidth();
//        int h = (int) (w*mScale);
//        setMeasuredDimension(w, h);
        setMeasuredDimension((int) (getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec)),
                (int) (getDefaultSize(getSuggestedMinimumHeight(), widthMeasureSpec)*mScale));
    }
}
