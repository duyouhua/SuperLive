package com.joytouch.superlive.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 获取网络图片展示
 * 使用方式:
 * ImageUtils.imageLoader.displayImage(URL0, viewholder.iv_imgvie1,ImageUtils.v3_Options);
 *
 */
public class ImageUtils {
/*
* DisplayImageOptions使用ImageLoader进行图片加载时的设置
*ImageUtils.imageLoader.displayImage(URL0, viewholder.iv_imgvie1,
                                    ImageUtils.v3_Options);
* */
	public static ImageLoader imageLoader = ImageLoader.getInstance();
	public static DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.logo)
			.showImageForEmptyUri(R.drawable.logo)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).cacheInMemory()
			.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
	public static DisplayImageOptions reward = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.logo)
			.showImageForEmptyUri(R.drawable.logo)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).cacheInMemory()
			.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
	public static DisplayImageOptions head = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.logo)
			.showImageForEmptyUri(R.drawable.logo)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).cacheInMemory()
			.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
	/**
	 * banner
	 */
	public static DisplayImageOptions options_banner = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.logo)
			.showImageForEmptyUri(R.drawable.logo)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).cacheInMemory()
			.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();

	public static DisplayImageOptions v3_headPhoto_options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.logo).cacheInMemory()
			.showImageForEmptyUri(R.drawable.logo).cacheOnDisc()
			.build();
	public static DisplayImageOptions v3_Options = new DisplayImageOptions.Builder()
			.cacheInMemory().cacheOnDisc()
			.bitmapConfig(Bitmap.Config.RGB_565)
			.showStubImage(R.drawable.logo)
			.delayBeforeLoading(0)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

	public static DisplayImageOptions gridViewOptions = new DisplayImageOptions.Builder()
			.cacheInMemory().bitmapConfig(Bitmap.Config.RGB_565)
			.showStubImage(R.drawable.logo).build();
	public static DisplayImageOptions enlarge_photo_options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.logo)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).cacheInMemory()
			.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();;

	public static boolean isAbleToLoad(Context context) {

		SharedPreferences sp = context.getSharedPreferences(
				Preference.preference, Context.MODE_PRIVATE);

		return sp.getBoolean(Preference.load2G3G, true)
				|| NetworkUtils.isWifiConnected(context);
	}
}
