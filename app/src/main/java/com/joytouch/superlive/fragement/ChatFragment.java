package com.joytouch.superlive.fragement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.GuessRankActivity;
import com.joytouch.superlive.adapter.LiveChatAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.app.SuperLiveApplication;
import com.joytouch.superlive.interfaces.FullScreenListener;
import com.joytouch.superlive.interfaces.SelectorAchor;
import com.joytouch.superlive.javabean.AnchorInfo;
import com.joytouch.superlive.javabean.ChatInfo;
import com.joytouch.superlive.javabean.GiftVo;
import com.joytouch.superlive.javabean.RoomBank;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.widget.AnchorInfroDialog;
import com.joytouch.superlive.widget.CircleImageView;
import com.joytouch.superlive.widget.GiftShowManager;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.joytouch.superlive.widget.PullableListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yj on 2016/4/11.
 * 聊天
 */
public class ChatFragment extends BaseFragment implements View.OnClickListener {
    private  View view;
    private CircleImageView head;
    private ImageView room_rank;
    private ImageView userhead1;
    private ImageView userhead2;
    private ImageView userhead3;
    private PullToRefreshLayout prl;
    private PullableListView plv;
    private LiveChatAdapter adapter;
    private String rommid = "";
    private TextView anchorname;
    private List<RoomBank> roomRanks;

    private int type=1;
    private List<ChatInfo> list;
    private String matchid;
    private LinearLayout ranktopll;
    private GestureDetector mGestureDetector;
    private int verticalMinDistance = 50;
    private int minVelocity         = 100;
    private  boolean isListviewScroll = false;//决定左右滑动时,listview不上下滑动,屏蔽掉
    private int postion;
    private int anchorAllCount;
    private String isAnchor="";
    AnchorInfo info ;
    private  boolean isScroll = true;
    private boolean isPrivate ;
    private LinearLayout gift_con;
    private GiftShowManager giftManger;
    private List<View> viewlist=new ArrayList<>();
    private CircleImageView img_1,img_2,img_3,img_4,img_5,img_6,img_7,img_8,img_9,img_10;
    private TextView online;
    private String onlinenum;
    private ImageView top_1;
    private ImageView top_2;
    private ImageView top_3;
    public void setIsScroll(boolean isScroll) {
        this.isScroll = isScroll;
        plv.setSelection(list.size() - 1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
       matchid =  bundle.getString("id");
        isAnchor = bundle.getString("isAnchor");
        onlinenum = bundle.getString("online");
        if ("yes".equals(isAnchor)){
            info = (AnchorInfo) bundle.getSerializable("info");
            type = 4;
            Log.e("userid", info.getAnchorname());
        }
        if("1".equals(bundle.getString("isPrivate"))){
            isPrivate = true;
        }else {
            isPrivate = false;
        }
        roomRanks = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, null);
        initView();
        if ("yes".equals(isAnchor)){
            changeAchor(info,0);
        }
        return view;
    }

