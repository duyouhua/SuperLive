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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.nameimginfo;
import com.joytouch.superlive.utils.FileUploadUtil;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.matchUtils;
import com.joytouch.superlive.utils.picturePress.ImageCompressUtil;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 编辑用户个人信息
 * 用户第一次注册或者绑定手机时提醒用户编辑自己的昵称和头像
 */
public class PersonalDataActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_finish;
    private TextView tv_title;
    private TextView tv_right;
    private CircleImageView iv_icon;
    private EditText et_input;
    private Button but_submit;
    private BaseAnimatorSet bas_in;
    private BaseAnimatorSet bas_out;
    private LinearLayout root_my;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private final int SYS_INTENT_REQUEST = 0XFF01;
    private final int CAMERA_INTENT_REQUEST = 0XFF02;
    private String picFileFullName;
    private Bitmap bitmap;//选择的图片对象
    private ImageView camera;
    private EditText et_name;
    private ImageView iv_delete;
    private String wxqq="0";//0表示正常手机注册过来的,1表示新的三方账号登录绑定手机号过来的
    private SharedPreferences sp;
    Map<String, Bitmap> imageMap = new HashMap<String, Bitmap>();
    private Map<String, String> basicMap = new HashMap<String, String>();
    private YujThread yujThread;
    private Bitmap upbitmap=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        wxqq= getIntent().getStringExtra("wxqq");
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        initView();
        bas_in = new BounceTopEnter();
        bas_out = new SlideBottomExit();
        destfile = new File("/data/data/" + Preference.package_name + "/temp");
        if (!destfile.exists()) {
            destfile.mkdirs();
        }
    }

    private void initView() {
        et_name=(EditText)findViewById(R.id.et_name);
        iv_delete=(ImageView)findViewById(R.id.iv_delete);
        et_name.addTextChangedListener(watcher);
        iv_delete.setOnClickListener(this);
        camera=(ImageView)findViewById(R.id.camera);
        root_my=(LinearLayout)findViewById(R.id.root_my);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("个人资料");
        tv_right = (TextView) this.findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("跳过");
        tv_right.setOnClickListener(this);
        iv_icon = (CircleImageView) this.findViewById(R.id.iv_icon);
        iv_icon.setOnClickListener(this);
        et_input = (EditText) this.findViewById(R.id.et_input);
        but_submit = (Button) this.findViewById(R.id.but_submit);
        but_submit.setOnClickListener(this);
        //如果是新的三方账号登录注册绑定手机号过来的,需要展示头像和昵称
        if (wxqq.equals("1")){
            et_name.setHint(sp.getString(Preference.nickname,""));
            ImageLoader.getInstance().displayImage(sp.getString(Preference.headPhoto,""),
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
    }
    public void setBasIn(BaseAnimatorSet bas_in) {
        this.bas_in = bas_in;
    }
    public void setBasOut(BaseAnimatorSet bas_out) {
        this.bas_out = bas_out;
    }
    //监听Edittext中数据变化
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            iv_delete.setVisibility(View.VISIBLE);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.tv_right:
                Intent intent=new Intent(PersonalDataActivity.this,RecommendattentionList.class);
                startActivity(intent);
                finish();
                break;
            case R.id.but_submit:
                int length = matchUtils.getBytes(et_name.getText().toString());
                if (length>24){
                    Toast.makeText(PersonalDataActivity.this, "昵称最多24个字符(或8个汉字)", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                        if (matchUtils.matchone(et_name.getText().toString()) == true) {
                            Toast.makeText(PersonalDataActivity.this, "昵称中包涵敏感字符，修改失败", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            UpImagNick();
                            Intent intent2=new Intent(PersonalDataActivity.this,RecommendattentionList.class);
                            startActivity(intent2);
                            finish();
                        }
                }
                break;
            case R.id.iv_icon:
                ActionSheetDialogNoTitle();
                break;
            case R.id.iv_delete:
                et_name.setText("");
                iv_delete.setVisibility(View.GONE);
                break;
        }
    }

    private void UpImagNick() {
        uploadImage(upbitmap);
    }

    private void ActionSheetDialogNoTitle() {
        final String[] stringItems = {"拍照", "相册"};
        final ActionSheetDialog dialog = new ActionSheetDialog(PersonalDataActivity.this, stringItems, root_my);
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
    //打开本地相册
    public void openAlbum(){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("image/*");
        startActivityForResult(intent, SYS_INTENT_REQUEST);
    }
    /**
     * 拍照
     */
    public void takePicture(){
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
                    camera.setVisibility(View.GONE);
                    setBitmap(bitmap);
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == CAMERA_INTENT_REQUEST
                && resultCode == RESULT_OK && data != null) {
            cameraCamera(data);
        }else{
            camera.setVisibility(View.VISIBLE);
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
        camera.setVisibility(View.GONE);
        iv_icon.setImageBitmap(bitmap);
        //上传头像
//        Update(path);
//        uploadImage(bitmap);
        setBitmap(bitmap);
    }

    private void setBitmap(Bitmap bitmap) {
        upbitmap=bitmap;
    }

    private void uploadImage(Bitmap bitmap) {
            if (bitmap!=null){
                imageMap.put("image", bitmap);
            }
            basicMap.put("phone", "1");
            basicMap.put("nick_name",et_name.getText().toString()+"");
            basicMap.put("token", sp.getString(Preference.token,""));
            basicMap.put("version", "1");
            yujThread = new YujThread();
            yujThread.start();
        }

    private class YujThread extends Thread {
        @Override
        public void run() {
            super.run();
            String result = "";
            try {
                result = FileUploadUtil.uploadImage(Preference.Editinfo, basicMap, imageMap);
                Gson gson=new Gson();
                nameimginfo info = gson.fromJson(result, nameimginfo.class);
                if (info.status.equals("_0000")) {
                    sp.edit().putString(Preference.headPhoto, info.img_id)
                            .commit();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            destfile.delete();
                        }
                    }, 100);
                }else if (info.equals("_1000")) {
                    new LoginUtils(PersonalDataActivity.this).reLogin(PersonalDataActivity.this);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
