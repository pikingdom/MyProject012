package com.nd.hilauncherdev.plugin.navigation.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nd.hilauncherdev.plugin.navigation.R;


/**
 * Created by Administrator on 2018/8/29.
 */

public class NavigationSearchView extends BasePageView {

    private RelativeLayout search_rl;
    private ImageView search_img;
    private TextView search_tv;
    private RecyclerView recyclerView;
    public NavigationSearchView(Context context) {
        this(context,null);
    }

    public NavigationSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.navigation_search_page_view,this);
        findViews();
        initData();
    }

    private void findViews() {
        search_rl = (RelativeLayout) findViewById(R.id.search_rl);
        search_img = (ImageView) findViewById(R.id.search_img);
        search_tv = (TextView) findViewById(R.id.search_tv);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void initData() {

    }

    public void loadData(){

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
