package com.nd.hilauncherdev.plugin.navigation.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2018\9\8 0008.
 */

public class NavigationSitesGridLayoutManger extends GridLayoutManager {

    private int heightOffset;
    public NavigationSitesGridLayoutManger(Context context, int spanCount, int heightOffset) {
        super(context, spanCount);
        this.heightOffset = heightOffset;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        final int measuredWidth = View.MeasureSpec.getSize(widthSpec);
        int measuredHeight = 0;
        int count = getItemCount();
        if(count >0 ){
            int span = getSpanCount();
            View view = recycler.getViewForPosition(0);
            if(view != null){
                RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
                int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, getPaddingLeft() + getPaddingRight(), p.width);
                int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, getPaddingTop() + getPaddingBottom(), p.height);
                view.measure(childWidthSpec, childHeightSpec);
                int itemHeight = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
                int rows = count/span;
                if(count%span !=0){
                    rows=rows+1;
                }
                measuredHeight = (itemHeight+heightOffset)*rows;
                recycler.recycleView(view);
                setMeasuredDimension(measuredWidth,measuredHeight);
            } else {
                super.onMeasure(recycler, state, widthSpec, heightSpec);
            }
        } else {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        }
    }
    private int[] mMeasuredDimension = new int[2];
//    @Override
//    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
//        final int widthMode = View.MeasureSpec.getMode(widthSpec);
//        final int heightMode = View.MeasureSpec.getMode(heightSpec);
//        final int widthSize = View.MeasureSpec.getSize(widthSpec);
//        final int heightSize = View.MeasureSpec.getSize(heightSpec);
//        int width = 0;
//        int height = 0;
//        int count = getItemCount();
//        int span = getSpanCount();
//        for (int i = 0; i < count; i++) {
//            measureScrapChild(recycler, i,
//                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
//                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
//                    mMeasuredDimension);
//            if (getOrientation() == HORIZONTAL) {
//                if (i % span == 0) {
//                    width = width + mMeasuredDimension[0];
//                }
//                if (i == 0) {
//                    height = mMeasuredDimension[1];
//                }
//            } else {
//                if (i % span == 0) {
//                    height = height + mMeasuredDimension[1];
//                }
//                if (i == 0) {
//                    width = mMeasuredDimension[0];
//                }
//            }
//        }
//        setMeasuredDimension(width, height);
//    }

}
