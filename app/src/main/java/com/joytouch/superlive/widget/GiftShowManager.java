package com.joytouch.superlive.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.GiftVo;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author zhongxf
 * @Description 礼物显示的管理类
 * @Date 2016/6/6.
 * 主要礼物逻辑：利用一个LinkedBlockingQueue来存储所有的礼物的实体类。然后利用Handler的消息机制，每隔一段时间从队列中取一次礼物出来
 * 如果取得礼物为空（队列中没有礼物），那么就延迟一段时间之后再次从队列中取出礼物
 * 如果从队列中取出的礼物不为空，则根据送礼物的人的UserId去寻找这个礼物是否正在显示，如果不在显示，则新建一个，如果正在显示，则直接修改数量
 * <p/>
 * 这个礼物View的管理类中一直存在一个定时器在沦陷礼物的容器下面的所有的礼物的View，当有礼物的View上次的更新时间超过最长显示时间，那么久就移除这个View
 * <p/>
 * 6/7实现：礼物容器中显示的礼物达到两条，并且新获取的礼物和他们两个不一样，那么需要移除一个来显示新的礼物
 * 判断所有的里面的出现的时间，然后把显示最久的先移除掉（需要考虑到线程安全）
 *
 * 6/7实现：定时器的线程会更新View，在获取礼物的时候也会更新View（增加线程安全控制）
 */
public class GiftShowManager {
    static int[] present_1evel = new int[]{R.drawable.present_1,R.drawable.present_2,R.drawable.present_3
            ,R.drawable.present_4,R.drawable.present_5,R.drawable.present_6
            ,R.drawable.present_7,R.drawable.present_8};
    static int[] present_bac = new int[]{R.drawable.gift_bac_blue,R.drawable.gift_bac_red,R.drawable.gift_bac_green};
    private LinkedBlockingQueue<GiftVo> queue;//礼物的队列
    private LinearLayout giftCon;//礼物的容器
    private Context cxt;//上下文

    private TranslateAnimation inAnim;//礼物View出现的动画
    private AnimationSet outAnim;//礼物View消失的动画
    private ScaleAnimation giftNumAnim;//修改礼物数量的动画

    private final static int SHOW_GIFT_FLAG = 1;//显示礼物
    private final static int GET_QUEUE_GIFT = 0;//从队列中获取礼物
    private final static int REMOVE_GIFT_VIEW = 2;//当礼物的View显示超时，删除礼物View

