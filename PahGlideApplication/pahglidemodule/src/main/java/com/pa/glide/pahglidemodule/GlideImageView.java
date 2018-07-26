package com.pa.glide.pahglidemodule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.pa.glide.pahglidemodule.progress.GlideApp;
import com.pa.glide.pahglidemodule.progress.OnProgressListener;
import com.pa.glide.pahglidemodule.transformation.CircleTransformation;
import com.pa.glide.pahglidemodule.transformation.RadiusTransformation;

/**
 * 自定义GlideImageView 对外暴力设置的方法
 */
@SuppressLint({"CheckResult", "AppCompatCustomView"})
public class GlideImageView extends ImageView {

    private boolean enableState = false;
    private float pressedAlpha = 0.4f;
    private float unableAlpha = 0.3f;
    private GlideImageLoader imageLoader;

    public GlideImageView(Context context) {
        this(context, null);
    }

    public GlideImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlideImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        imageLoader = GlideImageLoader.create(this);
    }

    public GlideImageLoader getImageLoader() {
        if (imageLoader == null) {
            imageLoader = GlideImageLoader.create(this);
        }
        return imageLoader;
    }

    /**
     * 给VIew设置背景
     */
    public void setBitmap(String url) {
        GlideApp.with(getContext()).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                setImageBitmap(resource);
            }
        });
    }

    /**
     * 设置RequestOptions参数
     *
     * @param options
     * @return
     */
    public GlideImageView apply(RequestOptions options) {
        getImageLoader().getGlideRequest().apply(options);
        return this;
    }

    /**
     * 设置图片裁剪格式centerCrop
     */
    public GlideImageView centerCrop() {
        getImageLoader().getGlideRequest().centerCrop();
        return this;
    }

    /**
     * 设置图片裁剪格式fitCenter
     */
    public GlideImageView fitCenter() {
        getImageLoader().getGlideRequest().fitCenter();
        return this;
    }

    /**
     * 设置缓存模式
     */
    public GlideImageView diskCacheStrategy(@NonNull DiskCacheStrategy strategy) {
        getImageLoader().getGlideRequest().diskCacheStrategy(strategy);
        return this;
    }

    /**
     * 设置空占位图
     */
    public GlideImageView placeholder(@DrawableRes int resId) {
        getImageLoader().getGlideRequest().placeholder(resId);
        return this;
    }

    /**
     * 设置错误占位图
     */
    public GlideImageView error(@DrawableRes int resId) {
        getImageLoader().getGlideRequest().error(resId);
        return this;
    }

    public GlideImageView fallback(@DrawableRes int resId) {
        getImageLoader().getGlideRequest().fallback(resId);
        return this;
    }

    public GlideImageView dontAnimate() {
        getImageLoader().getGlideRequest().dontTransform();
        return this;
    }

    public GlideImageView dontTransform() {
        getImageLoader().getGlideRequest().dontTransform();
        return this;
    }

    /**
     * 设置圆角
     */
    public void load(String url) {
        load(url, 0);
    }

    public void load(String url, @DrawableRes int placeholder) {
        load(url, placeholder, 0);
    }

    public void load(String url, @DrawableRes int placeholder, int radius) {
        load(url, placeholder, radius, null);
    }

    /**
     * 设置回调
     */
    public void load(String url, @DrawableRes int placeholder, OnProgressListener onProgressListener) {
        load(url, placeholder, null, onProgressListener);
    }

    public void load(String url, @DrawableRes int placeholder, int radius, OnProgressListener onProgressListener) {
        load(url, placeholder, new RadiusTransformation(getContext(), radius), onProgressListener);
    }

    public void load(Object obj, @DrawableRes int placeholder, Transformation<Bitmap> transformation) {
        getImageLoader().loadImage(obj, placeholder, transformation);
    }


    /**
     * 设置圆
     */
    public void loadCircle(String url) {
        loadCircle(url, 0);
    }

    public void loadCircle(String url, @DrawableRes int placeholder) {
        loadCircle(url, placeholder, null);
    }

    public void loadCircle(String url, @DrawableRes int placeholder, OnProgressListener onProgressListener) {
        load(url, placeholder, new CircleTransformation(), onProgressListener);
    }

    public void load(Object obj, @DrawableRes int placeholder, Transformation<Bitmap> transformation, OnProgressListener onProgressListener) {
        getImageLoader().listener(obj, onProgressListener).loadImage(obj, placeholder, transformation);
    }

    /**
     * 设置drawable
     */
    public void loadDrawable(@DrawableRes int resId) {
        loadDrawable(resId, 0);
    }

    public void loadDrawable(@DrawableRes int resId, @DrawableRes int placeholder) {
        loadDrawable(resId, placeholder, null);
    }

    public void loadDrawable(@DrawableRes int resId, @DrawableRes int placeholder, @NonNull Transformation<Bitmap> transformation) {
        getImageLoader().load(resId, placeholder, transformation);
    }

    /**
     * 设置透明度
     */
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (enableState) {
            if (isPressed()) {
                setAlpha(pressedAlpha);
            } else if (!isEnabled()) {
                setAlpha(unableAlpha);
            } else {
                setAlpha(1.0f);
            }
        }
    }

    public GlideImageView enableState(boolean enableState) {
        this.enableState = enableState;
        return this;
    }

    public GlideImageView pressedAlpha(float pressedAlpha) {
        this.pressedAlpha = pressedAlpha;
        return this;
    }

    public GlideImageView unableAlpha(float unableAlpha) {
        this.unableAlpha = unableAlpha;
        return this;
    }
}
