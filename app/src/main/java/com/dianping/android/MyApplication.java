package com.dianping.android;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        mContext = getApplicationContext();
        LitePal.initialize(mContext);
    }

    public static Context getContext() {
        return mContext;
    }
}
