package com.joytouch.superlive.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.joytouch.superlive.activity.LotteryDetailsActivity;
import com.joytouch.superlive.activity.MainActivity;
import com.joytouch.superlive.activity.WebViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 极光的接收类
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	private boolean isAppRunning = false;
	private static final String MY_PKG_NAME = "com.joytouch.superlive";
	private SharedPreferences sp ;
	private String id;

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();

		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			id = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			// processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			//sp.edit().putString(String.valueOf(notifactionId),id).commit();

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			String userid = "";
			String anchorid = "";
			String roomid = "";
			String type = "";
			String url = "";
			try {
				//得到极光推过来通知的额外字段
				JSONObject object = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
				userid = object.optString("guessid");
				roomid = object.optString("roomid");
				anchorid = object.optString("anchorid");
				type = object.optString("type");
				url = object.optString("url");
				Log.e("type1111",url);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if("1".equals(type)){
				Intent itent3 = new Intent(context, WebViewActivity.class);
				itent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				itent3.putExtra("title","系统通知");
				itent3.putExtra("url",url);
				context.startActivity(itent3);
			}else
			if("2".equals(type)){
				//判断额外字段是否为空
				if(!TextUtils.isEmpty(userid)&&!TextUtils.isEmpty(roomid)&&!TextUtils.isEmpty(anchorid)){
					//不为空保存的本地在打开maintabactivity界面时用于打开竞猜题目详情用
					Intent itent1 = new Intent(context, LotteryDetailsActivity.class);
					itent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					itent1.putExtra("qid", userid);
					itent1.putExtra("roomid", roomid);
					itent1.putExtra("anchorid", anchorid);
					context.startActivity(itent1);
				}
			}else{
				Intent itent2 = new Intent(context, MainActivity.class);
				itent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(itent2);
			}



		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

}
