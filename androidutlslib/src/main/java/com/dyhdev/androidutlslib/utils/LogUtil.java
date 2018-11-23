package com.dyhdev.androidutlslib.utils;


import android.util.Log;

/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 *
 * @Date 2018/11/23  16:14
 * @Author DYH
 */
public class LogUtil{

    public static void v(String msg){
        Log.v("Log", msg);
    }
    public static void v(String tag, String msg){
        Log.v(tag, msg);
    }

    public static void d(String msg){
        Log.d("Log", msg);
    }
    public static void d(String tag, String msg){
        Log.d(tag, msg);
    }

    public static void i(String msg){
        Log.i("Log", msg);
    }
    public static void i(String tag, String msg){
        Log.i(tag, msg);
    }

    public static void e(String msg){
        Log.e("Log", msg);
    }
    public static void e(String tag, String msg){
        Log.e(tag, msg);
    }

    public static void w(String msg){
        Log.w("Log", msg);
    }
    public static void w(String tag, String msg){
        Log.w(tag, msg);
    }
}
