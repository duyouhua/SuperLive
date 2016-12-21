package com.joytouch.superlive.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cjzhibo.im.ImApi;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.ChatAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.app.SuperLiveApplication;
import com.joytouch.superlive.interfaces.changeStateCallback;
import com.joytouch.superlive.javabean.Chat;
import com.joytouch.superlive.javabean.ChatInfo;
import com.joytouch.superlive.javabean.messagelist;
import com.joytouch.superlive.javabean.messagelistAll;
import com.joytouch.superlive.javabean.talkOneTone;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.DBHelper.AccounDBHelper;
import com.joytouch.superlive.utils.EmojiReplace;
import com.joytouch.superlive.utils.KeyboardUtil;
import com.joytouch.superlive.utils.xutil.XUtil;
import com.joytouch.superlive.widget.PullToRefreshLayout;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

// 聊天好友列表
//        创建一个数据库 方法一：
//                 accountUtils dbut=  new accountUtils(ChatActivity.this);
//                 dbut .create();
//                 dbut.insert();
//                 dbut.update();
//                 dbut.delete();
//                 dbut.query();


/**
 * 注意:
 * 好友的信息user_id,otherimg,othernickname都已经从其他界面接收过来了,但是MessageListActivity没有传,需要修改
 * 从个人主页进入此页面属于发消息
 * 从MessageListActivity进入此页面属于回复信息
 */
public class ChatActivity extends Activity implements PullToRefreshLayout.OnRefreshListener,View.OnClickListener,changeStateCallback {
    private PullToRefreshLayout refreshLayout;
    private ListView listView;
    private ImageView iv_finish;
    private TextView tv_title;
    private ImageView iv_right;
    private EditText et_input;
    private ImageView iv_add;
    private List<Chat> chats;
    private SharedPreferences sp;
    private  DbManager db;
    private AccounDBHelper helper;
    private List<talkOneTone> List=new ArrayList<>();
    private ChatAdapter adapter;
    private  Long preTime=0l;
    private String otherid;//模仿从上一级传过来的好友userid
    private List<talkOneTone> adapterList=new ArrayList<>();//记录所有聊天的数据到数据库
    private  List<talkOneTone> alllist=new ArrayList<talkOneTone>();//有关此次聊天的所有记录
    private  List<talkOneTone> itemlist=new ArrayList<talkOneTone>();//第一次加载的记录
    private int Oversize=20;//分页加载20条
    private int end;//分页加载后
    private int start;//分页加载前
    private String othernickname;
    private String otherimg;
    private String myimg;
    private DbManager db2;
    private DbManager db_all;
    List<talkOneTone>  zblist=new ArrayList<>();//主要用于存储离线消息
    String ok_content="";
    long ok_time;
    private String ok_otherid="";
    private String other_level="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        myimg=sp.getString(Preference.headPhoto, "");
        //好友的信息
        otherid=this.getIntent().getStringExtra("otherid");
        othernickname=this.getIntent().getStringExtra("othernickname");
        otherimg=this.getIntent().getStringExtra("otherimg");
        other_level=this.getIntent().getStringExtra("other_level");
        setContentView(R.layout.activity_chat);
        //1表示在聊天界面
        sp.edit().putString("isChatActivity",otherid+sp.getString(Preference.myuser_id,"")).commit();
        Log.e("isChatActivity_chat", otherid + sp.getString(Preference.myuser_id, ""));
        SuperLiveApplication.imApi.setOneMessageListener(new ImApi.OneMessage() {
            @Override
            public void Messagelist(String from, String body, String type2) {

            }

            @Override
            public void chatMessage(String from, String body) {
                Log.e("adapterList", from + "/" + body);
                String[] strs = from.split("@");
                String otherud = strs[0];
                if (otherud.equals(otherid)) {
                    //退出后,preTime被销毁,会走else,time2相当于从对面拿来的时间
                    long time2 = System.currentTimeMillis();
                    if (time2 - preTime < 600000) {
                        time2 = 0;
                    } else {
                        preTime = time2;
                    }

                    insert(sp.getString(Preference.myuser_id, ""), sp.getString(Preference.nickname, ""),
                            sp.getString(Preference.headPhoto, ""), otherid, othernickname,
                            otherimg, time2, body, "1", "0");


                    //上下status和left_right一致
                    talkOneTone info = new talkOneTone();
                    info.setMy_userid(sp.getString(Preference.myuser_id, ""));
                    info.setMy_img(sp.getString(Preference.headPhoto, ""));
                    info.setMy_nickname(sp.getString(Preference.nickname, ""));
                    info.setContent(body);
                    info.setTime(time2);
                    info.setStatus("1");
                    info.setLeft_right("0");
                    info.setOther_userid(otherid);
                    info.setImage(otherimg);
                    info.setNickname(othernickname);

                    ok_content = body;
                    ok_time = time2;
                    ok_otherid = otherid;

//                    sp.edit().putString("ok_content",body)
//                            .putString("ok_otherid",otherid)
//                            .putLong("ok_time",time2).commit();

                    adapterList.add(info);
                    itemlist.add(info);
                    adapter.notifyDataSetChanged();
                    listView.setSelection(itemlist.size() - 1);
                }

            }

            @Override
            public void groupMessage(ChatInfo info) {
            }
        });