    private Timer timer;//轮询礼物容器的所有的子View判断是否超过显示的最长时间


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_GIFT_FLAG://如果是处理显示礼物的消息,显示礼物
                    GiftVo showVo = (GiftVo) msg.obj;
                    String userId = showVo.getUserId();
                    String countnum=showVo.getNum()+"";
                    int num = showVo.getNum();
                    //根据userid判断是否存在,如果为空,则重新创建一个否则改变数量
                    View giftView = giftCon.findViewWithTag(userId);
                    giftNumAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            handler.sendEmptyMessageDelayed(GET_QUEUE_GIFT, 1000);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });

                    //新的礼物不存在: 判断已有的礼物如果数量大于2,需要移出一个,创建并且显示,否则只需要显示
                    if (giftView == null) {
                        //首先需要判断下Gift ViewGroup下面的子View是否超过两个
                        int count = giftCon.getChildCount();
                        //如果正在显示的礼物的个数超过两个，那么就移除最后一次更新时间比较长的
                        if (count >= 2) {
                            View giftView1 = giftCon.getChildAt(0);
                            TextView nameTv1 = (TextView) giftView1.findViewById(R.id.name);
                            long lastTime1 = (long) nameTv1.getTag();
                            Object tag1 = giftView1.findViewById(R.id.iv_present).getTag();

                            View giftView2 = giftCon.getChildAt(1);
                            TextView nameTv2 = (TextView) giftView2.findViewById(R.id.name);
                            long lastTime2 = (long) nameTv2.getTag();
                            Object tag2 = giftView1.findViewById(R.id.iv_present).getTag();
                            Log.e("tag1tag2",tag1+"   "+tag2);
                            //如果有一个是1
                            if (tag1.equals("1") || tag2.equals("1")){
                                Message rmMsg = new Message();
                                if (tag1.equals("1")){
                                    if (tag2.equals("1")){
                                        if (lastTime1 > lastTime2) {//如果第二个View显示的时间比较长
                                            rmMsg.obj = 1;
                                            rmMsg.what = REMOVE_GIFT_VIEW;
                                            handler.sendMessage(rmMsg);
                                            createview(giftView, userId, countnum, num, showVo);
                                        } else {//如果第一个View显示的时间长
                                            rmMsg.obj = 0;
                                            rmMsg.what = REMOVE_GIFT_VIEW;
                                            handler.sendMessage(rmMsg);
                                            createview(giftView, userId, countnum, num, showVo);
                                        }
                                    }else{
                                        rmMsg.obj = 0;
                                        rmMsg.what = REMOVE_GIFT_VIEW;
                                        handler.sendMessage(rmMsg);
                                        createview(giftView, userId, countnum, num, showVo);
                                    }
                                }else{
                                    if (tag2.equals("1")){
                                        rmMsg.obj = 1;
                                        rmMsg.what = REMOVE_GIFT_VIEW;
                                        handler.sendMessage(rmMsg);
                                        createview(giftView,userId,countnum,num,showVo);
                                    }
                                }
                            }else{
                                handler.sendEmptyMessageDelayed(GET_QUEUE_GIFT, 1000);
                            }
                        }else{
                            createview(giftView,userId,countnum,num,showVo);
                        }
                    } else {//如果送的礼物正在显示（只是修改下数量）
                        queue.poll();
                        //显示礼物的数量
                        final MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.gift_num);
