package com.joytouch.superlive.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则匹配工具类
 * Created by fengdongfei on 2016/3/11.
 */
public class matchUtils {
//    private static String regEx = "[`~!@#%^&*()+=|{}(_)':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？《》-＿_～]";
    private static String regEx="[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}《》～＿_￣¯―【】‘；：”“’。，、？]";
    /**
     * 在某个字符串中查询某个字符或者某个子字串
     * @param string 需要匹配的字符串
     * @return
     */
    public static boolean matchone(String string) {
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(string);
        boolean rs = mat.find();
        return rs;
    }

    /**
     * 后台utf-8,一个汉字三个字节,一个字符二个字节
     * @param string
     * @return
     */
    public static int getBytes(String string) {
        byte[] buff= new byte[0];
        try {
            buff = string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int f=buff.length;
        return  f;
    }
}
