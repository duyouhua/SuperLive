package com.joytouch.superlive.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.javabean.level_jifen;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.FastBlur;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.widget.CircleImageView;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.joytouch.superlive.widget.PullableScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * 其他非主播个人界面
 * 进入此界面需要传user_id,othernickname,otherimg
 * 根据是否是关注,判断是否显示私信(我关注显示私信,对方是我的粉丝,不显示私信)
 * 在通知中,显示我来了一条消息,可以回复信息(需要传userid,othernickname,otherimg)
 */
public class OtherUserMessageActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener,View.OnClickListener{
    private PullToRefreshLayout refreshLayout;
    private PullableScrollView sv;
    private CircleImageView iv_icon;
    private Bitmap bm;
    private TextView tv_bg;
    private ImageView iv_finish;
    private RelativeLayout rl_attention,rl_fans;
    private RelativeLayout rl_letter;
    private RelativeLayout rl_attened;
    private SharedPreferences sp;
    private TextView tv_attention_text;
    private RelativeLayout add_heimingdan;
    private TextView tv_black_text;
    private TagFlowLayout tfl;
    private List<String> strs;
    private String userid;
    private String othernickname;
    private String otherimg;
    private Dialog dialog;
    private String imgurl;
    private TextView tv_name;
    private TextView anchor_level;
    private TextView tv_sign;
    private TextView tv_fans_num;
    private TextView tv_attention_num;
    private TextView tv_load_time;
    private TextView tv_times;
    private TextView tv_percent;
    private TextView tv_gold;
    private RelativeLayout rl_living;
    private TextView tv_living;
    private RelativeLayout rl_style;
    private String other_level="";
    private boolean isShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_message);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        userid=getIntent().getStringExtra("user_id");
        isShow = getIntent().getBooleanExtra("isShow",false);
        othernickname=getIntent().getStringExtra("othernickname");
        otherimg=getIntent().getStringExtra("otherimg");
        Log.e("跳转接收",userid+"//"+othernickname+"//"+otherimg);
        initView();
        getData();
    }

    private void initView() {
        rl_style=(RelativeLayout)this.findViewById(R.id.rl_style);
        tv_living=(TextView)this.findViewById(R.id.tv_living);
        rl_living=(RelativeLayout)this.findViewById(R.id.rl_living);
        tv_gold=(TextView)this.findViewById(R.id.tv_gold);
        tv_percent=(TextView)this.findViewById(R.id.tv_percent);
        tv_times=(TextView)this.findViewById(R.id.tv_times);
        tv_load_time=(TextView)this.findViewById(R.id.tv_load_time);
        tv_attention_num=(TextView)this.findViewById(R.id.tv_attention_num);
        tv_fans_num=(TextView)this.findViewById(R.id.tv_fans_num);
        tv_sign=(TextView)this.findViewById(R.id.tv_sign);
        anchor_level=(TextView)this.findViewById(R.id.anchor_level);
        tv_name=(TextView)this.findViewById(R.id.tv_name);
        tv_black_text=(TextView)this.findViewById(R.id.tv_black_text);
        add_heimingdan=(RelativeLayout)this.findViewById(R.id.add_heimingdan);
        add_heimingdan.setOnClickListener(this);
        tv_attention_text=(TextView)this.findViewById(R.id.tv_attention_text);
        rl_attened=(RelativeLayout)this.findViewById(R.id.rl_attened);
        rl_attened.setOnClickListener(this);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        rl_attention= (RelativeLayout) this.findViewById(R.id.rl_attention);
        rl_fans = (RelativeLayout) this.findViewById(R.id.rl_fans);
        rl_attention.setOnClickListener(this);
        rl_fans.setOnClickListener(this);
        rl_letter = (RelativeLayout) this.findViewById(R.id.rl_letter);
        rl_letter.setOnClickListener(this);
        refreshLayout = (PullToRefreshLayout) this.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setCanPullDown(false);
        refreshLayout.setCanPullUp(false);
//        myListView.setAdapter(new OtherUserStatusAdapter(list, this));
        iv_icon = (CircleImageView) this.findViewById(R.id.iv_icon);
        tv_bg = (TextView) this.findViewById(R.id.tv_bg);
        sv = (PullableScrollView) this.findViewById(R.id.sv);
        tfl = (TagFlowLayout) this.findViewById(R.id.tfl);
        sv.smoothScrollTo(0, 20);
        //主播风格

        imgurl = Preference.img_url+"200x200/"+otherimg;
        ImageLoader.getInstance().displayImage(imgurl, iv_icon, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                bm = bitmap;
                applyBlur();
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        //以下二个需要根据拿到的数据判断是true还是false,和设置相对应的文字
//        tv_black_text.setTag(true);
//        rl_attened.setTag(true);
    }
    private void getDate(String anchor_desp) {
        String sl[] = anchor_desp.split("@");
        strs = new ArrayList<>();
        for (int i=0;i<sl.length;i++){
            strs.add(sl[i]);
        }
        tfl.setAdapter(new TagAdapter<String>(strs) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(OtherUserMessageActivity.this).inflate(R.layout.item_other_auchor_biaoqian, null);
                tv.setText(s);
                return tv;
            }
        });
    }


    public void getData(){
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("phone", Preference.phone)
                .add("userid", userid)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(this).httpPost(Preference.person, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("他人主页", json);
                        Gson gson = new Gson();
                        final level_jifen Bean = gson.fromJson(json, level_jifen.class);
                        if (Bean.status.equals("_0000")) {
                            //刚进来判断是否是拉黑和关注,设置tag和文字,根据是否关注判断是否显示私信
                            tv_name.setText(Bean.Person.name);
//                            anchor_level.setText(Bean.Person.level);
                            tv_sign.setText(Bean.Person.sign);
                            ConfigUtils.level(anchor_level, Bean.Person.level);
                            tv_attention_num.setText(Bean.Person.con_count);
                            tv_fans_num.setText(Bean.Person.fan_count);
                            if (Bean.Person.last_login_time.length() >= 16) {
                                tv_load_time.setText(Bean.Person.last_login_time.substring(6, 16));
                            }
                            tv_times.setText(Bean.Person.part_total + "次");
                            tv_percent.setText(Bean.Person.percent);
                            tv_gold.setText(Bean.Person.win);
                            other_level = Bean.Person.level;
                            if (Bean.Person.is_attention.equals("1")) {
                                rl_attened.setTag(false);
                                tv_attention_text.setText("取消关注");
                                rl_letter.setVisibility(View.VISIBLE);
                            } else {
                                rl_attened.setTag(true);
                                tv_attention_text.setText("关注");
                                rl_letter.setVisibility(View.GONE);
                            }

                            if (Bean.Person.is_black.equals("1")) {
                                tv_black_text.setTag(false);
                                tv_black_text.setText("取消拉黑");
                            } else {
                                tv_black_text.setTag(true);
                                tv_black_text.setText("拉黑");
                            }
                            //风格是否展示
                            if (Bean.Person.anchor_desp.equals("0")) {//不是主播
                                rl_style.setVisibility(View.GONE);
                            } else {//是主播
                                rl_style.setVisibility(View.VISIBLE);
                                getDate(Bean.Person.anchor_desp);
                            }
//                            状态是否展示
                            if (Bean.Person.is_quiz.size() == 0 && Bean.Person.is_live.size() == 0) {//没有状态
                                rl_living.setVisibility(View.GONE);
                            } else if (Bean.Person.is_quiz.size() == 0 && Bean.Person.is_live.size() != 0) {//是在直播
                                rl_living.setVisibility(View.GONE);
                                tv_living.setText("正在直播");
                                rl_living.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(OtherUserMessageActivity.this, LiveDetailActivity.class);
                                        intent.putExtra("romid", Bean.Person.is_live.get(0).room_id);
                                        intent.putExtra("matchid", Bean.Person.is_live.get(0).match_id);
                                        intent.putExtra("qiutanid", "");
                                        startActivity(intent);
                                    }
                                });
                            } else if (Bean.Person.is_quiz.size() != 0 && Bean.Person.is_live.size() == 0) {
                                if(!isShow) {
                                    rl_living.setVisibility(View.GONE);
                                }else {
                                    rl_living.setVisibility(View.GONE);
                                }
                                tv_living.setText("正在竞猜");
                                rl_living.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(OtherUserMessageActivity.this, LiveDetailActivity.class);
                                        intent.putExtra("romid", Bean.Person.is_quiz.get(0).room_id);
                                        intent.putExtra("matchid", Bean.Person.is_quiz.get(0).match_id);
                                        intent.putExtra("qid", Bean.Person.is_quiz.get(0).que_id );
                                        startActivity(intent);
                                    }
                                });
                            }
                        } else {
                            rl_living.setVisibility(View.GONE);
                            Toast.makeText(OtherUserMessageActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
    private void applyBlur() {
        if (bm == null){
            return;
        }
        iv_icon.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                iv_icon.getViewTreeObserver().removeOnPreDrawListener(this);
                iv_icon.buildDrawingCache();
                float w = (float) (tv_bg.getWidth() * 1.0 / bm.getWidth());
                float h = (float) (tv_bg.getHeight() * 1.0 / bm.getHeight());
                Matrix matrix = new Matrix();
                matrix.postScale(w, h); //长和宽放大缩小的比例
                Bitmap bmp = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);


                blur(bmp, tv_bg);
                return true;
            }
        });
    }
    //模糊处理方法
    private void blur(Bitmap bkg, View view) {
        float radius = 4;
        float scaleFactor = 8;

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor), (int) (view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        overlay = FastBlur.doBlur(overlay, (int) radius, true);

        view.setBackground(new BitmapDrawable(this.getResources(), overlay));
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
//            case R.id.rl_attention:
//                toActivity(AttentionAndFansActivity.class);
//                break;
//            case R.id.rl_fans:
//                toActivity(AttentionAndFansActivity.class);
//                break;

            case R.id.rl_letter://发送私信
                //将我的关注人的userid传过去,还有昵称,img都要传
                Intent intent=new Intent(OtherUserMessageActivity.this,ChatActivity.class);
                intent.putExtra("otherid",userid);
                intent.putExtra("othernickname",othernickname);
                intent.putExtra("otherimg",otherimg);
                intent.putExtra("other_level",other_level);
                startActivity(intent);
                finish();
                break;
            case R.id.add_heimingdan://拉黑

                if ((boolean)tv_black_text.getTag()==true){//可以拉黑
                    showaddBlaskDialog("拉黑后你们将解除关注关系,ta不能再关注你或者私信你");
                }else if ((boolean)tv_black_text.getTag()==false){//取消拉黑
                    showaddBlaskDialog("取消拉黑后你们将可以相互关注和私信聊天");
                }
                break;
            case R.id.rl_attened:
                if ((boolean)rl_attened.getTag()==true){//可以添加关注
                    addguanzhu();
                }else if ((boolean)rl_attened.getTag()==false){
                   delguanzhu();
                }
                break;
        }
    }

    private void showaddBlaskDialog(String content_) {
        dialog = new Dialog(OtherUserMessageActivity.this, R.style.Dialog_bocop);
        dialog.setContentView(R.layout.addblack_dialog);
//        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView content=(TextView)dialog.findViewById(R.id.content);
        content.setText(content_);
        TextView tv_canle = (TextView) dialog.findViewById(R.id.tv_canle);
        TextView tv_ok = (TextView) dialog.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if ((boolean)tv_black_text.getTag()==true){//可以拉黑
                    addBlack();
                  dialog.dismiss();
                }else if ((boolean)tv_black_text.getTag()==false){
                    delectblack();
                  dialog.dismiss();
                }
            }
        });
        tv_canle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * 取消拉黑
     */
    private void delectblack() {
        FormBody.Builder build2=new FormBody.Builder();
        build2.add("token", sp.getString(Preference.token,""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("userid",userid)
                .build();
        new HttpRequestUtils(OtherUserMessageActivity.this).httpPost(Preference.Delblack, build2,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {

                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("取消拉黑", json);
                        Gson gson = new Gson();
                        BaseBean Bean = gson.fromJson(json, BaseBean.class);
                        if (Bean.status.equals("_0000")) {
                            tv_black_text.setTag(true);
                            tv_black_text.setText("拉黑");
                        } else {
                            Toast.makeText(OtherUserMessageActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 拉黑
     */
    private void addBlack() {
        FormBody.Builder build2=new FormBody.Builder();
        build2.add("token", sp.getString(Preference.token,""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("userid",userid)
                .build();
        new HttpRequestUtils(OtherUserMessageActivity.this).httpPost(Preference.Addblack, build2,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {

                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("拉黑", json);
                        Gson gson = new Gson();
                        BaseBean Bean = gson.fromJson(json, BaseBean.class);
                        if (Bean.status.equals("_0000")) {
                            tv_black_text.setTag(false);
                            tv_black_text.setText("取消拉黑");
//                            delguanzhu();
                            rl_attened.setTag(true);
                            tv_attention_text.setText("关注");
                            //拉黑后不显示私信
                            rl_letter.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(OtherUserMessageActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 添加关注
     */
    private void addguanzhu() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token, ""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("userid",userid)
                .build();
        new HttpRequestUtils(OtherUserMessageActivity.this).httpPost(Preference.Addcon, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("添加关注", json);
                        Gson gson = new Gson();
                        BaseBean Bean = gson.fromJson(json, BaseBean.class);
                        if (Bean.status.equals("_0000")) {
                            rl_attened.setTag(false);
                            tv_attention_text.setText("取消关注");
                            //添加关注后显示私信
                            rl_letter.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(OtherUserMessageActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 取消关注
     */
    private void delguanzhu() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("userid", userid)
                .build();
        new HttpRequestUtils(OtherUserMessageActivity.this).httpPost(Preference.Delcon, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("取消关注", json);
                        Gson gson = new Gson();
                        BaseBean Bean = gson.fromJson(json, BaseBean.class);
                        if (Bean.status.equals("_0000")) {
                            rl_attened.setTag(true);
                            tv_attention_text.setText("关注");
                            //取消关注后隐藏私信
                            rl_letter.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(OtherUserMessageActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
