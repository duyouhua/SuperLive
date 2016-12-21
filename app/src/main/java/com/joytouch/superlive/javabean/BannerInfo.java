package com.joytouch.superlive.javabean;

/**
 * Created by yj on 2016/5/9.
 * banner信息表
 */
public class BannerInfo {
    private String type;
    private String matchid;
    private String rommid;
    private String url;
    private String title;
    private String logo;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMatchid() {
        return matchid;
    }

    public void setMatchid(String matchid) {
        this.matchid = matchid;
    }

    public String getRommid() {
        return rommid;
    }

    public void setRommid(String rommid) {
        this.rommid = rommid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
