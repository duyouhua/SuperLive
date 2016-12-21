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
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.FileUploadUtil;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.picturePress.ImageCompressUtil;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户个人的简单信息
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener{
    private final int SYS_INTENT_REQUEST = 0XFF01;
    private final int CAMERA_INTENT_REQUEST = 0XFF02;
    private ImageView iv_finish;
    private TextView tv_title;
    private RelativeLayout rl_icon,rl_nick,rl_sign,rl_pass;
    private LinearLayout root_my;
    private Bitmap bitmap;
    private CircleImageView iv_icon;
    private RelativeLayout rl_level;
    private SharedPreferences sp;
    private List<ImageView> imglist;//img控件存储
    Map<String, Bitmap> imageMap = new HashMap<String, Bitmap>();
    private Map<String, String> basicMap = new HashMap<String, String>();
    private YujThread yujThread;
    private String imgid;
    private TextView tv_nickname;
    private TextView tv_sign;
    private TextView anchor_fans;
    private LinearLayout ll_level;
    private String imgurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        initView();
        destfile = new File("/data/data/" + Preference.package_name + "/temp");
        if (!destfile.exists()) {
            destfile.mkdirs();
        }
        //创建集合存储图片对象
    }

    private void initView() {
        ll_level=(LinearLayout)findViewById(R.id.ll_level);
        anchor_fans=(TextView)this.findViewById(R.id.anchor_fans);
        if (sp.getString(Preference.level,"").equals("")){
            ll_level.setVisibility(View.GONE);
        }
//        anchor_fans.setText(sp.getString(Preference.level,""));
        ConfigUtils.level(anchor_fans,sp.getString(Preference.level,""));;
        tv_sign=(TextView)this.findViewById(R.id.tv_sign);
        tv_sign.setText(sp.getString(Preference.sign,""));
        tv_nickname=(TextView)this.findViewById(R.id.tv_nickname);
        tv_nickname.setText(sp.getString(Preference.nickname,""));
        rl_level=(RelativeLayout)this.findViewById(R.id.rl_level);
        rl_level.setOnClickListener(this);
        iv_icon=(CircleImageView)this.findViewById(R.id.iv_icon);
        root_my=(LinearLayout)this.findViewById(R.id.root_my);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("个人信息");
        rl_icon = (RelativeLayout) this.findViewById(R.id.rl_icon);
        rl_icon.setOnClickListener(this);
        rl_nick = (RelativeLayout) this.findViewById(R.id.rl_nick);
        rl_nick.setOnClickListener(this);
        rl_sign = (RelativeLayout) this.findViewById(R.id.rl_sign);
        rl_sign.setOnClickListener(this);
        rl_pass = (RelativeLayout) this.findViewById(R.id.rl_pass);
        rl_pass.setOnClickListener(this);
        //设置默认头像
        imgid = sp.getString(Preference.headPhoto,"");
        if (imgid.equals("")){
            imgid =Preference.baseheadphoto;
        }
        imgurl = Preference.img_url+"200x200/"+imgid;
        Log.e("initView",imgurl);
        ImageLoader.getInstance().displayImage(imgurl,
                iv_icon, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume", imgurl);
        if (sp.getString(Preference.level,"").equals("")){
            ll_level.setVisibility(View.GONE);
        }else{
            ll_level.setVisibility(View.VISIBLE);
        }
//        anchor_fans.setText(sp.getString(Preference.level,""));
        tv_sign.setText(sp.getString(Preference.sign, ""));
        tv_nickname.setText(sp.getString(Preference.nickname, ""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.rl_icon:
                ActionSheetDialogNoTitle();
                //头像
                break;
            case R.id.rl_nick:
                Intent intent=new Intent(this, ResetNameActivity.class);
                intent.putExtra("nick_id","");
                intent.putExtra("origin","0");
                startActivity(intent);
                break;
            case R.id.rl_sign:
                toActivity(InputSignActivity.class);
                break;
            case R.id.rl_pass:
                toActivity(ResetPasswordActivity.class);
                break;
            case R.id.rl_level:
                toActivity(LevelActivity.class);
                break;
        }
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
    private File destfile;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SYS_INTENT_REQUEST && resultCode == RESULT_OK&& data != null) {
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
                //图片压缩路径,安卓4.4SD卡不允许手机内存个sd卡同时都有的情况下,sd卡的数据不允许修改
                String outp = destfile + imageFilePath.substring(imageFilePath.lastIndexOf("."));
                String outputFilePath = imageFilePath.substring(0, imageFilePath.lastIndexOf(".")) + "_comp_tou" + imageFilePath.substring(imageFilePath.lastIndexOf("."));
                File file = ImageCompressUtil.compressImage(imageFilePath, outp, ImageCompressUtil.Quality.QUALITY_80);
                if (file.exists()) {
                    bitmap = BitmapFactory.decodeFile(outp);
                }
                //展示图片
//                Update(imageFilePath);
                if (bitmap != null) {
                    iv_icon.setImageBitmap(bitmap);
                    uploadImage(bitmap);
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

    private void uploadImage(Bitmap bitmap) {
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        imageMap.put("img", bitmap);
        basicMap.put("phone", "1");
        basicMap.put("token", sp.getString(Preference.token,""));
        basicMap.put("version", Preference.version);
        yujThread = new YujThread();
        yujThread.start();
    }
    private class YujThread extends Thread {
        @Override
        public void run() {
            super.run();
            String result = "";
            try {
                result = FileUploadUtil.uploadImage(Preference.img_headphoto, basicMap, imageMap);
                Log.e("图片传","执行"+result);
                JSONObject json = new JSONObject(result);
                if (!json.isNull("status")) {
                    if (json.getString("status").equals("_1000")) {
                       new LoginUtils(UserInfoActivity.this).reLogin(UserInfoActivity.this);
                    }else if (json.getString("status").equals("_0000")){
                        imgurl = Preference.img_url+"200x200/"+json.getString("status");
                        sp.edit().putString(Preference.headPhoto,json.getString("img_id")).commit();
                       runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ImageLoader.getInstance().displayImage(imgurl,
                                                iv_icon, new ImageLoadingListener() {
                                                    @Override
                                                    public void onLoadingStarted(String s, View view) {

                                                    }

                                                    @Override
                                                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                                                    }

                                                    @Override
                                                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                                                    }

                                                    @Override
                                                    public void onLoadingCancelled(String s, View view) {

                                                    }
                                                });
                                        Toast.makeText(UserInfoActivity.this, "成功修改头像", Toast.LENGTH_LONG).show();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                destfile.delete();
                                            }
                                        },500);
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
        iv_icon.setImageBitmap(bitmap);
        //上传头像
//        Update(path);
        uploadImage(bitmap);
    }
}
