package com.joytouch.superlive.javabean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 通知的本地存储
 1.在类名上面加入@Table标签，标签里面的属性name的值就是以后生成的数据库的表的名字
 2.实体bean里面的属性需要加上@Column标签，这样这个标签的name属性的值会对应数据库里面的表的字段。
 3.实体bean里面的普通属性，如果没有加上@Column标签就不会在生成表的时候在表里面加入字段。
 4.实体bean中必须有一个主键，如果没有主键，表以后不会创建成功，@Column(name=”id”,isId=true,autoGen=true)这个属性name的值代表的是表的主键的标识，
 isId这个属性代表的是该属性是不是表的主键，autoGen代表的是主键是否是自增长，如果不写autoGen这个属性，默认是自增长的属性。
 http://blog.csdn.net/qq379454816/article/details/50086349
 */

//适配表
@Table(name = "MessageTable")
public class messagelist {
    //记录的id
    @Column(name = "id", isId = true,autoGen=true)
    private int id;

    //我的user_id
    @Column(name = "my_userid")
    private String my_userid;

    //我的my_nickname
    @Column(name = "my_nickname")
    private String my_nickname;

    //我的my_img
    @Column(name = "my_img")
    private String my_img;

    //他人的user_id
    @Column(name = "userid")
    private String userid;

    //他人的昵称
    @Column(name = "nickname")
    private String nickname;

    //他人的头像地址
    @Column(name = "image")
    private String image;

    //他的等级
    @Column(name = "level")
    private String level;

    //聊天记录的时间
    @Column(name = "time")
    private Long time;

    //聊天的内容
    @Column(name = "content")
    private String content;

    //聊天记录的发送状态
    @Column(name = "status")
    private String status;

    //通知type
    @Column(name = "type")
    private String type;


//粉丝

    //邀请分享时跳转需要的match_id
    @Column(name = "match_id")
    private String match_id;

    //邀请分享时跳转需要的room_id
    @Column(name = "room_id")
    private String room_id;

    //邀请分享时跳转需要的qiutan_id
    @Column(name = "qiutan_id")
    private String qiutan_id;


    //图片img_url
    @Column(name = "img_url")
    private String img_url;

    //intent_url
    @Column(name = "intent_url")
    private String intent_url;

    //系统消息文字跳转urltext_url
    @Column(name = "text")
    private String text;

    //跳转详情页is_private
    @Column(name = "is_private")
    private String is_private;

    public String getIs_private() {
        return is_private;
    }

    public void setIs_private(String is_private) {
        this.is_private = is_private;
    }

    public String getMy_nickname() {
        return my_nickname;
    }

    public void setMy_nickname(String my_nickname) {
        this.my_nickname = my_nickname;
    }

    public String getMy_img() {
        return my_img;
    }

    public void setMy_img(String my_img) {
        this.my_img = my_img;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getIntent_url() {
        return intent_url;
    }

    public void setIntent_url(String intent_url) {
        this.intent_url = intent_url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getQiutan_id() {
        return qiutan_id;
    }

    public void setQiutan_id(String qiutan_id) {
        this.qiutan_id = qiutan_id;
    }

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMy_userid() {
        return my_userid;
    }

    public void setMy_userid(String my_userid) {
        this.my_userid = my_userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MessageTable [id=" + id +
                ", my_userid=" + my_userid +
                ", userid=" + userid +
                ", nickname=" + nickname+
                ", image=" + image+
                ", type=" + type+
                ", time=" + time+
                ", content=" + content+
                ", status=" + status+
                ", level=" + level+
                ", match_id=" + match_id+
                ", room_id=" + room_id+
                ", qiutan_id=" + qiutan_id+
                ", img_url=" + img_url+
                ", intent_url=" + intent_url+
                ", text=" + text+
                ", my_nickname=" + my_nickname+
                ", my_img=" + my_img+
                ", is_private=" + is_private+
                "]";
    }
}