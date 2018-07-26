package com.pa.glide.pahglideapplication;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.ImageView;

import com.pa.glide.pahglidemodule.progress.CircleProgressView;
import com.pa.glide.pahglidemodule.progress.OnProgressListener;
import com.pa.glide.pahglidemodule.util.GlideCacheUtil;
import com.pa.glide.pahglidemodule.util.GlideUtils;

public class MainActivity extends BaseActivity {

    ImageView image11;
    ImageView image12;
    ImageView image13;
    ImageView image14;

    ImageView image21;
    ImageView image22;
    ImageView image23;
    ImageView image24;

    ImageView image31;
    CircleProgressView progressView1;
    ImageView image41;
    CircleProgressView progressView2;

    ImageView image51;

    String url1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497688355699&di=ea69a930b82ce88561c635089995e124&imgtype=0&src=http%3A%2F%2Fcms-bucket.nosdn.127.net%2Ff84e566bcf654b3698363409fbd676ef20161119091503.jpg";
    String url2 = "http://img1.imgtn.bdimg.com/it/u=4027212837,1228313366&fm=23&gp=0.jpg";
    String url3 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1529402445474&di=b5da3b2f6a466e618e1e32d4dd2bda4d&imgtype=0&src=http%3A%2F%2F2b.zol-img.com.cn%2Fproduct%2F133_500x2000%2F801%2Fce21ke76FRh4A.jpg";

    String gif1 = "http://img.zcool.cn/community/01e97857c929630000012e7e3c2acf.gif";
    String gif2 = "http://5b0988e595225.cdn.sohucs.com/images/20171202/a1cc52d5522f48a8a2d6e7426b13f82b.gif";
    String gif3 = "http://img.zcool.cn/community/01d6dd554b93f0000001bf72b4f6ec.jpg";

    public static final String cat = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/resources/cat.jpg";
    public static final String cat_thumbnail = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/resources/cat_thumbnail.jpg";

    public static final String girl = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/resources/girl.jpg";
    public static final String girl_thumbnail = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/resources/girl_thumbnail.jpg";

    public static String TEST = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String[] strings = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE};

        String[] strings2 = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO};

        //实验
        if (SharedPreUtils.getInt(this,TEST,0) == 0){
            PermissionUtils.getInstance().gotoPermissionSetting(this);
        }else {
            PermissionUtils.getInstance().getSinglePermission(this,Manifest.permission.CALL_PHONE);
        }

//        PermissionUtils.getInstance().getMultiplePermission(this,strings);
//        PermissionUtils.getInstance().getMultiplePermissionForSingleResult(this,strings2);



        image11 = findViewById(R.id.image11);
        image12 = findViewById(R.id.image12);
        image13 = findViewById(R.id.image13);
        image14 = findViewById(R.id.image14);
        image21 = findViewById(R.id.image21);
        image22 = findViewById(R.id.image22);
        image23 = findViewById(R.id.image23);
        image24 = findViewById(R.id.image24);
        image31 = findViewById(R.id.image31);
        progressView1 = findViewById(R.id.progressView1);
        image41 = findViewById(R.id.image32);
        progressView2 = findViewById(R.id.progressView2);
        image51 = findViewById(R.id.image41);

        line1();
        line2();
        line3();
        line4();
        line5();
    }

    private void line1() {

        GlideUtils.getInstance().setImage(url1, image11, R.mipmap.image_loading);
        GlideUtils.getInstance().setImageCircle(url1, image12, R.mipmap.image_loading);
        GlideUtils.getInstance().setImage(url2, image13, R.mipmap.image_loading);
        GlideUtils.getInstance().setImageRound(url2, image14, R.mipmap.image_loading, true);
    }

    private void line2() {
        GlideUtils.getInstance().setImageRound(gif2, image21, R.mipmap.image_loading, true);
        GlideUtils.getInstance().setImage(gif1, image22, R.mipmap.image_loading);
        GlideUtils.getInstance().setImageCircle(gif3, image23, R.mipmap.image_loading);
    }

    private void line3() {

        image31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SingleImageActivity.class);
                intent.putExtra(SingleImageActivity.KEY_IMAGE_URL, girl);
                intent.putExtra(SingleImageActivity.KEY_IMAGE_URL_THUMBNAIL, girl_thumbnail);
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, image31, getString(R.string.transitional_image));
                ActivityCompat.startActivity(MainActivity.this, intent, compat.toBundle());

            }
        });

        GlideUtils.getInstance().setImage(girl, image31, R.color.placeholder, new OnProgressListener() {

            @Override
            public void onProgress(boolean isComplete, int percentage, long bytesRead, long totalBytes) {
                if (isComplete) {
                    progressView1.setVisibility(View.GONE);
                } else {
                    progressView1.setVisibility(View.VISIBLE);
                    progressView1.setProgress(percentage);
                }
            }
        });
    }

    /**
     * 缓存
     */
    private void line4() {
        image41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SingleImageActivity.class);
                intent.putExtra(SingleImageActivity.KEY_IMAGE_URL, cat);
                intent.putExtra(SingleImageActivity.KEY_IMAGE_URL_THUMBNAIL, cat_thumbnail);
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, image41, getString(R.string.transitional_image));
                ActivityCompat.startActivity(MainActivity.this, intent, compat.toBundle());

            }
        });

        GlideUtils.getInstance().setImage(cat, image41, R.color.placeholder, new OnProgressListener() {

            @Override
            public void onProgress(boolean isComplete, int percentage, long bytesRead, long totalBytes) {
                if (isComplete) {
                    progressView2.setVisibility(View.GONE);
                } else {
                    progressView2.setVisibility(View.VISIBLE);
                    progressView2.setProgress(percentage);
                }
            }
        });
    }

    /**
     * 模糊图
     */
    private void line5() {

        image51.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SingleImageActivity.class);
                intent.putExtra(SingleImageActivity.KEY_IMAGE_URL, url3);
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, image51, getString(R.string.transitional_image));
                ActivityCompat.startActivity(MainActivity.this, intent, compat.toBundle());

            }
        });

        GlideUtils.getInstance().setImage(url3, image51, R.color.placeholder);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //清除缓存的方法
        GlideCacheUtil.getInstance().clearImageAllCache(this);
    }
}
