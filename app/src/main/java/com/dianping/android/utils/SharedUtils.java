package com.dianping.android.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dianping.android.MyApplication;

public class SharedUtils {

    public static void setShareBoolean() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirst",false);
        editor.apply();
    }

    public static boolean getSharedBoolean() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        boolean b = preferences.getBoolean("isFirst",true);
        return b;
    }

    public static void setShareUserName(String userName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName",userName);
        editor.apply();
    }

    public static String getShareUserName() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        String userName = preferences.getString("userName","点击登录");
        return userName;
    }
}