//                        int showNum = (int) giftNum.getTag() + num;
                        int showNum = (int)num;
                        giftNum.setText("X" + (showNum));
                        giftNum.setTag(showNum);

                        TextView tv = (TextView) giftView.findViewById(R.id.name);
                        tv.setTag(System.currentTimeMillis());

                        TextView tv_msg = (TextView) giftView.findViewById(R.id.tv_msg);
                        tv_msg.setText(showVo.getMsg());

                        ImageView iv_present= (ImageView) giftView.findViewById(R.id.iv_present);
                        iv_present.setBackgroundResource(present_1evel[Integer.parseInt(showVo.getGift_id()) - 1]);
                        iv_present.setTag(num);

                        LinearLayout linearLayout = (LinearLayout) giftView.findViewById(R.id.linearLayout);
                        if (showVo.getIdent().equals("0")){//绿色
                            linearLayout.setBackgroundResource(present_bac[0]);
                        }else if (showVo.getIdent().equals("1")){//红色
                            linearLayout.setBackgroundResource(present_bac[1]);
                        }else{//蓝色
                            linearLayout.setBackgroundResource(present_bac[2]);
                        }

                        giftNum.startAnimation(giftNumAnim);

                    }
                    break;
                case GET_QUEUE_GIFT://如果是从队列中获取礼物实体的消息,从队列中获取礼物

                    GiftVo vo = queue.peek();
                    if (vo != null) {//如果从队列中获取的礼物不为空，那么就将礼物展示在界面上
                        Message giftMsg = new Message();
                        giftMsg.obj = vo;
                        giftMsg.what = SHOW_GIFT_FLAG;
                        handler.sendMessage(giftMsg);
                    } else {
                        handler.sendEmptyMessageDelayed(GET_QUEUE_GIFT, 1000);//如果这次从队列中获取的消息是礼物是空的，则一秒之后重新获取
                    }
                    break;

                case REMOVE_GIFT_VIEW://当礼物的View显示超时，删除礼物View
                    final int index = (int) msg.obj;
                    View removeView = giftCon.getChildAt(index);
                    outAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            new Handler().post(new Runnable() {
                                public void run() {
                                    if (giftCon!=null){
                                        giftCon.removeViewAt(index);
                                    }
                                }
                            });

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    if (removeView!=null){
                        removeView.startAnimation(outAnim);
                    }

                    break;
                default:
                    break;
            }
        }
    };

    private void createview(View giftView, String userId, String countnum, int num, GiftVo showVo) {
        queue.poll();
        //根据userid判断不逊在,创建一个新的
        // 获取礼物的View的布局
        giftView = LayoutInflater.from(cxt).inflate(R.layout.gift_item, null);
        giftView.setTag(userId);
        giftView.findViewById(R.id.iv_present).setTag(num);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity= Gravity.RIGHT;//因为是动态添加的布局,所以要代码设置其在父层右边,为初始布局,不然最终一直跑到左边
        lp.topMargin = 5;
        giftView.setLayoutParams(lp);

        //显示礼物的数量
        final MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.gift_num);
        giftNum.setTag(num);
        giftNum.setPadding(0, 10, 0, 0);
        giftNum.setText("X" + num);

        TextView tv = (TextView) giftView.findViewById(R.id.name);
        tv.setText(showVo.getUserId());
        tv.setTag(System.currentTimeMillis());

        TextView tv_msg = (TextView) giftView.findViewById(R.id.tv_msg);
        tv_msg.setText(showVo.getMsg());

        ImageView iv_present= (ImageView) giftView.findViewById(R.id.iv_present);
        iv_present.setBackgroundResource(present_1evel[Integer.parseInt(showVo.getGift_id()) - 1]);
        iv_present.setTag(num);

        LinearLayout linearLayout = (LinearLayout) giftView.findViewById(R.id.linearLayout);
        if (showVo.getIdent().equals("0")){//绿色
            linearLayout.setBackgroundResource(present_bac[0]);
        }else if (showVo.getIdent().equals("1")){//红色
            linearLayout.setBackgroundResource(present_bac[1]);
        }else{//蓝色
            linearLayout.setBackgroundResource(present_bac[2]);
        }


        CircleImageView iv_icon= (CircleImageView) giftView.findViewById(R.id.iv_icon);
        ImageLoader.getInstance().displayImage(Preference.img_url + "40x40/" + showVo.getImage(), iv_icon, ImageLoaderOption.optionsHeader);

        //将礼物的View添加到礼物的ViewGroup中
        giftCon.addView(giftView);
        giftView.startAnimation(inAnim);//播放礼物View出现的动
        inAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                giftNum.startAnimation(giftNumAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public GiftShowManager(Context cxt, final LinearLayout giftCon) {
        this.cxt = cxt;
        this.giftCon = giftCon;
        queue = new LinkedBlockingQueue<GiftVo>(100);
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(cxt, R.anim.gift_in);
        outAnim = (AnimationSet) AnimationUtils.loadAnimation(cxt, R.anim.gift_out);
        giftNumAnim = (ScaleAnimation) AnimationUtils.loadAnimation(cxt, R.anim.gift_num);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int count = giftCon.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = giftCon.getChildAt(i);
                    TextView name = (TextView) view.findViewById(R.id.name);
                    long nowtime = System.currentTimeMillis();
                    long upTime = (long) name.getTag();
                    if ((nowtime - upTime) >= 3000) {
                        Message msg = new Message();
                        msg.obj = i;
                        msg.what = REMOVE_GIFT_VIEW;
                        handler.sendMessage(msg);
                    }
                }
            }
        };
        timer = new Timer();
        timer.schedule(task, 2000, 2000);

    }

    //开始显示礼物
    public void showGift() {
        handler.sendEmptyMessageDelayed(GET_QUEUE_GIFT, 1000);//从队列中获取礼物
    }

    //放入礼物到队列
    public boolean addGift(GiftVo vo) {
        return queue.add(vo);
    }


}
