package com.joytouch.superlive.utils;

import android.graphics.Bitmap;

import com.joytouch.superlive.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by yj on 2016/5/3.
 * imageloader 的设置
 */
public class ImageLoaderOption {
    //开直播
    public static DisplayImageOptions optionduiwu = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.qizhi_bac)
            .showImageOnFail(R.drawable.qizhi_bac)
            .showImageForEmptyUri(R.drawable.qizhi_bac)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();
    //圆形头像的加载设置
    public static DisplayImageOptions optionsHeader = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.defulticon)
            .showImageOnFail(R.drawable.defulticon)
            .showImageForEmptyUri(R.drawable.defulticon)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();
    //头像非min
    public static DisplayImageOptions optionsHeaderno = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.defulticon)
            .showImageOnFail(R.drawable.head_empty)
            .showImageForEmptyUri(R.drawable.head_empty)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();
    //banner位的加载设置
    public static DisplayImageOptions optionsBaner = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.banner_empty)
                    .showImageOnFail(R.drawable.banner_empty)
                    .showImageForEmptyUri(R.drawable.banner_empty)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
    //球队的加载设置
    public static DisplayImageOptions optionsteamLogo = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.team_logo)
            .showImageOnFail(R.drawable.team_logo)
            .showImageForEmptyUri(R.drawable.team_logo)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();
    //球队的加载设置
    public static DisplayImageOptions optionsnewteamLogo = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.new_bg)
            .showImageOnFail(R.drawable.new_bg)
            .showImageForEmptyUri(R.drawable.new_bg)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();
    //回顾视屏
    public static DisplayImageOptions reviewLogo = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.vidio_empty)
            .showImageOnFail(R.drawable.vidio_empty)
            .showImageForEmptyUri(R.drawable.vidio_empty)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

}