    private void initView() {
        img_1=(CircleImageView)view.findViewById(R.id.img_1);
        img_2=(CircleImageView)view.findViewById(R.id.img_2);
        img_3=(CircleImageView)view.findViewById(R.id.img_3);
        img_4=(CircleImageView)view.findViewById(R.id.img_4);
        img_5=(CircleImageView)view.findViewById(R.id.img_5);
        img_6=(CircleImageView)view.findViewById(R.id.img_6);
        img_7=(CircleImageView)view.findViewById(R.id.img_7);
        img_8=(CircleImageView)view.findViewById(R.id.img_8);
        img_9=(CircleImageView)view.findViewById(R.id.img_9);
        img_10=(CircleImageView)view.findViewById(R.id.img_10);
        viewlist.add(img_1);
        viewlist.add(img_2);
        viewlist.add(img_3);
        viewlist.add(img_4);
        viewlist.add(img_5);
        viewlist.add(img_6);
        viewlist.add(img_7);
        viewlist.add(img_8);
        viewlist.add(img_9);
        viewlist.add(img_10);
        online = (TextView) view.findViewById(R.id.online);

        ranktopll = (LinearLayout) view.findViewById(R.id.topcontainer);
        head = (CircleImageView) view.findViewById(R.id.chat_anchor_head);
        room_rank = (ImageView) view.findViewById(R.id.room_rank);
        anchorname = (TextView) view.findViewById(R.id.anchor_name);
        top_1=(ImageView)view.findViewById(R.id.top_1);
        top_2=(ImageView)view.findViewById(R.id.top_2);
        top_3=(ImageView)view.findViewById(R.id.top_3);
        prl = (PullToRefreshLayout) view.findViewById(R.id.prl);
        gift_con=(LinearLayout)view.findViewById(R.id.gift_con);
        giftManger = new GiftShowManager(getContext(), gift_con);
//        giftManger.showGift();//开始显示礼物
        plv = (PullableListView) view.findViewById(R.id.plv);
        prl.setCanPullDown(false);
        prl.setCanPullUp(false);
        head.setOnClickListener(this);
        room_rank.setOnClickListener(this);
        list = new ArrayList<>();
        adapter = new LiveChatAdapter(list,getActivity());
        plv.setAdapter(adapter);
        //设置top的点击事件
        for(int i= 0;i<ranktopll.getChildCount();i++){
            final int finalI = i;
            ranktopll.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(roomRanks.size()>0&&finalI<roomRanks.size()){
                        if(null!=roomRanks.get(finalI)){
                            String userid = (String) SPUtils.get(getActivity(),Preference.myuser_id,"",Preference.preference);
                            if(!TextUtils.isEmpty(roomRanks.get(finalI).getUseid())) {
                                if (userid.equals(roomRanks.get(finalI).getUseid())) {
                                    userDetail(4, roomRanks.get(finalI).getUseid());
                                } else {
                                    if(isPrivate){
                                        userDetail(5, roomRanks.get(finalI).getUseid());
                                    }else {
                                        userDetail(3, roomRanks.get(finalI).getUseid());
                                    }
                                }
                            }
                        }

                    }

                }
            });
        }
        /**
         * listview添加手势监听,用于左右滑动,屏蔽listview上下滑动事件,切换房间
         */
        if (!"yes".equals(isAnchor)){
            mGestureDetector = new GestureDetector(getActivity(), new MyOnGestureListener());
            plv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mGestureDetector.onTouchEvent(event);
                    LogUtils.e("s---------",""+isListviewScroll   );
                    return isListviewScroll;
                }
            });
        }
        plv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(i+i1==i2){
                    isScroll = true;
                }else {
                    isScroll = false;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.chat_anchor_head:
                AnchorInfroDialog dialog = new AnchorInfroDialog(getActivity());
                LogUtils.e("ssssss",""+type);
                dialog.setType(type);
                dialog.setUserid(Preference.zhubo_id);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        FullScreenListener listener = (FullScreenListener) getActivity();
                        listener.fullScren(false);
                    }
                });
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        FullScreenListener listener = (FullScreenListener) getActivity();
                        listener.fullScren(true);
                    }
                });
                dialog.show();
                break;
            case R.id.room_rank:
                Intent intent = new Intent(getActivity(), GuessRankActivity.class);
                intent.putExtra("id",rommid);
                startActivity(intent);
                break;
        }
    }
    Handler room = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AnchorInfo info = (AnchorInfo) msg.getData().getSerializable("info");
            if(getActivity()!=null) {
               SuperLiveApplication.imApi.joinRoom(info.getRoomid(), (String) SPUtils.get(getActivity(), Preference.nickname, "", Preference.preference));
                SPUtils.put(getActivity(),matchid,rommid,Preference.matchroom );
            }
        }
    };
    //切换主播
    public void changeAchor(AnchorInfo info,int postion){
        room.removeCallbacksAndMessages(null);
        if(roomRanks!=null){
            roomRanks.clear();
        }
        if(ranktopll!=null) {
            for (int i = 0; i < 10; i++) {
                ((CircleImageView) ranktopll.getChildAt(i).findViewWithTag("tag")).setImageResource(R.drawable.head_empty);
            }
        }
        //切换聊天室聊天链接
        if(!TextUtils.isEmpty(rommid)) {
            SuperLiveApplication.imApi.leaveRoom(rommid);
            list.clear();
        }
        Message msg = new Message();
        Bundle data = new Bundle();
        data.putSerializable("info",info);
        msg.setData(data);
        room.sendMessageDelayed(msg,1500);
        if (adapter!=null){

            adapter.notifyDataSetChanged();
        }
        Log.e("ssss--ss",head+"");
        //切换主播信息
        if(head!=null) {
            ImageLoader.getInstance().displayImage(Preference.photourl + "200x200/" + info.getHeaderimg(), head, ImageLoaderOption.optionsHeader);
        }
        if(anchorname!=null) {
            anchorname.setText(info.getAnchorname());
        }
        Preference.lotteryMoney = info.getBetting();
        rommid = info.getRoomid();
        Preference.room_id = info.getRoomid();
        Preference.zhubo_id = info.getAnchorid();
        this.postion = postion;
        if (online!=null){
            online.setText(info.getOnline()+"人在线");
        }


    }
    //用户详情
    public void userDetail(int i,String userid){
        AnchorInfroDialog dialog = new AnchorInfroDialog(getActivity());
        dialog.setType(i);
        dialog.setUserid(userid);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                FullScreenListener listener = (FullScreenListener) getActivity();
                listener.fullScren(false);
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                FullScreenListener listener = (FullScreenListener) getActivity();
                listener.fullScren(true);
            }
        });
        dialog.show();

    }
    String lastuid = "";
    //接收的消息
    public void message(ChatInfo info){
        if (info.getType().equals("11") && !info.getGift_id().equals("4")){
            GiftVo vo = new GiftVo();
            vo.setUserId(info.getNickname());
            vo.setNum(Integer.parseInt(info.getCount()));
            vo.setImage(info.getImage());
            vo.setGift_id(info.getGift_id());
            vo.setMsg(info.getContent());
            vo.setIdent(info.getIdent());
            giftManger.addGift(vo);
            giftManger.showGift();//开始显示礼物
        }else if (info.getType().equals("11") && info.getGift_id().equals("4")){
            //显示红包dialog
//            Toast.makeText(getContext(), "红包", Toast.LENGTH_SHORT).show();
        }
        if(!Preference.room_id.equals(info.getRoomid())){
            return;
        }
        if(list == null){
            return;
        }
        list.add(info);
        adapter.notifyDataSetChanged();
        if(isScroll) {
            plv.setSelection(list.size() - 1);
        }

    }
    public void roomTOp10(String roomid,List<RoomBank> list){
        if(!Preference.room_id.equals(roomid)){
            return;
        }
        if(ranktopll==null||ranktopll.getChildCount()==0){
            return;
        }
        if(roomRanks!=null){
            roomRanks.clear();
        }
        roomRanks = list;
        if (roomRanks.size()>=3){
            top_1.setVisibility(View.VISIBLE);
            top_2.setVisibility(View.VISIBLE);
            top_3.setVisibility(View.VISIBLE);
        }else if (roomRanks.size()==2){
            top_1.setVisibility(View.VISIBLE);
            top_2.setVisibility(View.VISIBLE);
        }else if (roomRanks.size()==1){
            top_1.setVisibility(View.VISIBLE);
        }
        for(int i = 0;i<roomRanks.size();i++){
            ImageLoader.getInstance().displayImage(Preference.photourl+"40x40/"+roomRanks.get(i).getImage(),(CircleImageView)viewlist.get(i),ImageLoaderOption.optionsHeader);
        }
        ImageLoader.getInstance().clearMemoryCache();
        System.gc();
    }


    public void movetoleft() {
        //左
        SelectorAchor achor = (SelectorAchor) getActivity();
        postion--;
        if(postion>=0){
            achor.anchor(postion);
        }else{
            postion = 0;
        }
    }

    public void movetoright() {
        //you
        SelectorAchor achor = (SelectorAchor) getActivity();
        postion++;
        LogUtils.e("s++++++++",""+postion);
        if(postion<=(anchorAllCount-1)){
            achor.anchor(postion);
        }else{
            postion=anchorAllCount-1;
        }
    }

    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean flag=true;

        @Override
        public boolean onDown(MotionEvent e) {
            return super.onDown(e);
        }

        //1.手势起点的移动事件 2.当前手势点的移动事件 3.每秒x轴方向移动 4.每秒y轴方向移动的
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1!=null && e2!=null){

                if(e1.getX()- e2.getX() > verticalMinDistance && Math.abs(velocityX) >
                        minVelocity && Math.abs(e1.getY()-e2.getY())<200 ) {

                    isListviewScroll =true;
                  movetoleft();
                    LogUtils.e("s-----------",""+postion);

                }else{
                    isListviewScroll = false;
                }
                if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity
                        && Math.abs(e1.getY()-e2.getY())<200){
                    isListviewScroll =true;
                    movetoright();
                }else{
                    isListviewScroll =false;
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }




        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //左上正数,右下负数,(Math.abs(_mPosX-_mCurPosX) > 30)
            if ( Math.abs(distanceY)>40 && Math.abs(distanceX)<20){
//                Log.e("手势滑动","上下");
                flag=false;
            }
            else if (Math.abs(distanceX)>40 && Math.abs(distanceY)<20 ){
//                Log.e("手势滑动","左右");
                flag=true;
            }
            return flag;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
//            return super.onSingleTapUp(e);
            return flag;
        }
    }
    public void   anchorSize(int count){
        anchorAllCount = count;
        LogUtils.e("chatfragmet=+===",""+count);
    }
    public void ondestory(){
        room.removeCallbacksAndMessages(null);
    }
    public void joinRoom(int status){
        LogUtils.e("=======","   "+status);
        if(status==1&&getActivity()!=null){
            LogUtils.e("=======","   "+list.size());
            if(list.size()>0){
                SuperLiveApplication.imApi.joinRoom(rommid, "##"+(String) SPUtils.get(getActivity(), Preference.nickname, "", Preference.preference));
            }else {
                SuperLiveApplication.imApi.joinRoom(rommid, (String) SPUtils.get(getActivity(), Preference.nickname, "", Preference.preference));
            }

        }
    }
    public void online(String count,String roomid){
        if(rommid.equals(roomid)&&online!=null) {
            online.setText(count + "人在线");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SuperLiveApplication.imApi.leaveRoom(rommid);
    }
}