        //获取所有的记录条数,本地存储一个值,insert+1,delect-1;
        //创建一个数据库，方法二
        //数据库里面表的创建的时间，只有在你对数据库里面的操作涉及到这张表的操作时，会先判断当前的表是否存在，如果不存在，才会创建一张表，如果存在，
        // 才会进行相应的CRUD操作，但是只要我们想进行一张表的CRUD操作，我们必须先执行下面的2步
        DbManager.DaoConfig daoConfig= XUtil.getDaoConfig();
        db = x.getDb(daoConfig);
        //创建一个表存储需要在列表中填充数据的表   MessageTable
        DbManager.DaoConfig daoConfig_= XUtil.getMessageListDaoConfig();
        db2 = x.getDb(daoConfig_);
        //创建一个数据库存储所有的数据  MessageTableAll
        DbManager.DaoConfig daoConfig_2 = XUtil.getMessageList();
        db_all = x.getDb(daoConfig_2);
        //第一步,找到关于我和好友的所有聊天数据
        findaboutAlll();
        //刚进入查询所有数据(用于保存使用)
        queryAll();
//        selector();
//        delete();
//        updateOne("0",1);
//        updateGroup("0",1);
//        deleteAll();
//        delete_table();
        initView();
//        ConfigUtils.chagevegive(ChatActivity.this);
    }

    private void initView() {
        adapter = new ChatAdapter(itemlist, this,otherimg,myimg);
        refreshLayout = (PullToRefreshLayout) this.findViewById(R.id.refresh);
        listView = (ListView) this.findViewById(R.id.listView);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText(othernickname);
        iv_right = (ImageView) this.findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setBackgroundResource(R.drawable.friend_info);
        iv_right.setOnClickListener(this);
        et_input = (EditText) this.findViewById(R.id.et_input);
        //设置EditText的显示方式为多行文本输入
        et_input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //文本显示的位置在EditText的最上方
//        et_input.setGravity(Gravity.TOP);
        et_input.setSingleLine(false);
        //水平滚动设置为False
        et_input.setHorizontallyScrolling(false);
        et_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!et_input.getText().toString().equals("")) {
                    String mymessage= null;
                    try {
                        mymessage = EmojiReplace.emojiConvert1(et_input.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //软键盘右下角的发送键，点击发评论
                    SuperLiveApplication.imApi.sendUserMsg(otherid,mymessage );
                    saveToDatabase(et_input.getText().toString(), sp.getString(Preference.myuser_id, ""),
                            sp.getString(Preference.headPhoto, ""), sp.getString(Preference.nickname, ""), System.currentTimeMillis());
                    et_input.setText("");
                }
                return true;
            }
        });

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setCanPullUp(false);

