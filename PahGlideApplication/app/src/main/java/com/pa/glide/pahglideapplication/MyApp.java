package com.pa.glide.pahglideapplication;

import android.app.Application;
import android.content.Context;

import com.pa.glide.pahglidemodule.util.GlideUtils;

/**
 * Created by zhangxiaowen on 2018/7/11.
 */

public class MyApp extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        GlideUtils.getInstance().initGlide(this);
    }
}
