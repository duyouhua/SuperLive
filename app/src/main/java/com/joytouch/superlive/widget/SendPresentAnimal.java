package com.joytouch.superlive.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.ChargeActivity;
import com.joytouch.superlive.adapter.BaseListAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.javabean.SendRedBase;
import com.joytouch.superlive.javabean.registInfo;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.widget.MyScrollview.MyGridView;
import com.joytouch.superlive.widget.MyScrollview.PurchasePopUpWindow;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by Administrator on 7/18 0018.
 */
public class SendPresentAnimal {
    private static Activity  context;
    private static TextView btn_submit;
    private static ViewPager vp_details;
    private static RelativeLayout ll_present_root;
    private static RelativeLayout ll_time_down;
    private static MyPagerAdapter mMyadapter;
    private static LayoutInflater inflater;
    private static TextView tv_counttime;
    private static Time_CountNum timeCount;
    private static ImageView circle_bac;
    private static TextView mymoney;
    private static boolean isshow=false;
    private static ArrayList<String> name_two;
    private static ArrayList<String> name_one;
    private static ArrayList<String> name_three;
    private static ArrayList<String> name_four;
    private static int ok=0;
    private  int id;
    private static String color="0";
    private  Context comtext;
    private static ArrayList<String> data_one;
    private static ArrayList<String> data_two;
    private static ArrayList<String> data_three;
    private static ArrayList<String> data_four;
    private static ChildrenIconAdapter adapter_one;
    private static ChildrenIconAdapter adapter_two;
    private static ChildrenIconAdapter adapter_three;
    private static ChildrenIconAdapter adapter_four;
    private static List<View> listViews;
    private static int all_num=8;//礼物总数
    private static  int length = 2;//礼物页数
    private static int numlines=4;//每一个gridview的列数
    private  static boolean iszhubo=false;
    public static String getpid="";//默认第一次就是""的
    private static MyGridView[] mGridView = new MyGridView[length];
    private static String room_id;
    private static String match_id;
    //礼物金额和图片路径
    static String[] attrs = {"1","6","300","888","1","66","300","888"};
    static  String [] names={"随悦","毛巾","冠军戒指","红包","燕京啤酒","加油","大力神杯","啦啦队"};
    static int[] present_1evel = new int[]{R.drawable.present_1,R.drawable.present_2,R.drawable.present_3
            ,R.drawable.present_4,R.drawable.present_5,R.drawable.present_6
            ,R.drawable.present_7,R.drawable.present_8};
    static View.OnClickListener onClicker = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
        }
    };
    private static SharedPreferences sp;

    public SendPresentAnimal(Activity context,int id,String color,boolean isancher,String room_id,String match_id) {
        Log.e("room_id_red",room_id);
        this.context=context;
        this.room_id = room_id;
        this.match_id = match_id;
        this.id=id;
        this.color=color;
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        iszhubo=isancher;
        if (iszhubo==true){
            all_num=1;
            length=1;
            numlines=4;
        }else{
            all_num=8;
            length=2;
            numlines=4;
        }
        inflater = LayoutInflater.from(context.getApplicationContext());
        listViews = new ArrayList<View>();
        for (int k=0;k<length;k++){
            listViews.add(inflater.inflate(R.layout.purchase_gridview, null));
        }
    }

    public static void setNoXuanzhuan(){
        if (ll_present_root!=null){
            ll_present_root.setVisibility(View.GONE);
        }
    }


    private static ArrayList<String> getDataTwo() {

        ArrayList<String> result = new ArrayList<String>();

        for (int i = 0; i < all_num; i++) {
            result.add(attrs[i]);
        }
        return result;
    }
    private static ArrayList<String> getData_name() {

        ArrayList<String> result = new ArrayList<String>();

        for (int i = 0; i < all_num; i++) {
            result.add(names[i]);
        }
        return result;
    }
    /**
     * 不是主播:  展示动画  //0 主播  1 蓝色  2红色
     */
    public static void popupWindow_Present(){
        //友盟礼物列表计数事件
        MobclickAgent.onEvent(context, "presentlist");
        getdata();
    }

    public static  void  popupwindow_anchoe(){
        //友盟礼物列表计数事件
        MobclickAgent.onEvent(context, "presentlist");
        anchor_dialog();
    }

    private static void anchor_dialog() {
        final Dialog dialog = new Dialog(context, R.style.Dialog_bocop);
        dialog.setContentView(R.layout.red_anchor);
        ImageView close = (ImageView) dialog.findViewById(R.id.cancle);
        TextView sucess = (TextView) dialog.findViewById(R.id.sucess);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        sucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRed(room_id, match_id,new PurchasePopUpWindow(context,
                        onClicker));
//                sendRedmoney(color,getpid);
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失dialog
        dialog.setCancelable(true);//点击返回键取消
        dialog.show();
    }

    private static void sendRedmoney(String ident, String pid) {

        FormBody.Builder build=new FormBody.Builder();
        build
                .add("token", sp.getString(Preference.token, ""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("anchor_id", Preference.zhubo_id)
                .add("ident", ident)//0 代表普通,1代表红色,2代表蓝色
                .add("pid",pid)
                .add("room_id",Preference.room_id)
                .add("gift_id", "4")
                .build();
        new HttpRequestUtils(context).httpPost(Preference.present, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
                        getpid = "";
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("赠送礼物", json);
                        Gson gson = new Gson();
                        BaseBean Bean = gson.fromJson(json, BaseBean.class);
                        if (Bean.status.equals("_0000")) {
                            getpid = Bean.pid;
                        } else if (Bean.status.equals("_2001")) {
                            showDialog();
                        } else {
                            getpid = "";
                            Toast.makeText(context, Bean.message, Toast.LENGTH_SHORT).show();
                        }
                        //友盟每一个礼物的统计
                        MobclickAgent.onEvent(context, "present" + (oldposition + 1) + "");

                    }
                });
    }


    public static class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = mListViews.get(position);
            ViewGroup parent = (ViewGroup) v.getParent();
            // ((ViewPager) collection).addView(v, 0);
            if (parent != null) {
                parent.removeAllViews();
            }
            container.addView(v);
            return v;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

    }
    /**
     * childgridview图片适配
     */
    private static int oldposition=-1;
    private static class ChildrenIconAdapter extends BaseListAdapter<String> {
        private  List<String> name_;
        private  String type;
        private List<String> mData;

        public ChildrenIconAdapter(Context context, List<String> mData,String type,List<String> name_) {
            super(context, mData);
            // TODO Auto-generated constructor stub
            this.mData = mData;
            this.type=type;
            this.name_=name_;
        }



        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        private class ViewHolder {
            ImageView iv_present;
            TextView tv_iconname,tv_liwuname;
            RelativeLayout rl_bac_item;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.home_custom_grid_item, null);
                holder.tv_iconname = (TextView) convertView
                        .findViewById(R.id.tv_presentname);
                holder.iv_present = (ImageView) convertView
                        .findViewById(R.id.iv_item);
                holder.tv_liwuname= (TextView) convertView.findViewById(R.id.tv_name_liwu);
                holder.rl_bac_item=(RelativeLayout)convertView.findViewById(R.id.rl_bac_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_iconname.setText(mData.get(position));
            holder.tv_liwuname.setText(name_.get(position));
            holder.rl_bac_item.setBackgroundResource(R.drawable.click_list_item);
            if (type.equals("one")){
                //当时主播的时候只需要红包出现
                if (iszhubo==true){
                    holder.iv_present.setBackgroundResource(present_1evel[3]);
                    holder.tv_iconname.setText(attrs[3]);
                    if (position!=0){
                        convertView.setVisibility(View.GONE);
                    }
                }else{
                    holder.iv_present.setBackgroundResource(present_1evel[position]);
                }

            }else if (type.equals("two")){
                holder.iv_present.setBackgroundResource(present_1evel[4 + position]);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.rl_bac_item.setSelected(true);
                    if (type.equals("one")){
                        if (oldposition!=position){
                            for (int e=0;e<length;e++){
                                if (e==0 && oldposition<numlines){
                                    if (oldposition!=-1){
                                        mGridView[e].getChildAt(oldposition).findViewById(R.id.rl_bac_item).setSelected(false);
                                    }
                                }else if (e==0 && oldposition>=numlines){
                                    if (oldposition!=-1){
                                        mGridView[e+1].getChildAt(oldposition-numlines).findViewById(R.id.rl_bac_item).setSelected(false);
                                    }
                                }
                            }
                        }
                        if (position!=oldposition){
                            getpid="";
                            btn_submit.setVisibility(View.VISIBLE);
                            ll_time_down.setVisibility(View.GONE);

                        }
                        oldposition=position;
                    }else if (type.equals("two")){
                        if (oldposition!=position+4){
                            for (int e=0;e<length;e++){
                                if (e==1 && oldposition>=numlines){
                                    if (oldposition!=-1){
                                        mGridView[e].getChildAt(oldposition-numlines).findViewById(R.id.rl_bac_item).setSelected(false);
                                    }
                                }else if (e==1 && oldposition<numlines){
                                    if (oldposition!=-1){
                                        mGridView[e-1].getChildAt(oldposition).findViewById(R.id.rl_bac_item).setSelected(false);
                                    }
                                }
                            }
                        }
                        if ((position+4)!=oldposition){
                            getpid="";
                            btn_submit.setVisibility(View.VISIBLE);
                            ll_time_down.setVisibility(View.GONE);
                        }
                        oldposition=position+4;
                    }
                }
            });
            return convertView;
        }

        @Override
        protected View getItemView(View convertView, int position) {
            // TODO Auto-generated method stub
            return null;
        }

        public void update(List<String> values) {
            mData = values;
            notifyDataSetInvalidated();
            notifyDataSetChanged();
        }
    }

    public static class Time_CountNum extends CountDownTimer {
        private TextView button;
        private String msg;

        public Time_CountNum(TextView button, long millisInFuture, long countDownInterval, String msg) {
            super(millisInFuture, countDownInterval);
            this.button = button;
            this.msg = msg;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            button.setText(millisUntilFinished / 100+"");
            button.setTextColor(Color.parseColor("#ffffff"));
        }

        @Override
        public void onFinish() {
            button.setText(msg);
            ll_time_down.setVisibility(View.GONE);
            btn_submit.setVisibility(View.VISIBLE);
//            getpid="";
        }

    }

    public static void getdata(){
            FormBody.Builder build=new FormBody.Builder();
            build
                    .add("token", sp.getString(Preference.token,""))
                    .add("phone", Preference.phone)
                    .add("version", Preference.version)
                    .build();
            new HttpRequestUtils(context).httpPost(Preference.Myinfo, build,
                    new HttpRequestUtils.ResRultListener() {
                        @Override
                        public void onFailure(IOException e) {

                        }

                        @Override
                        public void onSuccess(String json) {
                            Log.e("我的余额", json);
                            Gson gson = new Gson();
                            registInfo Bean = gson.fromJson(json, registInfo.class);
                            if (Bean.status.equals("_0000")) {
                                Gson info = new Gson();
                                registInfo registinfo = info.fromJson(json, registInfo.class);
                                if (isshow == false) {
                                    popu();
                                }
                                mymoney.setText(registinfo.user_info.balance);
                            } else if (Bean.status.equals("_1000")) {
                                new LoginUtils(context).reLogin(context);
                            }

                        }
                    });
    }

    //充值dialog
    private static void showDialog() {
        final Dialog dialog = new Dialog(context, R.style.Dialog_bocop);
        //判断dialog是否显示
        if (dialog.isShowing()){
            return;
        }
        dialog.setContentView(R.layout.chargemoney);
        TextView close = (TextView) dialog.findViewById(R.id.cancle);
        TextView sucess = (TextView) dialog.findViewById(R.id.sucess);
        dialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        sucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChargeActivity.class);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失dialog
        dialog.setCancelable(true);//点击返回键取消
        if (ok==0){
            dialog.show();
            ok=1;
        }
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ok=0;
            }
        });
    }

    private static void popu() {

        final PurchasePopUpWindow mPopUpWindow = new PurchasePopUpWindow(context,
                onClicker);
        mPopUpWindow.showAtLocation(
                context.findViewById(R.id.add_content),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        isshow=true;
        mPopUpWindow.setOutsideTouchable(true);
        View view = mPopUpWindow.getContentView();
        mymoney=(TextView)view.findViewById(R.id.mymoney);
        ll_present_root=(RelativeLayout)view.findViewById(R.id.ll_present_root);
        btn_submit = (TextView) view.findViewById(R.id.btn_submit);
        vp_details = (ViewPager) view.findViewById(R.id.vp_details);
        tv_counttime=(TextView)view.findViewById(R.id.tv_counttime);
        circle_bac=(ImageView)view.findViewById(R.id.circle_bac);
        ll_present_root=(RelativeLayout)view.findViewById(R.id.ll_present_root);
        ll_present_root.setVisibility(View.VISIBLE);
        view.findViewById(R.id.ll_rrr).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        ll_time_down=(RelativeLayout)view.findViewById(R.id.ll_time_down);
        ll_time_down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        view.findViewById(R.id.tv_chongzhi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ChargeActivity.class);
                context.startActivity(intent);
            }
        });
        //赠送主播
        if (color.equals("0")){
            btn_submit.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.retang_solid_text_bg));
            circle_bac.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.circle_lvse));
        }else if (color.equals("2")){
            btn_submit.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.retang_solid_text_bg1));
            circle_bac.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.circle_lanse));
        }else if (color.equals("1")){
            btn_submit.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.retang_solid_text_bg2));
            circle_bac.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.circle_hongse));
        }
        //倒计时3秒
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("选择",oldposition+"");
                //如果是主播,直接发红包,否则进入倒计时连点
                if (iszhubo==true){
                    if (oldposition!=-1){
                     btn_submit.setEnabled(false);
                     sendRed(room_id,match_id,mPopUpWindow);
//                    Givinggifts(color,getpid,mPopUpWindow);
                    }else{
                        Toast.makeText(context, "请选择红包", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if (oldposition!=-1){
                        //如果是红包不能连点
                        if (!"4".equals((oldposition+1)+"")){
                            ll_time_down.setVisibility(View.VISIBLE);
                            btn_submit.setVisibility(View.GONE);
                            //3秒跑10次
                            timeCount = new Time_CountNum(tv_counttime, 3000, 100, "0");
                            timeCount.start();
                            Givinggifts(color, getpid, mPopUpWindow);
                        }else{
                            //添加红包动画
                            Log.e("hongbao123","11111111");
                            btn_submit.setEnabled(false);
                            sendRed(room_id, match_id, mPopUpWindow);
//                            Givinggifts(color,getpid,mPopUpWindow);
                   }


                    }else{
                        Toast.makeText(context, "请选择礼物", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        mPopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                oldposition = -1;
                isshow = false;
                getpid = "";
                btn_submit.setEnabled(true);
            }
        });
        //赠送礼物
        circle_bac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Givinggifts(color, getpid, mPopUpWindow);
                timeCount.cancel();
                timeCount=null;
                timeCount = new Time_CountNum(tv_counttime, 3000, 100, "0");
                timeCount.start();

            }
        });

        mMyadapter = new MyPagerAdapter(listViews);
        vp_details.setAdapter(mMyadapter);
        vp_details.setCurrentItem(0);

        name_one=new  ArrayList<String>();
        name_two=new ArrayList<String>();
        name_three=new  ArrayList<String>();
        name_four=new ArrayList<String>();

        data_one = new ArrayList<String>();
        data_two = new ArrayList<String>();
        data_three = new ArrayList<String>();
        data_four= new ArrayList<String>();

        adapter_one = new ChildrenIconAdapter(context, data_one,"one",name_one);
        adapter_two = new ChildrenIconAdapter(context, data_two,"two",name_two);
        adapter_three = new ChildrenIconAdapter(context,data_three,"three",name_three);
        adapter_four = new ChildrenIconAdapter(context,data_four,"four",name_four);

        for (int i = 0; i < length; i++) {
            mGridView[i] = (MyGridView) listViews.get(i).findViewById(
                    R.id.gridview);
            mGridView[i].setNumColumns(numlines);
        }
        if (getDataTwo().size() <= 4) {
            for (int i = 0; i < getDataTwo().size(); i++) {
                data_one.add(getDataTwo().get(i).toString().trim());
                name_one.add(getData_name().get(i).toString().trim());
            }

        } else if (getDataTwo().size() > 4 && getDataTwo().size() <= 8) {
            for (int i = 0; i < 4; i++) {
                data_one.add(getDataTwo().get(i).toString().trim());
                name_one.add(getData_name().get(i).toString().trim());
            }
            for (int i = 4; i < getDataTwo().size(); i++) {
                data_two.add(getDataTwo().get(i).toString().trim());
                name_two.add(getData_name().get(i).toString().trim());
            }
        } else if (getDataTwo().size() > 8 && getDataTwo().size() <= 12) {
            for (int i = 0; i < 4; i++) {
                data_one.add(getDataTwo().get(i).toString().trim());
                name_one.add(getData_name().get(i).toString().trim());
            }
            for (int i = 4; i < 8; i++) {
                data_two.add(getDataTwo().get(i).toString().trim());
                name_two.add(getData_name().get(i).toString().trim());
            }
            for (int i = 8; i < 12 && i < getDataTwo().size(); i++) {
                data_three.add(getDataTwo().get(i).toString().trim());
                name_three.add(getData_name().get(i).toString().trim());
            }
        }else{
            for (int i = 0; i < 4; i++) {
                data_one.add(getDataTwo().get(i).toString().trim());
                name_one.add(getData_name().get(i).toString().trim());
            }
            for (int i = 4; i < 8; i++) {
                data_two.add(getDataTwo().get(i).toString().trim());
                name_two.add(getData_name().get(i).toString().trim());
            }
            for (int i = 8; i < 12 ; i++) {
                data_three.add(getDataTwo().get(i).toString().trim());
                name_three.add(getData_name().get(i).toString().trim());
            }
            for (int i=12;i< 16 && i < getDataTwo().size();i++){
                data_four.add(getDataTwo().get(i).toString().trim());
                name_four.add(getData_name().get(i).toString().trim());
            }
        }
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                mGridView[i].setAdapter(adapter_one);
            }
            if (i == 1) {
                mGridView[i].setAdapter(adapter_two);
            }
            if (i == 2) {
                mGridView[i].setAdapter(adapter_three);
            }
            if (i == 3) {
                mGridView[i].setAdapter(adapter_four);
            }
        }
    }

    private static void Givinggifts(String ident, String pid, final PurchasePopUpWindow Window) {
        FormBody.Builder build=new FormBody.Builder();
        //主播值显示红包,gift_id为4
        if (iszhubo==true){
            build
                    .add("token", sp.getString(Preference.token, ""))
                    .add("phone", Preference.phone)
                    .add("version", Preference.version)
                    .add("anchor_id", Preference.zhubo_id)
                    .add("ident", ident)//0 代表普通,1代表红色,2代表蓝色
                    .add("pid",pid)
                    .add("room_id",Preference.room_id)
                    .add("gift_id", "4")
                    .build();
        }else{
            build
                    .add("token", sp.getString(Preference.token, ""))
                    .add("phone", Preference.phone)
                    .add("version", Preference.version)
                    .add("anchor_id", Preference.zhubo_id)
                    .add("ident", ident)//0 代表普通,1代表红色,2代表蓝色
                    .add("pid", pid)
                    .add("room_id",Preference.room_id)
                    .add("gift_id", oldposition + 1 + "")
                    .build();
        }
        Log.e("getpid","getpid="+getpid+"  *ident==="+ident+"==="+(oldposition+1)+""+"==="+Preference.zhubo_id+"==="+Preference.room_id+"   pid="+pid);
        new HttpRequestUtils(context).httpPost(Preference.present, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
                        getpid="";
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("赠送礼物",json);
                        Gson gson = new Gson();
                        BaseBean Bean = gson.fromJson(json, BaseBean.class);
                        if (Bean.status.equals("_0000")) {
                            mymoney.setText(Bean.wallet);
                            getpid=Bean.pid;
//                            if (color.equals("1") || color.equals("2")){
//                                PresentCallback back = (PresentCallback) context;
//                                back.changestste(Integer.parseInt(Bean.gift_of_red),Integer.parseInt(Bean.gift_of_blue));
//                            }
                        } else  if (Bean.status.equals("_2001")){
                            Window.dismiss();
                            showDialog();
                        }else{
                            getpid="";
                            Toast.makeText(context, Bean.message, Toast.LENGTH_SHORT).show();
                        }
                        //友盟每一个礼物的统计
                        MobclickAgent.onEvent(context, "present"+(oldposition+1)+"");

                    }
                });
    }
    /**
     * 自定义监听改变通知列表的数据(不包括离线消息)
     */
    private static sdapter receListener_;
    public static void setReceiveListener(sdapter receListener) {
        receListener_ = receListener;
    }
    public interface sdapter{
        void adapterdata(int red,int blue);
    }
