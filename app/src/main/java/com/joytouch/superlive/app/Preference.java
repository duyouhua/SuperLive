package com.joytouch.superlive.app;

import android.os.Environment;

/**
 * Created by yj on 2016/4/6.
 */
public class Preference {
  public static String localUrl = "http://api.cjzhibo.net/v5/";
    public static String photourl = "http://img.cjzhibo.net/img/";
//    public static String localUrl = "http://tapi.cjzhibo.net/v5/";
//    public static String photourl = "http://timg.cjzhibo.net/img/";
    public static String store_address="http://api.cjzhibo.net/mobile/temp/apprecommend.json";//应用推荐地址
    public static String liuliangurl = "http://api.cjzhibo.net/mobile/main/flow.json?token=";//兑换流量地址
    public static String gameurl = "http://gamet.cjzhibo.net/?token=";//小游戏地址
    public static String timeUrl = "http://api.cjzhibo.net/v5/Tags.json";

      public static String selfLotterySend="http://adm.cjzhibo.net/f/serverApi/guess/save";//出題
//  public static String selfLotterySend="http://tadm.cjzhibo.net/f/serverApi/guess/save";//出題测试地址

      public static String openlottery = "http://adm.cjzhibo.net/f/serverApi/guess/lottery";//开奖接口
//  public static String openlottery = "http://tadm.cjzhibo.net/f/serverApi/guess/lottery";//开奖接口

      public static String grabred = "http://red.cjzhibo.net/v1/redpacket_snatch";//抢红包
//  public static String grabred = "http://124.65.163.254:8091/v1/redpacket_snatch";//抢红包

      public static String sendRed = "http://red.cjzhibo.net/v1/redpacket_create";//发送包
//  public static String sendRed = "http://124.65.163.254:8091/v1/redpacket_create";//ceshi发送包

    //测试地址
//    public static final String update_url = "http://www.qiuwin.com/InterfaceForMobile/" + "ZB_ver2.php";
    //正式地址
    public static final String update_url = "http://s.cjzhibo.net/InterfaceForMobile/" + "ZB_ver.php";
    //分享前url
    public static final String shareUrl = "http://api.cjzhibo.net/mobile/main/live.json?";

    /**
     * 微信的配置
     */
    public static String wx_appid = "wx4e6a27bc488cc1d2";
    public static final String wx_appSecret = "c587264efd6575af3d0bbf54fe486bfe";
    public static final String softname = "超级直播_android版";
    public static final boolean isNBA = false;
    public static String payType = "1";//微信支付版本号
    public static String app_version = "V5.3";//app版本
    public static String version = "6";
    /*
   * qq的配置
   * */
    public static final String qq_appid = "1101346802";
    public static final String qq_appkey = "KL4tP4Vx3EFwGNYh";
    public static String package_name = "com.joytouch.superlive";
    public static String phone = "1";
    public static String room_id = "";
    public static String zhubo_id = "";
    public static String match_id = "";
    public static boolean isClock = false;
    public static final String rootDir = Environment
            .getExternalStorageDirectory().toString() + "/SuperLive";
    public static final String cacheIamge = rootDir + "/Cache/";
    public static final String key_f = "BTTQ";
    public static final String key_b = "HELLO";
    public static final String absDownloadDir = rootDir + "/DownLoad";

