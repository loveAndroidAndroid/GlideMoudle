package com.pa.glide.pahglidemodule.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.pa.glide.pahglidemodule.progress.GlideApp;
import com.pa.glide.pahglidemodule.progress.GlideRequest;
import com.pa.glide.pahglidemodule.progress.OnProgressListener;
import com.pa.glide.pahglidemodule.progress.ProgressManager;
import com.pa.glide.pahglidemodule.transformation.CircleTransformation;
import com.pa.glide.pahglidemodule.transformation.RadiusTransformation;

/**
 * Created by hetao on 15/4/18.
 * Glide工具类
 */
public class GlideUtils {

    private static Context mContext;
    private volatile static GlideUtils inst;
    /**
     * 图片缓存路径
     */
    public static final String CACHE_PHOTO = "PA_Photo";
    private static GlideRequest<Drawable> glideRequest;

    private GlideUtils() {

    }

    public static GlideUtils getInstance() {
        if (inst == null) {
            synchronized (GlideUtils.class) {
                if (inst == null) {
                    inst = new GlideUtils();
                }
            }
        }
        return inst;
    }

    /**
     * 初始化Glide
     *
     * @param context
     */
    public void initGlide(Context context) {

        this.mContext = context;

        GlideBuilder builder = new GlideBuilder();

        //磁盘缓存配置（默认缓存大小250M，默认保存在内部存储中）
        //设置磁盘缓存保存在外部存储，且指定缓存大小
        int diskCacheSizeBytes = 1024 * 1024 * 50;
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, CACHE_PHOTO, diskCacheSizeBytes));

        //设置内存缓存
        int memorySize = (int) (Runtime.getRuntime().maxMemory()) / 8;
        builder.setMemoryCache(new LruResourceCache(memorySize));

        GlideApp.init(context, builder);
    }

    public static void setImage(String url, ImageView imageView, int holderId) {
        glideRequest = GlideApp.with(mContext).asDrawable();
        glideRequest.load(url);
        if (holderId != 0) {
            glideRequest = glideRequest.placeholder(holderId);
        }
        glideRequest.into(new GlideImageViewTarget(imageView, url));
    }

    public static void setImage(String url, ImageView imageView, int holderId,OnProgressListener onProgressListener) {
        glideRequest = GlideApp.with(mContext).asDrawable();
        ProgressManager.addListener(url, onProgressListener);
        glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().load(url);
        if (holderId != 0) {
            glideRequest = glideRequest.placeholder(holderId);
        }
        glideRequest.into(new GlideImageViewTarget(imageView, url));
    }

    public static void setImageRound(String url, ImageView imageView, int holderId, boolean roundCorner) {
        glideRequest = GlideApp.with(mContext).asDrawable();
        glideRequest.load(url);
        if (holderId != 0) {
            glideRequest = glideRequest.placeholder(holderId);
        }
        if (roundCorner) {
            glideRequest = glideRequest.transform(new RadiusTransformation(mContext, 10));
        }
        glideRequest.into(new GlideImageViewTarget(imageView, url));
    }

    public static void setImageCircle(String url, ImageView imageView, int holderId) {
        glideRequest = GlideApp.with(mContext).asDrawable();
        glideRequest.load(url);
        if (holderId != 0) {
            glideRequest = glideRequest.placeholder(holderId);
        }
        glideRequest = glideRequest.transform(new CircleTransformation());

        glideRequest.into(new GlideImageViewTarget(imageView, url));
    }

    /**
     * 自定义DrawableImageViewTarget 接受回调
     */
    private static class GlideImageViewTarget extends DrawableImageViewTarget {

        private String mUrl;

        GlideImageViewTarget(ImageView view, String url) {
            super(view);
            this.mUrl = url;
        }

        @Override
        public void onLoadStarted(Drawable placeholder) {
            super.onLoadStarted(placeholder);
        }

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {
            OnProgressListener onProgressListener = ProgressManager.getProgressListener(mUrl);
            if (onProgressListener != null) {
                onProgressListener.onProgress(true, 100, 0, 0);
                ProgressManager.removeListener(mUrl);
            }
            super.onLoadFailed(errorDrawable);
        }

        @Override
        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
            OnProgressListener onProgressListener = ProgressManager.getProgressListener(mUrl);
            if (onProgressListener != null) {
                onProgressListener.onProgress(true, 100, 0, 0);
                ProgressManager.removeListener(mUrl);
            }
            super.onResourceReady(resource, transition);
        }
    }

    /**
     * 给VIew设置背景
     */
    public void setBitmap(Activity activity, String url, View imageView) {
        GlideApp.with(activity).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                imageView.setBackground(new dr);
            }
        });
    }

    public static RequestOptions getRequestOptions() {
        RequestOptions options = new RequestOptions()
                .placeholder(android.R.color.holo_red_dark)    //加载成功之前占位图
                .error(android.R.color.holo_red_dark)    //加载错误之后的错误图
                .override(400, 400)    //指定图片的尺寸
                //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
                .fitCenter()
                //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
                .centerCrop()
                .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
                .format(DecodeFormat.DEFAULT)
                .skipMemoryCache(true)    //跳过内存缓存
                .transform(new CircleTransformation())//设置裁剪方式
                .diskCacheStrategy(DiskCacheStrategy.ALL)    //缓存所有版本的图像
                .diskCacheStrategy(DiskCacheStrategy.NONE)    //跳过磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.DATA)    //只缓存原来分辨率的图片
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);    //只缓存最终的图片

        return options;
    }


}