//    if(receListener_!=null){
//        receListener_.adapterdata(1,2);
//    }
    public static void sendRed(String room_id,String match_id,final PurchasePopUpWindow Window){
        FormBody.Builder builder = new FormBody.Builder();
        Log.e("tokenhongbao",(String) SPUtils.get(context, Preference.token, "", Preference.preference));
        builder.add("token", (String) SPUtils.get(context, Preference.token, "", Preference.preference))
                .add("room_id",room_id)
                .add("match_id", match_id);
        Log.e("match_id_red", match_id);
        Log.e("room_id_red",room_id);
        new HttpRequestUtils(context).httpPost(Preference.sendRed, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                Log.e("errorhongbao",e.toString());
            }

            @Override
            public void onSuccess(String json) {
                if (Window!=null){
                    Window.dismiss();
                }
                Log.e("hongbao","1111"+json);
                if (ConfigUtils.isJsonFormat(json)){
                    Gson gson = new Gson();
                    Type type = new TypeToken<SendRedBase>(){}.getType();
                    SendRedBase base = gson.fromJson(json,type);
                    if ("ok".equals(base.getResult().getRes_code())){
                        Log.e("balance",base.getResult().getBalance());
                        if (null != mymoney){
                            mymoney.setText(base.getResult().getBalance());
                        }
                    }else {
                        //金币不足
                        showDialog();
                    }
                }else {

                }
                //友盟发红包计数事件
                MobclickAgent.onEvent(context, "redpackage");
            }
        });
    }
}
