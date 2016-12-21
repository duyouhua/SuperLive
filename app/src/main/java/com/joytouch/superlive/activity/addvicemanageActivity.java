package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.User;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.widget.RuleshaopDialog;
import com.joytouch.superlive.widget.ShopBuySelectorDialog;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Created by Administrator on 5/25 0025.
 */
public class addvicemanageActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_title;
    private ImageView iv_finish;
    private TextView tv_zhongxin;
    private SharedPreferences sp;
    private RelativeLayout okaddress;
    private TextView update_address;
    private Button bt_ok;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_advice;
    private String add_1;
    private String add_2;
    private int hy=2;
    private String province_id="1";
    private String qu_id="38";
    private String city_id="36";
    private String goods_id;
    private String price;
    private String ballPrice;
    private String oldBallprice;
    private String isactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_manage);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        price = getIntent().getStringExtra("price");
        oldBallprice = getIntent().getStringExtra("oldprice");
        ballPrice = getIntent().getStringExtra("ballprice");
        isactivity = getIntent().getStringExtra("isActivity");
        goods_id=getIntent().getStringExtra("goods_id");
        initUI();
        getdata();
    }

    private void getdata() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(this).httpPost(Preference.useraddress, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        Toast.makeText(addvicemanageActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("用户地址管理", json);
                        Gson gson = new Gson();
                        User Bean = gson.fromJson(json, User.class);
                        if (Bean.status.equals("_0000")) {
                            //没有信息时
                            if (Bean.UserAddress.true_name.equals("")){
                                okaddress.setVisibility(View.GONE);
                                update_address.setVisibility(View.VISIBLE);
                                province_id="1";
                                qu_id="38";
                                city_id="36";
                                hy=0;//添加新地址
                                bt_ok.setVisibility(View.GONE);
                            }else{
                                bt_ok.setVisibility(View.VISIBLE);
                                hy=1;
                                tv_name.setText(Bean.UserAddress.true_name);
                                tv_phone.setText(Bean.UserAddress.tel);
                                tv_advice.setText(Bean.UserAddress.area_info+Bean.UserAddress.address);
                                add_1=Bean.UserAddress.area_info;
                                add_2=Bean.UserAddress.address;
                                update_address.setVisibility(View.GONE);
                                okaddress.setVisibility(View.VISIBLE);
                                province_id=Bean.UserAddress.province_id;
                                qu_id=Bean.UserAddress.qu_id;
                                city_id=Bean.UserAddress.city_id;
                            }
                        } else {
                            Toast.makeText(addvicemanageActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void initUI() {

        tv_advice=(TextView)findViewById(R.id.tv_advice);
        tv_phone=(TextView)findViewById(R.id.tv_phone);
        tv_name=(TextView)findViewById(R.id.tv_name);
        bt_ok=(Button)findViewById(R.id.bt_ok);
        update_address=(TextView)findViewById(R.id.update_address);
        update_address.setOnClickListener(this);
        okaddress=(RelativeLayout)findViewById(R.id.okaddress);
        tv_zhongxin=(TextView) findViewById(R.id.tv_zhongxin);
        tv_zhongxin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_zhongxin.getPaint().setAntiAlias(true);//抗锯齿
        tv_zhongxin.setOnClickListener(this);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        tv_title.setText("地址管理");
        iv_finish.setOnClickListener(this);
        bt_ok.setOnClickListener(this);
        okaddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_finish:
                finish();
                break;
            case R.id.okaddress:
                Intent intent=new Intent(this,UpdateaddressActivity.class);
                intent.putExtra("name",tv_name.getText().toString());
                intent.putExtra("tel",tv_phone.getText().toString());
                intent.putExtra("address_1",add_1);
                intent.putExtra("hy",1);
                intent.putExtra("province_id",province_id);
                intent.putExtra("qu_id",qu_id);
                intent.putExtra("city_id",city_id);
                intent.putExtra("address_2",add_2);
                startActivity(intent);
                break;
            case R.id.tv_zhongxin:
                RuleshaopDialog dialog = new RuleshaopDialog(this);
                dialog.show();
                break;

            case R.id.update_address:
                Intent intent2=new Intent(this,UpdateaddressActivity.class);
                intent2.putExtra("name","");
                intent2.putExtra("tel","");
                intent2.putExtra("hy",0);
                intent2.putExtra("province_id",province_id);
                intent2.putExtra("qu_id",qu_id);
                intent2.putExtra("city_id",city_id);
                intent2.putExtra("address_1","北京北京市西城区");
                intent2.putExtra("address_2","");
                startActivity(intent2);
                break;
            case R.id.bt_ok:
                if (hy==0){
                    Intent intent1=new Intent(this,UpdateaddressActivity.class);
                    intent1.putExtra("name","");
                    intent1.putExtra("tel","");
                    intent1.putExtra("hy",0);
                    intent1.putExtra("province_id",province_id);
                    intent1.putExtra("qu_id",qu_id);
                    intent1.putExtra("city_id",city_id);
                    intent1.putExtra("address_1","北京北京市西城区");
                    intent1.putExtra("address_2","");
                    startActivity(intent1);
                }else if (hy==1) {
                    final ShopBuySelectorDialog dialog1 = new ShopBuySelectorDialog(addvicemanageActivity.this);
                        dialog1.setCanceledOnTouchOutside(true);
                        dialog1.setBallmoney(ballPrice);
                        dialog1.setGoldmoney(price);
                        dialog1.setGoods_id(goods_id);
                        if("1".equals(isactivity)){
                            dialog1.setIsActivty(true);
                        }else {
                            dialog1.setIsActivty(false);
                        }

                        dialog1.setOldmoney(oldBallprice);
                         dialog1.setCancelable(true);
                        dialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if (dialog1.isSuccess()) {
                                    finish();
                                }
                            }
                        });
                    dialog1.show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getdata();
    }
}
