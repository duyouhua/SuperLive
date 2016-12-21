package com.joytouch.superlive.javabean;

/**
 * Created by yj on 2016/5/13.
 * 直播详情信息
 */
public class LiveDetail {
    private String name;
    private String shareUrl;
    private String online;
    private String isPrivate;
    private String qiutanid;
    private String cat_league;
    private String cat_type;
    private String team1;
    private String team2;
    private String result;
    private String league_name;
    private String color1;
    private String color2;
    public String match_statu;
    private String match_mode;

    public String getMatch_mode() {
        return match_mode;
    }

    public void setMatch_mode(String match_mode) {
        this.match_mode = match_mode;
    }

    public String getMatch_statu() {
        return match_statu;
    }

    public void setMatch_statu(String match_statu) {
        this.match_statu = match_statu;
    }

    public String getColor1() {
        return color1;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public String getColor2() {
        return color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public String getLeague_name() {
        return league_name;
    }

    public void setLeague_name(String league_name) {
        this.league_name = league_name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getCat_league() {
        return cat_league;
    }

    public void setCat_league(String cat_league) {
        this.cat_league = cat_league;
    }

    public String getCat_type() {
        return cat_type;
    }

    public void setCat_type(String cat_type) {
        this.cat_type = cat_type;
    }

    public String getQiutanid() {
        return qiutanid;
    }

    public void setQiutanid(String qiutanid) {
        this.qiutanid = qiutanid;
    }

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }
}
