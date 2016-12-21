package com.joytouch.superlive.activity;

import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cjzhibo.im.ImApi;
import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.app.SuperLiveApplication;
import com.joytouch.superlive.fragement.GuangChangFagement;
import com.joytouch.superlive.fragement.LiveListFragment;
import com.joytouch.superlive.fragement.MineFragment;
import com.joytouch.superlive.fragement.ReviewFragment;
import com.joytouch.superlive.interfaces.ChanBestBack;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.javabean.CammerSaishiBean;
import com.joytouch.superlive.javabean.ChatInfo;
import com.joytouch.superlive.javabean.messagelist;
import com.joytouch.superlive.javabean.messagelistAll;
import com.joytouch.superlive.javabean.redjavabean;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.utils.xutil.XUtil;
import com.joytouch.superlive.widget.LiveSelectorDialog;
import com.umeng.analytics.MobclickAgent;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/4/6.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener ,ChanBestBack {
    private LinearLayout live;
    private LinearLayout square;
    private LinearLayout liveList;
    private LinearLayout review;
    private RadioButton live_log;
    private RadioButton live_text;
    private RadioButton square_logo;
    private RadioButton square_text;
    private RadioButton review_logo;
    private RadioButton review_text;
    private RadioButton mine_logo;
    private RadioButton mine_text;
    private RelativeLayout mine;
    private Button livelist_logo;
    private int RBT = 2;//用于记录当前选择的是那个fragement
    private int oldRBT;
    private ArrayList<Fragment> fragments ;
    private FragmentManager fragmentManager;
    private long oldTime;
    private ImageView message;
    private boolean isMessage;//是否有新消息
    private SharedPreferences sp;
    private LinearLayout ll;
    private DbManager db;
    //存储所有的关于我的通知消息
    private String otherimg="";
    private String otherlevel="";
    private String othernickname="";
    private String content="";
    private String match_id="";
    private String room_id="";
    private String qiutan_id="";
    private String type="0";
    private String systemimg_url="";
    private String intent_url="";
    private String text="";
    private DbManager db_all;
    private String isChatActivity;
    private String is_private;
    private String mission;
    private String asset;
    private boolean isFirstLive = true;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            livelist_logo.setEnabled(true);
        }
    };
    //签到: 获取返回时间+24小时,保存本地时间和是否已经签到,每一次打开app和后台到前台时判断是否执行签到,切换账号指控本地保存的数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        //创建一个表存储需要在列表中填充数据的表   MessageTable
        DbManager.DaoConfig daoConfig= XUtil.getMessageListDaoConfig();
        db = x.getDb(daoConfig);
        //创建一个数据库存储所有的数据  MessageTableAll
        DbManager.DaoConfig daoConfig_ = XUtil.getMessageList();
        db_all = x.getDb(daoConfig_);
        isFirstLive = true;
        initView();
    }
    GuangChangFagement squareft;
    private void initView() {
        live = (LinearLayout) findViewById(R.id.live);
        square = (LinearLayout) findViewById(R.id.square);
        liveList = (LinearLayout) findViewById(R.id.livelist);
        review = (LinearLayout) findViewById(R.id.review);
        mine = (RelativeLayout) findViewById(R.id.mine);
        livelist_logo = (Button) findViewById(R.id.livelist_logo);
        ll = (LinearLayout) findViewById(R.id.ll);
        message = (ImageView) findViewById(R.id.message);
        live_log = (RadioButton) findViewById(R.id.live_logo);
        live_text = (RadioButton) findViewById(R.id.live_content);
        square_logo = (RadioButton) findViewById(R.id.square_logo);
        square_text = (RadioButton) findViewById(R.id.square_content);
        review_logo = (RadioButton) findViewById(R.id.review_logo);
        review_text = (RadioButton) findViewById(R.id.review_content);
        mine_logo = (RadioButton) findViewById(R.id.mine_logo);
        mine_text = (RadioButton) findViewById(R.id.mine_content);
        //添加fragement
        fragments = new ArrayList<Fragment>();

//        SquareFragment squareft = new SquareFragment();

        squareft = new GuangChangFagement();
        LiveListFragment livelistft = new LiveListFragment();
        ReviewFragment reviewft = new ReviewFragment();
        //改变地方
        MineFragment mineft = new MineFragment();

        fragments.add(livelistft);
        fragments.add(squareft);
        fragments.add(reviewft);
        fragments.add(mineft);

        live.setOnClickListener(this);
        square.setOnClickListener(this);
        review.setOnClickListener(this);
        ll.setOnClickListener(this);
        live_log.setOnClickListener(this);
        live_text.setOnClickListener(this);
        square_logo.setOnClickListener(this);
        square_text.setOnClickListener(this);
        review_logo.setOnClickListener(this);
        review_text.setOnClickListener(this);
        mine_logo.setOnClickListener(this);
        mine_text.setOnClickListener(this);

        selectFragment(0);
        livelist_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                livelist_logo.setEnabled(false);
                if (Integer.valueOf((String) SPUtils.get(MainActivity.this,Preference.level,"",Preference.preference))>=3){
                    openlive();
                }else {
                    showToast("对不起，3级以上才能开直播，快去升级吧");
                    handler.sendEmptyMessageDelayed(0,1500);
                }
            }
        });
        RBT = 0;
        ((RadioButton)live.getChildAt(0)).setChecked(true);
        ((RadioButton)live.getChildAt(1)).setChecked(true);
    }

    private void openlive() {
        FormBody.Builder build_=new FormBody.Builder();
        build_
                .add("token", sp.getString(Preference.token, ""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(this).httpPost(Preference.openlive, build_,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        livelist_logo.setEnabled(true);
                        Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onSuccess(String json) {
                        livelist_logo.setEnabled(true);
                        Log.e("开直播", json);
                        Gson gson = new Gson();
                        CammerSaishiBean Bean = gson.fromJson(json, CammerSaishiBean.class);
                        if (Bean.status.equals("_0000")) {//上个房间未关闭
                            Intent intent = new Intent(MainActivity.this, LiveStreamingActivity.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putSerializable("stream_info", Bean);
                            intent.putExtras(mBundle);
                            startActivity(intent);
                        } else if (Bean.status.equals("_2002")){//新开一个房间
                            new LiveSelectorDialog(MainActivity.this).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.live_logo:
            case R.id.live_content:
            case R.id.live:
                RBT = 0;
                ((RadioButton)live.getChildAt(0)).setChecked(true);
                ((RadioButton)live.getChildAt(1)).setChecked(true);
                break;
            case R.id.square_logo:
            case R.id.square_content:
            case R.id.square:
                RBT =1;
                ((RadioButton)square.getChildAt(0)).setChecked(true);
                ((RadioButton)square.getChildAt(1)).setChecked(true);
                break;
            case R.id.review_content:
            case R.id.review_logo:
            case R.id.review:
                RBT = 2;
                ((RadioButton)review.getChildAt(0)).setChecked(true);
                ((RadioButton)review.getChildAt(1)).setChecked(true);
                break;
            case R.id.mine_logo:
            case R.id.mine_content:
            case R.id.ll:
                RBT = 3;
                ((RadioButton)ll.getChildAt(0)).setChecked(true);
                ((RadioButton)ll.getChildAt(1)).setChecked(true);
                break;
        }
        if(RBT != oldRBT) {
            selectFragment(RBT);
        }
    }
    //选着哪个fragment
    public  void selectFragment(int i){
        //将上个选择设置成未选择
        switch (oldRBT){
            case 0:
                ((RadioButton)live.getChildAt(0)).setChecked(false);
                ((RadioButton)live.getChildAt(1)).setChecked(false);
                break;
            case 1:
                ((RadioButton)square.getChildAt(0)).setChecked(false);
                ((RadioButton)square.getChildAt(1)).setChecked(false);
                break;
            case 2:
                ((RadioButton)review.getChildAt(0)).setChecked(false);
                ((RadioButton)review.getChildAt(1)).setChecked(false);
                break;
            case 3:
                ((RadioButton)ll.getChildAt(0)).setChecked(false);
                ((RadioButton)ll.getChildAt(1)).setChecked(false);
                break;
        }
        if(isMessage){
            if(i==3){
                message.setVisibility(View.GONE);
            }else {
                message.setVisibility(View.VISIBLE);
            }
        }

        Fragment fragment = fragments.get(i);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        fragments.get(i).onPause(); // 暂停当前tab
        //获取当前radiobutton选中的fragment
        fragments.get(i).onStop(); // 暂停当前tab

        if(fragment.isAdded()){
            fragment.onStart(); // 启动目标tab的onStart()
            fragment.onResume(); // 启动目标tab的onResume()
        }else{
            //添加进回退栈 (返回退回上级fragment)
            ft.add(R.id.container, fragment);
        }
        showTab(i); // 显示目标tab
        ft.commit();

        oldRBT = i;
    }
    /**
     * 切换tab
     * @param idx 当前RadioButton选中的位置
     */
    private void showTab(int idx){
        for(int i = 0; i < fragments.size(); i++){
            //获取当前选中的位置的fragment
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = fragmentManager.beginTransaction();

            if(idx == i){
                ft.show(fragment);
            }else{
                ft.hide(fragment);
            }
            ft.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        //防止切换中帐户时,诶有进入聊天室出现数据错乱
        isChatActivity=sp.getString("isChatActivity","0");
        SuperLiveApplication.imApi.setOneMessageListener(new ImApi.OneMessage() {
            @Override
            public void Messagelist(String from, String body, String type2) {
                Log.e("IM测试MainActivity", from + "/" + body + "/" + type2);
                String[] strs = from.split("@");
                String otheruserid = strs[0];
                if (type2.equals("") || type2.equals("share") || type2.equals("notify_image_url") || type2.equals("notify_new_fans")) {
                    //3 接收到数据时添加到数据库
                    if (type2.equals("")) {//正常聊天为"
                        content = body;
                        type = "2";
                    } else if (type2.equals("share")) {//粉丝分享进入房间
                        Gson json = new Gson();
                        BaseBean info = json.fromJson(body, BaseBean.class);
                        match_id = info.match_id;
                        room_id = info.room_id;
                        qiutan_id = info.qiutan_id;
                        is_private = info.is_private;
                        type = "4";
                    } else if (type2.equals("notify_image_url")) {//系统消息
                        Gson json = new Gson();
                        BaseBean info = json.fromJson(body, BaseBean.class);
                        content = info.text_url;
                        systemimg_url = info.img_url;
                        intent_url = info.intent_url;
                        text = info.text_url;
                        type = "3";
                    } else if (type2.equals("notify_new_fans")) {//粉丝关注
                        content = body;
                        type = "1";
                    }
                    getotherInfo(otheruserid);
                }
            }

            @Override
            public void chatMessage(String from, String body) {

            }

            @Override
            public void groupMessage(ChatInfo info) {

            }
        });
        //红点检测
        getisHongdian();
    }

    private void getisHongdian() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(MainActivity.this).httpPost(Preference.redpoint, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                    }
                    @Override
                    public void onSuccess(String json) {
                        Log.e("红点", json);
                        Gson gson = new Gson();
                        redjavabean Bean = gson.fromJson(json, redjavabean.class);
                        if (Bean.status.equals("_0000")) {
                            mission = Bean.RedPoint.mission;
                            asset = Bean.RedPoint.asset;
                            if (sp.getInt("hongdian", 0) < 1 && mission.equals("0") && asset.equals("0")) {
                                message.setVisibility(View.GONE);
                            } else {
                                message.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    //根据对方传来的userid获取对方的信息
    private void getotherInfo(final String otheruserid) {
        Log.e("type类型",type+"   PPPPP");
        FormBody.Builder build=new FormBody.Builder();
        build.add("userid", otheruserid)
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(this).httpPost(Preference.Getuser, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {

                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("获取个人信息", json);
                        Gson gson = new Gson();
                        BaseBean Bean = gson.fromJson(json, BaseBean.class);
                        if (Bean.status.equals("_0000")) {
                            otherimg = Bean.image;
                            otherlevel = Bean.level;
                            othernickname = Bean.nick_name;
                            long time = System.currentTimeMillis();
                            //默认通知是0,代表没有读
                            if (type.equals("4")) {
                                content = othernickname + "欢迎您进入直播间";
                            }
                            //相等  说明这个信息我不能获取,因为此时我在私聊界面
                            Log.e("isChatActivity", isChatActivity + ",,," + otheruserid + sp.getString(Preference.myuser_id, ""));
//----------------------------------------------------------------------------------------------------------------
                            if (!isChatActivity.equals(otheruserid + sp.getString(Preference.myuser_id, ""))) {
                                //插入所有数据表中
                                insert_toall(sp.getString(Preference.myuser_id, ""), otheruserid, othernickname, otherimg, otherlevel,
                                        time, content, "0", type, match_id, room_id, qiutan_id, systemimg_url, intent_url, text,
                                        sp.getString(Preference.headPhoto, ""), sp.getString(Preference.nickname, ""), is_private);
//----------------------------------------------------------------------------------------------------------------
                                //判断是否是正常聊天类型,并且根据我的userid和好友的userid判断是否适配表存在数据
                                if (type.equals("2")) {
                                    if (findNormalData(otheruserid) == true) {
                                        //插入适配列表中(用于通知列表数据源)
                                        insert(sp.getString(Preference.myuser_id, ""), otheruserid, othernickname, otherimg, otherlevel,
                                                time, content, "0", type, match_id, room_id, qiutan_id, systemimg_url, intent_url, text,
                                                sp.getString(Preference.headPhoto, ""), sp.getString(Preference.nickname, ""), is_private);
                                        //通知红点显示
                                        sp.edit().putInt("hongdian", sp.getInt("hongdian", 0) + 1).commit();
                                    } else {//更新适配表中存在的数据
                                        //找那条数据的id
                                        int id = findNormalID(otheruserid);
                                        //通知红点显示
                                        if (findNormalstatus(otheruserid).equals("1")) {
                                            sp.edit().putInt("hongdian", sp.getInt("hongdian", 0) + 1).commit();
                                        }

                                        //更新适配表中存在的数据
                                        updateContent(othernickname, otherlevel, otherimg, content, time, id);
                                    }
                                    if(receListener_2!=null){
                                        receListener_2.adapterMessage();
                                    }
                                }
//----------------------------------------------------------------------------------
                                //判断是否是系统消息
                                if (type.equals("3")) {
                                    if (findSystelData(otheruserid)==true) {
                                        Log.e("系统消息1","00000000");
                                        insert(sp.getString(Preference.myuser_id, ""), otheruserid, othernickname, otherimg, otherlevel,
                                                time, content, "0", type, match_id, room_id, qiutan_id, systemimg_url, intent_url, text,
                                                sp.getString(Preference.headPhoto, ""), sp.getString(Preference.nickname, ""), is_private);
                                        //通知红点显示
                                        sp.edit().putInt("hongdian", sp.getInt("hongdian", 0) + 1).commit();
                                    } else {
                                        Log.e("系统消息2","00000000");
                                        int id = findSystemlID(otheruserid);
                                        if (findsystemstatus(otheruserid).equals("1")) {
                                            sp.edit().putInt("hongdian", sp.getInt("hongdian", 0) + 1).commit();
                                        }
//                                       delete(id);
//                                       insert(sp.getString(Preference.myuser_id, ""), otheruserid, othernickname, otherimg, otherlevel,
//                                               time, content, "0", type, match_id, room_id, qiutan_id, systemimg_url, intent_url, text,
//                                               sp.getString(Preference.headPhoto, ""), sp.getString(Preference.nickname, ""), is_private);
                                        //更新适配表中存在的数据
                                        updateContent(othernickname, otherlevel, otherimg, content, time, id);
                                    }
                                    if(receListener_2!=null){
                                        receListener_2.adapterMessage();
                                    }
                                }
                            }
//----------------------------------------------------------------------------------
                            if (type.equals("4")) {
                                Log.e("改变2", "pppp" + type);
                                insert(sp.getString(Preference.myuser_id, ""), otheruserid, othernickname, otherimg, otherlevel,
                                        time, content, "0", type, match_id, room_id, qiutan_id, systemimg_url, intent_url, text,
                                        sp.getString(Preference.headPhoto, ""), sp.getString(Preference.nickname, ""), is_private);
                                //通知红点显示
                                sp.edit().putInt("hongdian", sp.getInt("hongdian", 0) + 1).commit();
                                if(receListener_2!=null){
                                    receListener_2.adapterMessage();
                                }
                            }
//-----------------------------------------------------------------------------------
                        }else {
                            Toast.makeText(MainActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                            }
                        }
                });
    }

    /**
     * 自定义监听改变通知列表的数据(不包括离线消息)
     */
    private static shuaxinadapter receListener_2;
    public static void setReceiveMessageListener(shuaxinadapter receListener) {
       receListener_2 = receListener;
    }

    @Override
    public void call(int position) {
        squareft.change(position);
    }



    public interface shuaxinadapter{
        void adapterMessage();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            long time = System.currentTimeMillis();
            if((time-oldTime)<2000){
                finish();
            }else{
                Toast.makeText(MainActivity.this, "再按一次返回键将退出", 1).show();
            }
            oldTime = time;
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SuperLiveApplication.imApi.stopIm();
        System.exit(0);
    }



    /**
     * 删除id为的数据记录
     * @param id
     */
    private void delete(int id) {
        try {
            db.deleteById(messagelist.class, id);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新聊天类型的适配表中存在的数据
     * 修改id为position+1的status
     */
    private void updateContent(String nickname,String level,String otherimg,String content,Long time,int id) {
        Log.e("更新",""+nickname+"///"+content);
        try {
            List<messagelist> stus = db.findAll(messagelist.class);
            messagelist stu = stus.get(id-1);
            stu.setContent(content);
            stu.setTime(time);
            stu.setNickname(nickname);
            stu.setLevel(level);
            stu.setImage(otherimg);
            stu.setStatus("0");
            db.update(stu);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找适配表中是否存在我和好友的聊天数据normal类型
     */
    private boolean findNormalData(String otheruserid) {
        boolean flag=true;
        try {
            List<messagelist> users = db.selector(messagelist.class)
                    .where("my_userid", "=", sp.getString(Preference.myuser_id, ""))
                    .and("userid", "=", otheruserid)
                    .and("type", "=", "2")
                    .findAll();
            Log.e("数据重复", users.toString() + otheruserid + "  " + sp.getString(Preference.myuser_id, "")+"----000000");
            if(users == null || users.size() == 0){
                //true表示需要建行数据插入适配表,之前不存在
                flag=true;
            } else{
                //false表示需要将存在的数据更新内容
                flag=false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    private boolean findSystelData(String otheruserid) {
        boolean flag=true;
        try {
            List<messagelist> users = db.selector(messagelist.class)
                    .where("my_userid", "=", sp.getString(Preference.myuser_id, ""))
                    .and("userid", "=", otheruserid)
                    .and("type", "=", "3")
                    .findAll();

            if(users == null || users.size() == 0){
                //true表示需要建行数据插入适配表,之前不存在
                flag=true;
            } else{
                //false表示需要将存在的数据更新内容
                flag=false;
            }
            Log.e("数据重复系统消息", users.size()+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    /**
     * 查找适配表中是否存在我和好友的聊天数据normal类型
     */
    private int findNormalID(String otheruserid) {
        int id = 0;
        try {
            List<messagelist> users = db.selector(messagelist.class)
                    .where("my_userid", "=", sp.getString(Preference.myuser_id, ""))
                    .and("userid", "=", otheruserid)
                    .and("type", "=", "2")
                    .findAll();

            if(users == null || users.size() == 0){

            } else{
                id=users.get(0).getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    private int findSystemlID(String otheruserid) {
        int id = 0;
        try {
            List<messagelist> users = db.selector(messagelist.class)
                    .where("my_userid", "=", sp.getString(Preference.myuser_id, ""))
                    .and("userid", "=", otheruserid)
                    .and("type", "=", "3")
                    .findAll();

            if(users == null || users.size() == 0){

            } else{
                id=users.get(0).getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
    /**
     * 查找适配表中根据id查找ststus
     */
    private String findNormalstatus(String otheruserid) {

        String status="";
        try {
            List<messagelist> users = db.selector(messagelist.class)
                    .where("my_userid", "=", sp.getString(Preference.myuser_id, ""))
                    .and("userid", "=", otheruserid)
                    .and("type", "=", "2")
                    .findAll();

            if(users == null || users.size() == 0){

            } else{
                status=users.get(0).getStatus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
    private String findsystemstatus(String otheruserid) {

        String status="";
        try {
            List<messagelist> users = db.selector(messagelist.class)
                    .where("my_userid", "=", sp.getString(Preference.myuser_id, ""))
                    .and("userid", "=", otheruserid)
                    .and("type", "=", "3")
                    .findAll();

            if(users == null || users.size() == 0){

            } else{
                status=users.get(0).getStatus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
    /**
     * 插入适配数据
     */
    private void insert(String my_userid,String userid,String nickname,
                        String image,String level, long time,String content,String status,String type,
                        String match_id,String room_id,String qiutan_id,String img_url,String intent_url,String text,String my_image,String mynick_name,String is_private) {
        Log.e("insert执行",""+nickname+"///"+content+"///"+image);
        try {
            messagelist person=new messagelist();
            person.setMy_userid(my_userid);
            person.setUserid(userid);
            person.setLevel(level);
            person.setNickname(nickname);
            person.setImage(image);
            person.setTime(time);
            person.setContent(content);
            person.setStatus(status);
            person.setType(type);
            person.setMatch_id(match_id);
            person.setRoom_id(room_id);
            person.setQiutan_id(qiutan_id);
            person.setImg_url(img_url);
            person.setIntent_url(intent_url);
            person.setText(text);
            person.setMy_img(my_image);
            person.setIs_private(is_private);
            person.setMy_nickname(mynick_name);
            db.save(person);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入总表数据
     */
    private void insert_toall(String my_userid,String userid,String nickname,
                              String image,String level, long time,String content,String status,String type,
                              String match_id,String room_id,String qiutan_id,String img_url,String intent_url,String text,String my_image,String mynick_name,String is_private) {
        Log.e("插入总表",nickname+"__"+img_url+"___"+type);
        try {
            messagelistAll person=new messagelistAll();
            person.setMy_userid(my_userid);
            person.setUserid(userid);
            person.setLevel(level);
            person.setNickname(nickname);
            person.setImage(image);
            person.setTime(time);
            person.setContent(content);
            person.setStatus(status);
            person.setType(type);
            person.setMatch_id(match_id);
            person.setRoom_id(room_id);
            person.setQiutan_id(qiutan_id);
            person.setImg_url(img_url);
            person.setIntent_url(intent_url);
            person.setText(text);
            person.setMy_img(my_image);
            person.setIs_private(is_private);
            person.setMy_nickname(mynick_name);
            db_all.save(person);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //退出登录处理
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags())!=0){
            finish();
            ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
            manager.killBackgroundProcesses(getPackageName());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
