package com.joytouch.superlive.utils;

import android.util.Log;

/**
 * Created by yj on 2015/10/9.
 */
public class LogUtils {
    private static boolean isOpen = true;

    public static boolean isOpen() {
        return isOpen;
    }

    public  static void d(String label,String content){
        if(isOpen){
            Log.d(label,content);
        }
    }
    public  static void e(String label,String content){
        if(isOpen){
            Log.e(label, content);
        }
    }
    public  static void i(String label,String content){
        if(isOpen){
            Log.i(label, content);
        }
    }
    public  static void v(String label,String content){
        if(isOpen){
            Log.v(label, content);
        }
    }
    public  static void w(String label,String content){
        if(isOpen){
            Log.w(label,content);
        }
    }
}
