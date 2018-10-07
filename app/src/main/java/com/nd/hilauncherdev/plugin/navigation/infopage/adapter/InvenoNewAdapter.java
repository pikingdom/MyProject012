package com.nd.hilauncherdev.plugin.navigation.infopage.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.nd.hilauncherdev.framework.common.util.GlideUtil;
import com.nd.hilauncherdev.framework.common.view.ScaleImageView;
import com.nd.hilauncherdev.framework.common.view.recyclerview.MultiItemTypeAdapter;
import com.nd.hilauncherdev.framework.common.view.recyclerview.base.ItemViewDelegate;
import com.nd.hilauncherdev.framework.common.view.recyclerview.base.ViewHolder;
import com.nd.hilauncherdev.plugin.navigation.R;
import com.nd.hilauncherdev.plugin.navigation.infopage.model.NewsInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/8/31.
 */

public class InvenoNewAdapter extends MultiItemTypeAdapter<NewsInfo> {


    public InvenoNewAdapter(Context context, List<NewsInfo> datas) {
        super(context, datas);
        addItemViewDelegate(new NewDelagate001());
        addItemViewDelegate(new NewDelagate002());
        addItemViewDelegate(new NewDelagate004());
        addItemViewDelegate(new NewDelagate008());
    }

    @Override
    protected boolean isEnabled(int viewType) {
        return super.isEnabled(viewType);
    }

    private String parsePublish(String publishTime){
        try {
            long current = System.currentTimeMillis()/1000;
            long publishL = Long.parseLong(publishTime);
            long result = current - publishL;
            int d_days = (int) (result/86400);
            int d_hours = d_days/3600;
            int d_minutes = d_hours/60;
            if(d_days>0) {
                return d_days+"天前";
            }else if(d_days<=0 && d_hours>0){
                return d_hours+"小时前";
            }else if(d_hours<=0 && d_minutes>3){
                return d_minutes+"分钟前";
            } else {
                return "刚刚";
            }
        }catch (Exception e){
            return "刚刚";
        }
    }

    public class NewDelagate001 implements ItemViewDelegate<NewsInfo> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.inveno_new_item_1;
        }

        @Override
        public boolean isForViewType(NewsInfo item, int position) {
            return NewsInfo.TYPE_1.equals(item.display) || ( !NewsInfo.TYPE_2.equals(item.display) &&
                    !NewsInfo.TYPE_4.equals(item.display) && !NewsInfo.TYPE_8.equals(item.display ) );
        }

        @Override
        public void convert(ViewHolder holder, NewsInfo newsInfo, int position) {
            holder.setText(R.id.summary, newsInfo.summary);
            holder.setText(R.id.source, newsInfo.source);
            holder.setText(R.id.publish_time,parsePublish(newsInfo.publish_time));
        }
    }

    public class NewDelagate002 implements ItemViewDelegate<NewsInfo> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.inveno_new_item_2;
        }

        @Override
        public boolean isForViewType(NewsInfo item, int position) {
            return NewsInfo.TYPE_2.equals(item.display);
        }

        @Override
        public void convert(ViewHolder holder, NewsInfo newsInfo, int position) {
            holder.setText(R.id.summary, newsInfo.summary);
            holder.setText(R.id.source, newsInfo.source);
            holder.setText(R.id.publish_time,parsePublish(newsInfo.publish_time));
            ScaleImageView imageView = (ScaleImageView)holder.getView(R.id.image);
            String url = newsInfo.getHeadUrl();
            if(!TextUtils.isEmpty(url)){
                GlideUtil.load(mContext,url,imageView);
            }
        }
    }

    public class NewDelagate004 implements ItemViewDelegate<NewsInfo> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.inveno_new_item_4;
        }

        @Override
        public boolean isForViewType(NewsInfo item, int position) {
            return NewsInfo.TYPE_4.equals(item.display);
        }

        @Override
        public void convert(ViewHolder holder, NewsInfo newsInfo, int position) {
            holder.setText(R.id.summary, newsInfo.summary);
            holder.setText(R.id.source, newsInfo.source);
            holder.setText(R.id.publish_time,parsePublish(newsInfo.publish_time));
            LinearLayout imageLL = holder.getView(R.id.list_images);
            for (int i = 0; i < imageLL.getChildCount(); i++) {
                String url = newsInfo.getUrl(i);
                ScaleImageView imageView = (ScaleImageView) imageLL.getChildAt(i);
                if(!TextUtils.isEmpty(url) && imageView !=null){
                    GlideUtil.load(mContext,url,imageView);
                }
            }
        }
    }

    public class NewDelagate008 implements ItemViewDelegate<NewsInfo> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.inveno_new_item_8;
        }

        @Override
        public boolean isForViewType(NewsInfo item, int position) {
            return NewsInfo.TYPE_8.equals(item.display);
        }

        @Override
        public void convert(ViewHolder holder, NewsInfo newsInfo, int position) {
            holder.setText(R.id.summary, newsInfo.summary);
            holder.setText(R.id.source, newsInfo.source);
            holder.setText(R.id.publish_time,parsePublish(newsInfo.publish_time));
            ScaleImageView imageView = (ScaleImageView)holder.getView(R.id.image);
            String url = newsInfo.getHeadUrl();
            if(!TextUtils.isEmpty(url)){
                GlideUtil.load(mContext,url,imageView);
            }
        }
    }
}