//---------------????数据带过来以后,出现问题
        //根据我的userid和他的userid从总表中查找到所有的聊天记录显示.将离线消息添加到我的表中
        // 再将总表关于我和他的,离线消息全部删除
        // 默认left_right为0
        findsyatemListabout(otherid);
        itemlist.addAll(zblist);

        listView.setAdapter(adapter);
        listView.setSelection(itemlist.size() - 1);
        //适配表的数据状态改变,红点消失
        findNormalID(otherid);

        iv_add=(ImageView) this.findViewById(R.id.iv_add);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this, "清除数据", Toast.LENGTH_SHORT).show();
                delete_table();
                adapter.notifyDataSetChanged();
            }
        });
        /*监听列表的滑动状态:暂时用不到
         * SCROLL_STATE_FLING 时让图片不显示，提高滚动性能让滚动小姑更平滑
         * SCROLL_STATE_IDLE 时显示当前屏幕可见的图片*/
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            //滑动的状态变化
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        // 手指触屏拉动准备滚动，只触发一次        顺序: 1
                        KeyboardUtil.closeKeybord(et_input, ChatActivity.this);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        // 持续滚动开始，只触发一次                顺序: 2
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 整个滚动事件结束，只触发一次            顺序: 4

                        break;
                    default:
                        break;

                }

            }

            //正在滑动
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // 一直在滚动中，多次触发                          顺序: 3

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConfigUtils.addActivity(this);
    }


    /**
     * 查找适配表中是否存在我和好友的聊天数据normal类型
     */
    private int findNormalID(String otheruserid) {

        int id = 0;
        try {
            List<messagelist> users = db2.selector(messagelist.class)
                    .where("my_userid", "=", sp.getString(Preference.myuser_id, ""))
                    .and("userid", "=", otheruserid)
                    .and("type", "=", "2")
                    .findAll();

            if(users == null || users.size() == 0){

            } else{
                id=users.get(0).getId();
                //修改适配表的关于我和他的离线消息状态
                updateadapterContent("1",id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
    /**
     * 从总表中查找我的离线正常消息
     * @param otheruserid
     * @return
     */
    private void findsyatemListabout(String otheruserid) {
        zblist.clear();
        Log.e("zblistde",zblist.size()+">>>>1");
        try {
            List<messagelistAll> users = db_all.selector(messagelistAll.class)
                    .where("my_userid", "=", sp.getString(Preference.myuser_id, ""))
                    .and("userid", "=", otheruserid)
                    .and("type", "=", "2")
                    .findAll();
            if(users == null || users.size() == 0){

            } else{
                for (int i=0;i<users.size();i++){
                    talkOneTone info = new talkOneTone();
                    info.setMy_userid(sp.getString(Preference.myuser_id, ""));
                    info.setMy_img(sp.getString(Preference.headPhoto, ""));
                    info.setMy_nickname(sp.getString(Preference.nickname, ""));
                    info.setContent(users.get(i).getContent());
                    info.setTime(users.get(i).getTime());
                    info.setStatus("1");
                    info.setLeft_right("0");
                    info.setOther_userid(users.get(i).getUserid());
                    info.setImage(users.get(i).getImage());
                    info.setNickname(users.get(i).getNickname());
                    zblist.add(info);
                    adapterList.add(info);
                    //将查到的离线消息添加到私聊数据库中
                    insert(info.getMy_userid(),info.getMy_nickname(),info.getMy_img(),otherid, othernickname,
                            otherimg, info.getTime(), info.getContent(), "1", "0");
                    //将总表中删除关于我和他的离线消息
                    Log.e("cefoid测试id",users.get(i).getId()+"");
                    delete(users.get(i).getId());
                }
            }
            Log.e("zblistde",zblist.size()+">>>>2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            //查找最后一条数据好友发来的数据
        }
        return super.onKeyDown(keyCode, event);
    }



    /**
     * 每发一句聊天就保存数据库,并且动态显示
     * @param content
     * @param my_userid
     * @param my_img
     * @param my_nickname
     * @param time
     */
    private void saveToDatabase(final String content, final String my_userid, final String my_img, final String my_nickname, long time) {
        if (time-preTime<60000){
            time=0;
        }else{
            preTime=time;
        }
        //上下status和left_right一致
        final talkOneTone info=new talkOneTone();
        info.setMy_userid(my_userid);
        info.setMy_img(my_img);
        info.setMy_nickname(my_nickname);
        info.setContent(content);
        info.setTime(time);
        info.setStatus("3");
        info.setLeft_right("1");
        info.setId(adapterList.size() + 1);
        info.setOther_userid(otherid);
        info.setImage(otherimg);
        info.setNickname(othernickname);
        adapterList.add(info);
        itemlist.add(info);
        adapter.notifyDataSetChanged();
        listView.setSelection(itemlist.size() - 1);
        insert(my_userid, my_nickname, my_img, otherid, othernickname, otherimg, time, content, "3", "1");
        if (SuperLiveApplication.imApi.getStatus()==1){//发送成功
            Log.e("发送状态",SuperLiveApplication.imApi.getStatus()+"...");
            info.setStatus("1");
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            listView.setSelection(itemlist.size() - 1);
            //保存状态
            updateOne("1", adapterList.size() - 1);
        }else{//发送失败
            Log.e("发送状态",SuperLiveApplication.imApi.getStatus()+"???");
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    //显示dialog,默认是发送失败状态,其实成功了,但是数据库保存失败状态,此处需要修改
                    info.setStatus("0");
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    listView.setSelection(itemlist.size() - 1);
                    //保存状态
                    updateOne("0", adapterList.size() - 1);
                }
            }, 1000);
        }

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        String my_img=sp.getString(Preference.headPhoto,"");
        if (start>Oversize){
            for(int i=end-1;i>=start;i--){
                itemlist.add(0,alllist.get(i));
            }
        }else if (start<Oversize && start>=0){
            itemlist.addAll(0,alllist);
        }else{
            Toast.makeText(ChatActivity.this, "没有数据了", Toast.LENGTH_SHORT).show();
        }
        end=start;
        start=end-Oversize;
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setSelection(Oversize);
        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.iv_right:
                //需要判断是主播还是他人非主播,没有私信按钮了吧?
                Intent intent=new Intent(ChatActivity.this,OtherUserMessageActivity.class);
                intent.putExtra("user_id",otherid);
                intent.putExtra("othernickname",othernickname);
                intent.putExtra("otherimg",otherimg);
                startActivity(intent);
//                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //0表示不在聊天界面
        sp.edit().putString("isChatActivity", "0").commit();
//        updateGroup("0");
        ConfigUtils.removeActivtity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sp.edit().putString("isChatActivity", "0").commit();
        //根据我的id和对方的id判断适配表中是否有有关记录
        boolean isCreate=findmeAndhe();
        Log.e("添加记录4",isCreate+"__");
        //true表示需要在适配表中创建一个新的记录,false修改内容
        if (isCreate==true){
            //插入适配列表中(用于通知列表数据源)
            if (itemlist.size()!=0){
                insertTomessagelist(sp.getString(Preference.myuser_id, ""), otherid, othernickname, otherimg, other_level,
                        preTime, itemlist.get(itemlist.size() - 1).getContent(), "1", "2", "", "", "", "", "", "",
                        sp.getString(Preference.headPhoto, ""), sp.getString(Preference.nickname, ""), "");

                Log.e("添加记录2", othernickname + "/" + other_level + "/" + preTime + "/" + itemlist.get(itemlist.size() - 1).getContent());
            }
        }else{
            //查找我和我的好友在适配表中的id
          int id=  findNormalID(otherid);
            Log.e("添加记录5", id + "");
            if (itemlist.get(itemlist.size()-1).getLeft_right().equals("0")){
                //我接收到的
                //修改内容
                updateMessagelistContent(othernickname,other_level,otherimg,itemlist.get(itemlist.size()-1).getContent(),
                        itemlist.get(itemlist.size()-1).getTime(),id,"1");
                Log.e("添加记录6", othernickname + "/"+itemlist.get(itemlist.size()-1).getContent());
            }else{
                //我发出的
                updateMessagelistContent(sp.getString(Preference.nickname,""),sp.getString(Preference.level,""),sp.getString(Preference.headPhoto, "")
                        ,itemlist.get(itemlist.size()-1).getContent(),itemlist.get(itemlist.size()-1).getTime(),id,"1");
                Log.e("添加记录7", itemlist.get(itemlist.size()-1).getContent());
            }

        }
        updateGroup("0");
        //发送广播改变适配列表中状态
//        Intent intent2 = new Intent();
//        intent2.setAction("MAIDIAN");
//        Bundle mbundle=new Bundle();
//        mbundle.putLong("ok_time",ok_time);
//        mbundle.putString("ok_content", ok_content);
//        mbundle.putString("ok_otherid", ok_otherid);
//        intent2.putExtras(mbundle);
//        ChatActivity.this.sendBroadcast(intent2);
    }



    @Override
    protected void onPause() {
        super.onPause();
//        sp.edit().putString("isChatActivity", "0").commit();
    }
    /**
     * 更新聊天类型的适配表中存在的数据
     * 修改id为position+1的status
     */
    private void updateMessagelistContent(String nickname,String level,String otherimg,String content,Long time,int id,String status) {
        try {
            List<messagelist> stus = db.findAll(messagelist.class);
            messagelist stu = stus.get(id-1);
            stu.setContent(content);
            stu.setTime(time);
            stu.setNickname(nickname);
            stu.setLevel(level);
            stu.setImage(otherimg);
            stu.setStatus(status);
            db.update(stu);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    /**
     * 插入数据
     * @param my_userid
     * @param my_nickname
     * @param my_img
     * @param other_userid
     * @param nickname
     * @param image
     * @param time
     * @param content
     * @param status
     * @param left_right
     */
    private void insert(String my_userid,String my_nickname,String my_img,
                        String other_userid,String nickname,String image, long time,String content,String status,String left_right) {
        try {
            talkOneTone person=new talkOneTone();
            person.setMy_userid(my_userid);
            person.setMy_nickname(my_nickname);
            person.setMy_img(my_img);
            person.setOther_userid(other_userid);
            person.setNickname(nickname);
            person.setImage(image);
            person.setTime(time);
            person.setContent(content);
            person.setStatus(status);
            person.setLeft_right(left_right);
            db.save(person);
            //db.saveOrUpdate(person);
            //db.saveBindingId(person);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询数据库的全部数据
     */
    private void queryAll() {
        try {
            List<talkOneTone> persons = db.findAll(talkOneTone.class);
            adapterList.addAll(persons);
            Log.e("queryAll", "/" + persons.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 该方法主要是用来进行一些特定条件的查找
     */
    private void selector() {
        try {
            List<talkOneTone> persons = db.selector(talkOneTone.class).where("content", "=", "可以吗").findAll();
            for(talkOneTone person:persons){
                Log.e("person_query","&&"+person.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 该方法的用途就是返回满足sqlInfo信息的所有数据的字段的一个集合。
     */
    private void findaboutAlll() {
        try {
            List<talkOneTone> users = db.selector(talkOneTone.class)
                    .where("my_userid", "=", sp.getString(Preference.myuser_id, ""))
                    .and("other_userid", "=", otherid)
                    .findAll();
            if(users == null || users.size() == 0){
//                Toast.makeText(ChatActivity.this, "暂无聊天内容", Toast.LENGTH_SHORT).show();
                return;//请先调用dbAdd()方法
            } else{
                alllist.addAll(0,users);
                preTime = users.get(alllist.size()-1).getTime();
                end=alllist.size();
                start=end-Oversize;
//                //根据我的id和对方的id判断适配表中是否有有关记录
//                boolean isCreate=findmeAndhe();
//                Log.e("添加记录3",isCreate+"");
//                //true表示需要在适配表中创建一个新的记录
//                if (isCreate==true){
//                    //插入适配列表中(用于通知列表数据源)
//                    insertTomessagelist(sp.getString(Preference.myuser_id, ""), otherid, othernickname, otherimg, other_level,
//                            alllist.get(alllist.size()-1).getTime(), alllist.get(alllist.size()-1).getContent(), "0", "2", "", "", "", "", "", "",
//                            sp.getString(Preference.headPhoto, ""), sp.getString(Preference.nickname, ""), "");
//                    Log.e("添加记录1",othernickname+"/"+other_level+"/"+alllist.get(alllist.size()-1).getTime()+"/"+users.get(alllist.size()-1).getContent());
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //历史记录是否超过5条,分页数目
            if (alllist.size()>Oversize){
                for(int i=start;i<end;i++){
                    itemlist.add(alllist.get(i));
                }
            }else{
                itemlist.addAll(0,alllist);
            }
            end=start;
            start=end-Oversize;
        }
    }

    /**
     * 插入适配数据
     */
    private void insertTomessagelist(String my_userid,String userid,String nickname,
                        String image,String level, long time,String content,String status,String type,
                        String match_id,String room_id,String qiutan_id,String img_url,String intent_url,String text,String my_image,String mynick_name,String is_private) {
        Log.e("insert到适配表执行",""+nickname+"///"+content+"///"+image);
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
     * 适配表中查询号是否有归还于我和他的记录
     * @return
     */
    public boolean findmeAndhe() {
        boolean istrue = false;
        try {
            List<messagelist> users = db.selector(messagelist.class)
                    .where("my_userid", "=", sp.getString(Preference.myuser_id, ""))
                    .and("userid", "=", otherid)
                    .and("type", "=", "2")
                    .findAll();

            if(users == null || users.size() == 0){
                istrue= true;//请先调用dbAdd()方法
            } else{
                istrue= false;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return istrue;
    }

    /**
     * 修改id为position+1的status
     */
    private void updateOne(String status,int position) {
        try {
            List<talkOneTone> stus = db.findAll(talkOneTone.class);
            talkOneTone stu = stus.get(position);
            stu.setStatus(status);
            db.update(stu);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    /**
     * 更新聊天类型的适配表中存在的数据
     * 修改id为position+1的status
     */
    private void updateadapterContent(String status,int id) {
        try {
            List<messagelist> stus = db2.findAll(messagelist.class);
            messagelist stu = stus.get(id-1);
            stu.setStatus(status);
            db2.update(stu);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    /**
     * 需求:status为3的status都变成0。
     */
    private void updateGroup(String ststus) {
        try {
            List<talkOneTone> persons = db.findAll(talkOneTone.class);
            for(talkOneTone Pletter:persons){
                Pletter.setStatus(ststus);
                db.update(Pletter, WhereBuilder.b("status", "=", 3), "status");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改多个参数
     */
    private void updateGroupmore() {
        try {
            List<talkOneTone> persons = db.findAll(talkOneTone.class);
            for(talkOneTone Pletter:persons){
                Pletter.setMy_img("www,12344");
                Pletter.setImage("www.kkk");
                db.update(Pletter, WhereBuilder.b("other_userid", "=", "2000"), "my_img","image");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 该方法主要是根据表的主键进行单条记录的删除
     需求:删除上方person表中id为5的记录
     */
    private void delete(int id) {
        try {
            db_all.deleteById(messagelistAll.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 该方法主要是根据实体bean进行对表里面的一条或多条数据进行删除
     */
    private void delete_more() {
        try {
            talkOneTone person = db.selector(talkOneTone.class).where("my_img", "=", "www.kkk").findFirst();
            db.delete(person);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 该方法主要是用来删除表格里面的所有数据，但是注意：表还会存在，只是表里面数据没有了
     */
    private void deleteAll() {
        try {
            db.delete(talkOneTone.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 该方法主要是根据where语句的条件进行删除操作
     */
    private void delete_sql() {
        try {
            db.delete(talkOneTone.class, WhereBuilder.b("id", "=", "23").and("my_img", "=", "www.kkk"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 该方法是用来删除表
     */
    private void delete_table() {
        try {
            db.dropTable(talkOneTone.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    //重发
    @Override
    public void changestste(final String state, final int position) {
        /**
         * 解决ListView的getChildAt方法返回null  :position- listView.getFirstVisiblePosition()
         */
        for (int i=0;i<itemlist.size();i++){
            listView.getChildAt(position- listView.getFirstVisiblePosition() ).findViewById(R.id.iv_send_error).setVisibility(View.GONE);
            listView.getChildAt(position- listView.getFirstVisiblePosition() ).findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //发送聊天,成功后执行下面方法
                //获取id
                int id = itemlist.get(position).getId();
                SuperLiveApplication.imApi.sendUserMsg(otherid, itemlist.get(position).getContent().toString());
                //保存状态
                updateOne(state, id - 1);
                itemlist.get(position).setStatus(state);
                adapter.notifyDataSetChanged();

            }
        }, 1000);

    }



}