    //保存本地数据
    public static String myuser_id = "myuser_id";
    public static String nickname = "nickname";
    public static String headPhoto = "headPhoto";//img_id
    public static String name159 = "name159";
    public static String pwd159 = "pwd159";
    public static String phone_id = "phone_id";
    public static String username = "username";
    public static String passwd = "passwd";
    public static String token = "token";
    public static String sign = "sign";
  public static String mobile="mobile";
    public static String level = "level";
    public static String balance = "balance";
    public static String lotteryMoney = "100";
    public static String prefernce_alarms = "alarms";
    public static String matchroom = "match";
    public static String matchname = "matchname";
    public static String ballgold = "ballgold";
    //缓存文件名称
    public static final String versionCode = "versionCode";
    public static final String cacheDir = "SuperLive/Cache";
    public static final String preference = "preference";
    public static String load2G3G = "load2G3G";
    public static String baseheadphoto = "6,268acfe4eb34";//默认头像id
    //从im拿图片地址
    public static String img_url = photourl ;
    //接口
    public static String Reg = localUrl + "Reg.json";//注册
    public static String Captcha = localUrl + "Captcha.json";//注册的验证码接口
    public static String getPayData = localUrl + "pay/getPayData.json";//微信支付
    public static String WxPay = localUrl + "pay/WxPay.json";//获取微信支付所需要的信息
    public static String normal = localUrl + "Login/normal.json";//正常手机登录
    public static String bindOld = localUrl + "Login/bindOld.json";//旧三方账号绑定手机号并登录
    public static String connect = localUrl + "Login/connect.json";//三方登录
    public static String bindConnect = localUrl + "Login/bindConnect.json";//新三方联合登录绑定账号 并登录
    public static String FindPasswd = localUrl + "FindPasswd.json";//找回密码
    public static String img_headphoto = localUrl + "EditUser/EditImg.json";//修改头像
    public static String EditNick = localUrl + "EditUser/EditNick.json";//修改昵称
    public static String EditSign = localUrl + "EditUser/EditSign.json";//修改签名
    public static String EditPwd = localUrl + "EditUser/EditPwd.json";//修改密码
    public static String Myinfo = localUrl + "Myinfo.json"; //个人中心信息
    public static String goods = localUrl + "goods.json";//商品列表
    public static String insertgoods = localUrl + "goods/insertgoods.json";//商品购买
    public static String asset = localUrl + "asset.json";//我的道具
    public static String exceptional_list = localUrl + "reward.json";//打赏金额列表
    public static String reward = localUrl + "reward/doreward.json";//打赏
    public static String golddetail = localUrl + "gold.json";//金币明细
    public static final String jifen_help_url = "http://www.qiuwin.com/SuperLive/Quiz/intro.php";//竞猜帮助
    public static final String livetitle = localUrl + "Tags.json";//直播列表头
    public static final String livematchlist = localUrl + "Matchlist.json";//直播比赛列表
    public static String review_option = localUrl + "Reviewlist/config.json";//回顾选项
    public static String review_list = localUrl + "Reviewlist.json";//回顾列表
    public static String Conlist = localUrl + "Concern/Conlist.json";//关注列表
    public static String Fanlist = localUrl + "Concern/Fanlist.json";//粉丝列表
    public static String Blacklist = localUrl + "Concern/Blacklist.json";//黑名单列表
    public static String review_conmments = localUrl + "review/getmatchcomment.json";//回顾详情页评论列表
    public static String review_sendcomment = localUrl + "review/insertcomment.json";//回顾详情发送评论功能
    public static String Addcon = localUrl + "Concern/Addcon.json";//添加关注
    public static String Delcon = localUrl + "Concern/Delcon.json";//取消关注
    public static String Addblack = localUrl + "Concern/Addblack.json";//添加黑名单
    public static String Delblack = localUrl + "Concern/Delblack.json";//取消拉黑
    public static String LotteryLabel = localUrl + "Que/Quelist.json";//竞猜标签
    public static String live_jifen = localUrl + "level.json";//等级积分
    public static String person = localUrl + "person.json";//个人主页
    public static String privateperson = localUrl + "userinfo.json";//私人主页
    public static String LiveDetail = localUrl + "live.json";//直播详情
    public static String entry = localUrl + "quest/entry.json";//每日签到
    public static String quest = localUrl + "quest.json";//任务列表
    public static String Editinfo = localUrl + "Edituser/Editinfo.json";//头像和昵称一起上传
    public static String recommend = localUrl + "recommend.json";//推荐关注
    public static String LiveRank = localUrl + "live/getparttop.json";//直播详情排行榜
    public static String JoinLottery = localUrl + "Que/Partque.json";//参与竞猜
    public static String LotteryDetail = localUrl + "Que/Quecetails.json";//竞猜详情
    public static String LotteryPeople = localUrl + "Que/Partuserlist.json";//竞猜人数
    public static String total = localUrl + "Rank/total.json";//富豪榜
    public static String week = localUrl + "Rank/week.json";//周榜
    public static String Myquelist = localUrl + "Que/Myquelist.json";//我的竞猜列表
    public static String Quecetails = localUrl + "Que/Quecetails.json";//我的竞猜详情页
    public static String Partuserlist = localUrl + "Que/Partuserlist.json";//我的竞猜详情页下面类表上啦加载
    public static String LiveSourceList = localUrl + "Sourcelive/livesource.json";//调用源开房间获取源列表
    public static String CreateRoomsBySource = localUrl + "Sourcelive/sourceroom.json";//调用源创建房间
    public static String CloseRoom = localUrl + "Mylive/closeroom.json";//关闭房间
    public static String JoinRoom = localUrl + "Sourcelive/intoroom.json";//调用源进入房间
    public static String LiveAction = localUrl + "Outs.json";//赛况接口
    public static String redpoint = localUrl + "redpoint.json";//红点
    public static String Getuser = localUrl + "Concern/Getuser.json";//根据userid拿到个人信息
    public static String ReviewMv = localUrl + "review.json";//回顾详情集锦列表接口
    public static String LiveAListLottery = localUrl + "Quizmode.json";//赛况接口
    public static String InvitationFans = localUrl + "live/invitation.json";//粉丝邀请接口
    public static String share=localUrl+"quest/share.json";//分享任务
    public static String GetQuestion = localUrl+"Sourcelive/matchguess.json";//主播问题列表
    public static String UseGuess = localUrl+"Sourcelive/useguess.json";//主播调用题目
    public static String useraddress=localUrl+"address/useraddress.json";//用户地址
    public static String insert=localUrl+"address/insert.json";//插入地址
    public static String ouzhoubeisign=localUrl+"Sign/sign.json";//欧洲杯签到
    public static String feedback=localUrl+"feedback.json";//一键反馈
    public static String online=localUrl+"online.json";//广场在线好友
    public static String Hotguess=localUrl+"Hotguess/List.json";//广场推题
    public static String banned=localUrl+"Roomgag/Gag.json";//禁言
    public static String removebanned=localUrl+"Roomgag/Removegag.json";//解禁
    public static String permissions=localUrl+"Roomauth/Checkque.json";//出题权限
    public static String present = localUrl+"present.json";//礼物赠送
    public static String addadmin = localUrl+"Roomauth/Addadmin.json";//授予禁言权限
    public static String removeadmin = localUrl +"Roomauth/Removeadmin.json";//撤销禁言权限
    public static String editScore = localUrl+"Roomgag/Editscore.json";//修改比分
    public static String ballBuy = localUrl+"goods/ticket.json";//球票购买
    public static String ballrank= localUrl+"Rank/gift.json";//球票排行榜
    public static String balldetail= localUrl+"Ticket_detail/Getlist.json";//球票明细
    public static String bindmobile = localUrl+"Mylive/bindmobile.json";//开始直播绑定手机号
    public static String openlive = localUrl+"Mylive/openlive.json";//开直播
    public static String loadlogo = localUrl+"Mylive/loadlogo.json";//队伍图片上传
    public static String createlive = localUrl+"Mylive/createlive.json";//申请直播
    public static String addquestionpermission = localUrl +"Roomauth/Addque.json";//赋予出题权限
}

