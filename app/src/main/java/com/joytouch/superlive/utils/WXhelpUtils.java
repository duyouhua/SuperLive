package com.joytouch.superlive.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.joytouch.superlive.app.Preference;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

/**
 * 微信登录辅助类
 */
public class WXhelpUtils {
	public static String myToken(Context context) {
		SharedPreferences sp=context.getSharedPreferences(Preference.preference, Context.MODE_PRIVATE);
	 if (sp.getString(Preference.token, "") != null&& !sp.getString(Preference.token, "").equals("")){
			return sp.getString(Preference.token, "");
		}

		return "";
	}

	public static boolean DEBUG = false;
	public static String wxMyCode = "";
	public static String wxToken = "";
	public static String wxOpenId = "";
	public static String wxUnionId = "";
	public static final String setPhotoOnOff = "setPhotoOnOff";
	public static final String setScoreOnOff = "setScoreOnOff";


	public static final String mainInfo = "mainInfo";

	public static final String CACHEPATH = Environment
			.getExternalStorageDirectory().toString() + "/SuperLive/cache/";
	

	public static void getHeadBitmap(String userName, String urlPath, View view) {
		Bitmap bitmap;
		ImageView imageView = null;
		if (view != null && (view instanceof ImageView)) {
			imageView = (ImageView) view;
		}
		File file = new File(CACHEPATH + MD5Util.md5(userName) + ".png");
		if (file.exists()) {

			bitmap = YujImageUtil.getBitmapByFile(file);
			if (bitmap != null) {
				imageView.setImageBitmap(bitmap);
			}

		} else {
			if (urlPath != null) {
				imageView.setTag(urlPath);
				new DownloadBitmap(imageView).execute();
			}
		}

	}

	public static class DownloadBitmap extends AsyncTask<Void, Void, Bitmap> {
		private ImageView v;
		private String pathString = "";

		public DownloadBitmap(View v) {
			pathString = (String) v.getTag();
			if (v instanceof ImageView) {
				this.v = (ImageView) v;
			}
		}

		public DownloadBitmap(String pathString) {
			this.pathString = pathString;
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			HttpGet get = new HttpGet(pathString);
			if(pathString.equals("")){
				return null;
			}
			try {
				HttpResponse response = new DefaultHttpClient().execute(get);
				if (response.getStatusLine().getStatusCode() == 200) {
					byte[] b = EntityUtils.toByteArray(response.getEntity());
					return BitmapFactory.decodeByteArray(b, 0, b.length);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				v.setImageBitmap(result);
			} else {

			}
		}
	}

	public void getHeadPhoto(final Context context, String path,
			ImageView photo, final View bg) {
		Bitmap headBitmap = BitmapFactory.decodeFile(Preference.cacheIamge
				+ path.hashCode());
		if (headBitmap == null) {

			new DownloadBitmap(path) {
				@Override
				protected void onPostExecute(Bitmap result) {
					if (result != null) {
						setHeadBackground(context, bg, result);
					}
				}
			}.execute();
		} else {
			setHeadBackground(context, bg, headBitmap);
		}
	}

	@SuppressWarnings("deprecation")
	public void setHeadBackground(Context context, View bg,
			Bitmap bitmap) {
		Bitmap bm = Bitmap.createBitmap(bitmap, 25, 25, 100, 100);
		bm = YujImageUtil.fastBlurBitmap(bm, 15, true);
		Drawable drawable = new BitmapDrawable(context.getResources(), bm);
		bg.setBackgroundDrawable(drawable);
	}

}
