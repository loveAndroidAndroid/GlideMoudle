package com.pa.glide.pahglidemodule.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.util.Util;
import com.pa.glide.pahglidemodule.util.Utils;

import java.security.MessageDigest;

/**
 * 画圆角
 */
public class RadiusTransformation extends BitmapTransformation {
    private final String TAG = getClass().getName();

    private int radius;

    public RadiusTransformation(Context context, int radius) {
        this.radius = Utils.dp2px(context, radius);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        //得到宽和高
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();

        Bitmap bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setHasAlpha(true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(toTransform, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        //画圆角
        canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius, paint);
        return bitmap;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RadiusTransformation) {
            RadiusTransformation other = (RadiusTransformation) obj;
            return radius == other.radius;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(TAG.hashCode(), Util.hashCode(radius));
    }

    /***
     * 通过内部算法 自定义图片缓存key
     * @param messageDigest
     */
    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((TAG + radius).getBytes(CHARSET));
    }

}
