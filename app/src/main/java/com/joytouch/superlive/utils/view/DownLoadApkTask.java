package com.joytouch.superlive.utils.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.DownLoadUtils;

import java.io.File;

public class DownLoadApkTask extends AsyncTask<Void, Void, Boolean> {

	Activity context;
	String fileName;
	String downloadUrl;
	DownLoadUtils downLoadUtils;
	long total;
	long fileLength;
	Dialog dialog;
	TextView tv_title;
	ProgressBar pb;
	TextView tv_progress;
	private TextView tv_canle;

	public DownLoadApkTask(Activity context, String fileName, String downloadUrl) {
		this.context = context;
		this.fileName = fileName;
		this.downloadUrl = downloadUrl;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog = new Dialog(context, R.style.Dialog_bocop);
		dialog.setContentView(R.layout.progress_dialog);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		tv_canle=(TextView)dialog.findViewById(R.id.tv_canle);
		tv_title = (TextView) dialog.findViewById(R.id.tv_title);
		pb = (ProgressBar) dialog.findViewById(R.id.pb);
		tv_progress = (TextView) dialog.findViewById(R.id.tv_progress);

		tv_title.setText("版本更新中...");

		dialog.show();

		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				cancelTask();
			}
		});
//		tv_canle.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				cancelTask();
//			}
//		});

	}

	public void cancelTask() {
		cancel(true);
		Toast.makeText(context, "下载取消！", 1000).show();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 刷进度条

			if (downLoadUtils.getDownloadLength() != 0) {
				int persent = (int) (downLoadUtils.getDownloadLength() * 100 / downLoadUtils
						.getFileLength());
				pb.setProgress(persent);
				tv_progress.setText(persent + "%");
			}
			sendEmptyMessageDelayed(1, 500);
		};
	};

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		downLoadUtils = new DownLoadUtils();
		handler.sendEmptyMessage(1);
		return downLoadUtils.download(Preference.absDownloadDir, fileName,
				downloadUrl);

	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		handler.removeCallbacksAndMessages(null);
		dialog.dismiss();
		dialog = null;

		if (result) {
			Uri uri = Uri.fromFile(new File(Preference.absDownloadDir,
					fileName));
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(uri,
					"application/vnd.android.package-archive");
			context.startActivity(intent);
			context.finish();
		} else {
			Toast.makeText(context, "下载失败！", 1000).show();
		}
	}

}
