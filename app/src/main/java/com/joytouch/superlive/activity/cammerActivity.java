package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.interfaces.TagSelectBack;
import com.joytouch.superlive.javabean.CammerSaishiBean;
import com.joytouch.superlive.utils.FileUploadUtil;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.TimeUtil;
import com.joytouch.superlive.utils.picturePress.ImageCompressUtil;
import com.joytouch.superlive.widget.CircleImageView;
import com.joytouch.superlive.widget.KeyboardListenRelativeLayout;
import com.joytouch.superlive.widget.LoadingDaliog;
import com.joytouch.superlive.widget.TagSelectDialog;
import com.joytouch.superlive.widget.TimeSelectDialog.PhotoInfalteDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;

/**
 * 开摄像头直播
 * Created by Administrator on 8/11 0011.
 */
public class cammerActivity extends BaseActivity implements View.OnClickListener,TagSelectBack {
    private final int SYS_INTENT_REQUEST = 0XFF01;
    private final int CAMERA_INTENT_REQUEST = 0XFF02;
    private TextView tv_shenqing;
    private ImageView iv_closeactivity;
    private LinearLayout ll_type;
    private Handler handler;
    private LinearLayout ll_starttime;
    private LinearLayout ll_endtime;
    private TextView tv_type;
    private SharedPreferences sp;
    private TextView tv_starttime;
    private TextView stop_time;
    int NowYear;
    int Month;
    int day;
    int hours;
    int minute;
    private EditText et_title;
    private EditText et_duiname1;
    private EditText et_duiname2;
    private CircleImageView iv_tubiao1;
    private CircleImageView iv_tubiao2;
    private KeyboardListenRelativeLayout root_my;
    private Bitmap bitmap;
    Map<String, Bitmap> imageMap = new HashMap<String, Bitmap>();
    private Map<String, String> basicMap = new HashMap<String, String>();
    private YujThread yujThread;
    private File destfile;
    private int key=0;
    private LoadingDaliog dialog;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cammer_layout);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        destfile = new File("/data/data/" + Preference.package_name + "/temp");
        if (!destfile.exists()) {
            destfile.mkdirs();
        }
        Calendar calNow = Calendar.getInstance();
        calNow.setTimeInMillis(System.currentTimeMillis());

        NowYear = calNow.get(Calendar.YEAR);
        Month = calNow.get(Calendar.MONTH);
        day = calNow.get(Calendar.DAY_OF_MONTH);
        calNow.setFirstDayOfWeek(Calendar.SUNDAY);
        hours = calNow.get(Calendar.HOUR_OF_DAY);
        minute = calNow.get(Calendar.MINUTE);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {

                }
            }
        };
        initView();
    }
    private boolean isHide;//true表示已经隐藏,false表示显示
    private void initView() {
        root_my = (KeyboardListenRelativeLayout) findViewById(R.id.root_my);
        root_my.setOnKeyboardStateChangedListener(new KeyboardListenRelativeLayout.IOnKeyboardStateChangedListener() {

            public void onKeyboardStateChanged(int state) {
                switch (state) {
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE:
                        isHide = true;
                        Log.e("执行", "1");
                        break;
                    case KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW:
                        isHide = false;
                        Log.e("执行", "2");
                        break;
                    default:
                        break;
                }
            }
        });
        iv_tubiao1 = (CircleImageView) findViewById(R.id.iv_tubiao1);
        iv_tubiao2 = (CircleImageView) findViewById(R.id.iv_tubiao2);

        et_duiname2 = (EditText) findViewById(R.id.et_duiname2);
        et_duiname1 = (EditText) findViewById(R.id.et_duiname1);
        et_title = (EditText) findViewById(R.id.et_title);
        tv_type = (TextView) findViewById(R.id.tv_type);
        ll_endtime = (LinearLayout) findViewById(R.id.ll_endtime);
        ll_starttime = (LinearLayout) findViewById(R.id.ll_starttime);
        tv_shenqing = (TextView) findViewById(R.id.tv_shenqing);
        iv_closeactivity = (ImageView) findViewById(R.id.iv_closeactivity);
        ll_type = (LinearLayout) findViewById(R.id.ll_type);
        tv_starttime = (TextView) findViewById(R.id.tv_starttime);
        stop_time = (TextView) findViewById(R.id.stop_time);
        ll_type.setOnClickListener(this);
        tv_shenqing.setOnClickListener(this);
        iv_closeactivity.setOnClickListener(this);
        ll_endtime.setOnClickListener(this);
        ll_starttime.setOnClickListener(this);
        iv_tubiao2.setOnClickListener(this);
        iv_tubiao1.setOnClickListener(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Long starttiem = System.currentTimeMillis();
        tv_starttime.setText(sdf.format(starttiem));

        stop_time.setText(sdf.format(starttiem + 10800 * 1000));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_tubiao2:
                key=2;
                ActionSheetDialogNoTitle();
                break;

            case R.id.iv_tubiao1:
                key=1;
                ActionSheetDialogNoTitle();
                break;

            case R.id.tv_shenqing:
                long time1 = TimeUtil.getDataLong(tv_starttime.getText().toString());
                long time2 = TimeUtil.getDataLong(stop_time.getText().toString());
                String title = et_title.getText().toString();
                String et_duiname_1 = et_duiname1.getText().toString();
                String et_duiname_2 = et_duiname2.getText().toString();
                String type=tv_type.getText().toString();

                if (title.equals("")) {
                    Toast.makeText(cammerActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (et_duiname_1.equals("") || et_duiname_2.equals("")) {
                    Toast.makeText(cammerActivity.this, "请输入队名", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (((time1+180) * 1000) < System.currentTimeMillis()) {
                    Toast.makeText(cammerActivity.this, "开始时间无效", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (time1 >= time2) {
                    Toast.makeText(cammerActivity.this, "结束时间不能比开始时间早", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (time2*1000<System.currentTimeMillis()){
                    Toast.makeText(cammerActivity.this, "结束时间不能少于当前时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.e("shijiankooko",((time1+180) * 1000)+"___"+System.currentTimeMillis());
                tv_shenqing.setSelected(true);
                tv_shenqing.setEnabled(false);
                shenqingzhibo(time1, time2, type, title, img_1, img_2, et_duiname_1, et_duiname_2);

                break;

            case R.id.iv_closeactivity:
                finish();
                break;

            case R.id.ll_type:
                TagSelectDialog dialog_tag = new TagSelectDialog(cammerActivity.this, R.layout.select_type);
                dialog_tag.show();
                break;

            case R.id.ll_starttime:
                PhotoInfalteDialog dialog = new PhotoInfalteDialog(cammerActivity.this, R.layout.inflater_service_schedule_select_time, handler, "ll_starttime", 1);
                dialog.show();
                break;

            case R.id.ll_endtime:
                PhotoInfalteDialog dialog1 = new PhotoInfalteDialog(cammerActivity.this, R.layout.inflater_service_schedule_select_time, handler, "ll_endtime", 2);
                dialog1.show();
                break;
        }
    }

    private void shenqingzhibo(long time1, long time2, String type, String title, String img_1, String img_2, String et_duiname_1, String et_duiname_2) {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("team1",et_duiname_1)
                .add("team2",et_duiname_2)
                .add("team1_logo",img_1)
                .add("team2_logo",img_2)
                .add("cat_type",type)
                .add("start_time",time1+"")
                .add("end_time",time2+"")
                .add("title", title)
                .build();
        Log.e("队伍",et_duiname_1+"__"+et_duiname_2);
        new HttpRequestUtils(this).httpPost(Preference.createlive, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        Toast.makeText(cammerActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String json) {
                        tv_shenqing.setEnabled(true);
                        Log.e("申请直播", json);
                        Gson gson = new Gson();
                        CammerSaishiBean bean = gson.fromJson(json, CammerSaishiBean.class);
                        if (bean.status.equals("_0000")) {
                            Intent intent = new Intent(cammerActivity.this, LiveStreamingActivity.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putSerializable("stream_info", bean);
                            intent.putExtras(mBundle);
                            startActivity(intent);
                        } else if (bean.status.equals("_1000")) {
                            new LoginUtils(cammerActivity.this).reLogin(cammerActivity.this);
                        } else {
                            Toast.makeText(cammerActivity.this, bean.message, Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                });
    }

    private void ActionSheetDialogNoTitle() {
        final String[] stringItems = {"拍照", "相册"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, root_my);
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    takePicture();
                } else if (position == 1) {
                    openAlbum();
                }
                dialog.dismiss();
            }
        });
    }

    private void openAlbum() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("image/*");
        startActivityForResult(intent, SYS_INTENT_REQUEST);
    }

    private void takePicture() {
        String sdStatus = Environment.getExternalStorageState();
        /* 检测sd是否可用 */
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "SD卡不可用！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, CAMERA_INTENT_REQUEST);
    }

    @Override
    public void selecttag(String text, int position) {
        if (position == 0) {
            tv_type.setText(text);
        } else if (position == 1) {
            tv_starttime.setText(text);
        } else if (position == 2) {
            stop_time.setText(text);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (cammerActivity.this.getCurrentFocus() != null) {
                if (cammerActivity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(cammerActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SYS_INTENT_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null,
                        null, null);
                if (null == cursor) {
                    Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();
                    return;
                }
                cursor.moveToFirst();
                String imageFilePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String outp = destfile + imageFilePath.substring(imageFilePath.lastIndexOf("."));
                File file = ImageCompressUtil.compressImage(imageFilePath, outp, ImageCompressUtil.Quality.QUALITY_80);
                if (file.exists()) {
                    bitmap = BitmapFactory.decodeFile(outp);
                }
                if (bitmap != null) {
//                    iv_tubiao1.setImageBitmap(bitmap);
                    if (key==1){
                        uploadImage(bitmap,iv_tubiao1);
                    }else if (key==2){
                        uploadImage(bitmap,iv_tubiao2);
                    }

                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == CAMERA_INTENT_REQUEST
                && resultCode == RESULT_OK && data != null) {
            cameraCamera(data);
        }
    }

    /**
     * @param data 拍照后获取照片
     */
    private void cameraCamera(Intent data) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String name = formatter.format(System.currentTimeMillis()) + ".jpg";
        Bundle bundle = data.getExtras();
        /* 获取相机返回的数据，并转换为Bitmap图片格式 */
        Bitmap bitmap = (Bitmap) bundle.get("data");
        FileOutputStream b = null;

        String path = Environment.getExternalStorageDirectory().getPath();
        File file = new File(path + "/myImage/");
        /** 检测文件夹是否存在，不存在则创建文件夹 **/
        if (!file.exists() && !file.isDirectory())
            file.mkdirs();
        String fileName = file.getPath() + "/" + name;
        Log.i("zhiwei.zhao", "camera file path:" + fileName);
        try {
            b = new FileOutputStream(fileName);
        /* 把数据写入文件 */
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (b == null)
                    return;
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (key==1){
            uploadImage(bitmap,iv_tubiao1);
        }else if (key==2){
            uploadImage(bitmap,iv_tubiao2);
        }
//        uploadImage(bitmap);
    }

    private void uploadImage(Bitmap bitmap,CircleImageView view) {
        dialog = new LoadingDaliog(cammerActivity.this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        imageMap.put("img", bitmap);
        basicMap.put("phone", "1");
        basicMap.put("version", Preference.version);
        yujThread = new YujThread(bitmap,view);
        yujThread.start();
    }
    private String img_1="";
    private String img_2="";
    private class YujThread extends Thread {
        private final Bitmap bitmap_ok;
        private final CircleImageView view;

        public YujThread(Bitmap bitmap, CircleImageView view) {
            this.bitmap_ok=bitmap;
            this.view=view;
        }

        @Override
        public void run() {
            super.run();
            String result = "";
            try {
                result = FileUploadUtil.uploadImage(Preference.loadlogo, basicMap, imageMap);
                Log.e("图片传", "执行" + result);
                final JSONObject json = new JSONObject(result);
                if (!json.isNull("status")) {
                    dialog.dismiss();
                    if (json.getString("status").equals("_1000")) {
                        new LoginUtils(cammerActivity.this).reLogin(cammerActivity.this);
                    } else if (json.getString("status").equals("_0000")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (key==1){
                                            img_1=json.optString("fid");
                                            view.setImageBitmap(bitmap_ok);
                                        }else if (key==2){
                                            img_2=json.optString("fid");
                                            view.setImageBitmap(bitmap_ok);
                                        }
                                        Log.e("返回图片地址",img_1+"__"+img_2);
                                        Toast.makeText(cammerActivity.this, "成功上传图片", Toast.LENGTH_LONG).show();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                destfile.delete();
                                            }
                                        }, 100);
                                    }
                                }, 1000);
                            }
                        });
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        
    }
}
