package com.example.imagelrucacherdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


/**
 * Created by Richie on 2017/3/3.
 */

public class ImageAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    //上下文引用
    private Context context;

    //数据源
    private String[] imageThumbUrl;

    // GridView 控件
    private GridView gridView;

    //下载器
    private ImageDownLoader imageDownLoader;

    //是否是第一次进入
    private boolean isFirstEntry = true;

    //一屏中第一个item的数量
    private int mFirstVisibleItem;

    //一屏中所有item的数量
    private int mVisibleItemCount;


    public ImageAdapter(Context context, String[] imageThumbUrl, GridView gridView) {
        this.context = context;
        this.imageThumbUrl = imageThumbUrl;
        this.gridView = gridView;

        //在构造中创建下载器
        imageDownLoader = new ImageDownLoader(context);

        gridView.setOnScrollListener(this);

    }

    @Override
    public int getCount() {
        return imageThumbUrl.length;
    }

    @Override
    public Object getItem(int position) {
        return imageThumbUrl[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView ;
        final String mImageUrl = imageThumbUrl[position];
        if (convertView == null) {
            imageView = new ImageView(context);

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        //给image设置Tag
        imageView.setTag(mImageUrl);



        Bitmap bitmap = imageDownLoader.showCacheBitmap(mImageUrl.replaceAll("[^\\w]", ""));
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));
        }
        return imageView;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            showImage(mFirstVisibleItem, mVisibleItemCount);
        } else {
            cancelTask();
        }
    }

    /**
     * GridView滚动的时候调用的方法，刚开始显示GridView也会调用此方法
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        if (isFirstEntry && visibleItemCount > 0) {
            showImage(mFirstVisibleItem, mVisibleItemCount);
            isFirstEntry = false;
        }
    }


    /**
     * 显示当前屏幕的图片，先会去查找LruCache，LruCache没有就去sd卡或者手机目录查找，在没有就开启线程去下载
     *
     * @param firstVisibleItem
     * @param visibleItemCount
     */
    private void showImage(int firstVisibleItem, int visibleItemCount) {
        Bitmap bitmap = null;
        for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
            String mImageUrl = imageThumbUrl[i];
            final ImageView imageview = (ImageView) gridView.findViewWithTag(mImageUrl);
            bitmap = imageDownLoader.downloadImage(mImageUrl, new ImageDownLoader.OnImageLoaderListener() {
                @Override
                public void OnloaderImage(Bitmap bitmap, String url) {
                    if (bitmap != null && imageview != null) {
                        imageview.setImageBitmap(bitmap);
                    }

                }
            });
            if(bitmap != null){
              imageview.setImageBitmap(bitmap);
            }else{
              imageview.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_empty));
            }

        }
    }


    public void cancelTask() {
        if (imageDownLoader != null) {
            imageDownLoader.cancelTask();
        }
    }


}
