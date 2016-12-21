package com.joytouch.superlive.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.view.WindowManager;

import com.cjzhibo.im.ImApi;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.utils.TimeUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.pili.pldroid.streaming.StreamingEnv;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.xutils.x;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

//import com.cjzhibo.im.ImApi;

/**
 * Created by yj on 2016/4/6.
 */
public class SuperLiveApplication extends Application {
	public static Context mContext;
	private static final String TAG = "Spartans";

	public static QQAuth mQQAuth;
	public static IWXAPI wxapi;
	public static ImApi imApi;
	// 浮动窗口全局变量，用以保存悬浮窗口的属性
	private static WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
	private SharedPreferences sp;
	public static WindowManager.LayoutParams getMywmParams() {
		return wmParams;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		StreamingEnv.init(getApplicationContext());
		sp = this.getSharedPreferences(Preference.preference,
				Context.MODE_PRIVATE);
		x.Ext.init(this);//Xutils初始化
		// 设置是否输出debug·
		x.Ext.setDebug(true);
		MobclickAgent.setDebugMode(true);//开启友盟debug模式
		mContext = this.getApplicationContext();
		mQQAuth = QQAuth.createInstance(Preference.qq_appid, mContext);
		registWx();
		initImageLoader(getApplicationContext());
		imApi = new ImApi();
		String nicname = (String) SPUtils.get(mContext, Preference.nickname,"",Preference.preference);
		String token = (String) SPUtils.get(mContext, Preference.token,"",Preference.preference);
		String userid=sp.getString(Preference.myuser_id,"");
		//设置帐户(用户名密码)
		LogUtils.e("xxxxxxxxxxxxxxxxxx", "nicname = " + nicname + "token = " + token + " userid = " + userid);
		imApi.start(userid, nicname, sp.getString(Preference.token, ""));
		if(!TimeUtil.currentLocalDateString().equals(SPUtils.get(mContext,"time","",Preference.matchroom))){
			SharedPreferences sp1 = this.getSharedPreferences(Preference.matchroom,Context.MODE_PRIVATE);
			sp1.edit().clear().commit();
		}
		SPUtils.put(mContext, "time", TimeUtil.currentLocalDateString(), Preference.matchroom);
		JPushInterface.init(getApplicationContext());
		JPushInterface.setDebugMode(true);
		LogUtils.e("xxxxxxxxxxxxxxxxxx", "nicname = "+JPushInterface.getRegistrationID(getApplicationContext()));

	}
	public void registWx() {
		wxapi = WXAPIFactory.createWXAPI(this, Preference.wx_appid, true);
		wxapi.registerApp(Preference.wx_appid);
	}

	public String getSource(Context context) {
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			return ai.metaData.getString("UMENG_CHANNEL");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	public static void initImageLoader(Context context) {

		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				Preference.cacheDir);
		ImageLoader imageLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.memoryCacheExtraOptions(480, 320)
						// max width, max height
				.diskCacheExtraOptions(480, 320, new BitmapProcessor() {
					@Override
					public Bitmap process(Bitmap bitmap) {
						return null;
					}
				})
						// Can slow ImageLoader, use it carefully (Better don't use it)
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
						// 你可以通过你自己的内存缓存实现
				.diskCache(new UnlimitedDiskCache(cacheDir))
						// 你可以通过你自己的盘缓存实现
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.imageDownloader(
						new BaseImageDownloader(context,5 * 1000, 20 * 1000))
						// connectTimeout (5 s), readTimeout (20 s)
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.build();
		imageLoader.init(config);
	}

}
