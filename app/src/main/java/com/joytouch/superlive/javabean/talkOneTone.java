package com.joytouch.superlive.javabean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 私信的本地存储
 1.在类名上面加入@Table标签，标签里面的属性name的值就是以后生成的数据库的表的名字
 2.实体bean里面的属性需要加上@Column标签，这样这个标签的name属性的值会对应数据库里面的表的字段。
 3.实体bean里面的普通属性，如果没有加上@Column标签就不会在生成表的时候在表里面加入字段。
 4.实体bean中必须有一个主键，如果没有主键，表以后不会创建成功，@Column(name=”id”,isId=true,autoGen=true)这个属性name的值代表的是表的主键的标识，
   isId这个属性代表的是该属性是不是表的主键，autoGen代表的是主键是否是自增长，如果不写autoGen这个属性，默认是自增长的属性。
   http://blog.csdn.net/qq379454816/article/details/50086349
 */


@Table(name = "Pletter")
public class talkOneTone {
    //记录的id
    @Column(name = "id", isId = true,autoGen=true)
    private int id;

    //我的user_id
    @Column(name = "my_userid")
    private String my_userid;

    //我的昵称
    @Column(name = "my_nickname")
    private String my_nickname;

    //我的头像
    @Column(name = "my_img")
    private String my_img;

    //他人的user_id
    @Column(name = "other_userid")
    private String other_userid;

    //他人的昵称
    @Column(name = "nickname")
    private String nickname;

    //他人的头像地址
    @Column(name = "image")
    private String image;

    //聊天记录的时间
    @Column(name = "time")
    private Long time;

    //聊天的内容
    @Column(name = "content")
    private String content;

    //聊天记录的发送状态
    @Column(name = "status")
    private String status;

    //聊天记录是发出还是接受
    @Column(name = "left_right")
    private String left_right;

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

    public String getOther_userid() {
        return other_userid;
    }

    public void setOther_userid(String other_userid) {
        this.other_userid = other_userid;
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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setTime(long time) {
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

    public String getLeft_right() {
        return left_right;
    }

    public void setLeft_right(String left_right) {
        this.left_right = left_right;
    }


    @Override
    public String toString() {
        return "Pletter [id=" + id +
                ", my_userid=" + my_userid +
                ", my_nickname=" + my_nickname +
                ", my_img=" + my_img +
                ", other_userid=" + other_userid +
                ", nickname=" + nickname+
                ", image=" + image+
                ", time=" + time+
                ", content=" + content+
                ", status=" + status+
                ", left_right=" + left_right+"]";
    }
}