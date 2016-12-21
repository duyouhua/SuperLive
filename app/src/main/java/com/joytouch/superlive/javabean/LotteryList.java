package com.joytouch.superlive.javabean;

import java.util.List;

/**
 * Created by yj on 2016/4/27.
 * 直播详情竞猜标签
 */
public class LotteryList {
    private String title;
    private List<LotteryInfo> info;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<LotteryInfo> getInfo() {
        return info;
    }

    public void setInfo(List<LotteryInfo> info) {
        this.info = info;
    }
}
