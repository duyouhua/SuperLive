package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.ShopAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.interfaces.ChangstatusBack;
import com.joytouch.superlive.javabean.ShopItemInfo;
import com.joytouch.superlive.javabean.ShopJavabean;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.view.CommonShowView;
import com.joytouch.superlive.widget.PullToRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * 商城界面
 */
public class ShopActivity extends BaseActivity implements View.OnClickListener,ChangstatusBack {
    private ImageView iv_finish;
    private TextView tv_title;
    private TextView tv_right;
    private PullToRefreshLayout refreshLayout;
    private ListView listView;
    private SharedPreferences sp;
    private List<ShopItemInfo> shopGoods;
    private ImageView asset_hongdian;
    private String asset;
    private CommonShowView loadstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        setContentView(R.layout.activity_shop);
        asset=this.getIntent().getStringExtra("asset");
        initView();
        getdata();

    }

    private void getdata() {
        FormBody.Builder build_2=new FormBody.Builder();
        build_2
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("token", sp.getString(Preference.token, ""))
//                .add("token","d156caf2-b83a-48ab-ad14-5e65f4d83ef2")
                .build();

        new HttpRequestUtils(this).httpPost(Preference.goods, build_2,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        //访问网络失败后,隐藏动画.显示重新加载
                        loadstate.stopAnimal();
                        loadstate.setfail();
                        loadstate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadstate.setloading();
                                loadstate.starAnimal();
                                getdata();
                            }
                        });
                    }
                    @Override
                    public void onSuccess(String json) {
                        Log.e("商品列表",json.toString());
                        //加载成功后,隐藏动画
                        loadstate.stopAnimal();
                        Gson gson = new Gson();
                        ShopJavabean info = gson.fromJson(json, ShopJavabean.class);
                        if (info.status.equals("_0000")){
                            shopGoods=new ArrayList<ShopItemInfo>();
                            shopGoods.clear();
                            shopGoods.addAll(info.Goods);
                            if (shopGoods.size()>0){
                                //有数据的情况下,隐藏界面
                                loadstate.setVisibility(View.GONE);
                            }else{
                                //如果没有数据,显示暂无数据
                                loadstate.setnull();
                            }

//                            ShopItemInfo infos=new ShopItemInfo();
//                            infos.bgcolor="";
//                            infos.chat_color="";
//                            infos.chat_img="";
//                            infos.detail="兑换流量";
//                            infos.expire="----";
//                            infos.goods_id="";
//                            infos.image="";
//                            infos.name="流量包";
//                            infos.price=2000;
//                            infos.status="0";
//                            infos.type="2";
//                            shopGoods.add(infos);
                            listView.setAdapter(new ShopAdapter(shopGoods, ShopActivity.this));
                        }else{
                            Toast.makeText(ShopActivity.this, info.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initView() {
        loadstate=(CommonShowView)this.findViewById(R.id.loadstate_ing);
        loadstate.setloading();
        loadstate.starAnimal();
        asset_hongdian=(ImageView)this.findViewById(R.id.asset_hongdian);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("商城");
        tv_right = (TextView) this.findViewById(R.id.tv_right);
        tv_right.setText("我的道具");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);
        refreshLayout = (PullToRefreshLayout) this.findViewById(R.id.refresh);
        refreshLayout.setCanPullUp(false);
        refreshLayout.setCanPullDown(false);
        listView = (ListView) this.findViewById(R.id.listView);
        if (asset.equals("1")){
            asset_hongdian.setVisibility(View.VISIBLE);
        }else{
            asset_hongdian.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.tv_right:
                toActivity(MyPropActivity.class);
                break;
        }
    }


    @Override
    public void anchor(int position) {
        getdata();
    }
}
