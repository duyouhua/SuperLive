package com.joytouch.superlive.javabean;

import java.io.Serializable;

/**
 * Created by yj on 2016/4/12.
 * 主播信息类
 */
public class AnchorInfo implements Serializable {
    public String roomid;
    public String anchorid;
    public String headerimg;
    private String liveurl;
    private String betting;
    private String online;
    private String urltype;
    private String urljs;
    private String anchorname;
    private String isFirst;
    private  boolean isselector;
    private String level;
    private String agent;
    private String replace;
    private String reg;
    private String roomtype;
    private String match;

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean isselector() {
        return isselector;
    }

    public void setIsselector(boolean isselector) {
        this.isselector = isselector;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String isFirst) {
        this.isFirst = isFirst;
    }

    public String getLiveurl() {
        return liveurl;
    }

    public void setLiveurl(String liveurl) {
        this.liveurl = liveurl;
    }

    public String getBetting() {
        return betting;
    }

    public void setBetting(String betting) {
        this.betting = betting;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getUrltype() {
        return urltype;
    }

    public void setUrltype(String urltype) {
        this.urltype = urltype;
    }

    public String getUrljs() {
        return urljs;
    }

    public void setUrljs(String urljs) {
        this.urljs = urljs;
    }

    public String getAnchorname() {
        return anchorname;
    }

    public void setAnchorname(String anchorname) {
        this.anchorname = anchorname;
    }

    public String getHeaderimg() {
        return headerimg;
    }

    public void setHeaderimg(String headerimg) {
        this.headerimg = headerimg;
    }

    public String getAnchorid() {
        return anchorid;
    }

    public void setAnchorid(String anchorid) {
        this.anchorid = anchorid;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }
}
