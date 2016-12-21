package com.joytouch.superlive.javabean;

/**
 * Created by yj on 2016/4/8.
 * 直播列表比赛对阵信息
 */
public class LiveMatchInfoJavabean {
    private String teama1Name;
    private String teama1Logo;
    private String teama2Name;
    private String teama2Logo;
    private String matchId;
    private String matchNametop;//不是对阵比赛的类型
    private String matchNameBottom;//比赛对阵
    private String matchStatus;
    private String time;
    private String roomid;
    private String qiutanId;
    private String type;
    private String onLine;
    private String loadTime;
    private boolean matchAlarm;
    private String matchType;//比赛的类型
    private String classification;//比赛的分类
    private String matchGroup;//比赛进行哪个阶段
    private String cat_type;
    private String cat_imag;//新闻类logo
    private String league_imag;//联赛logo
    private MatchTime matchTime;
    private long startTime;
    private String score;//比分
    private String stating;
    private String colors;
    private boolean isReward;
    private boolean subsidies;
    private boolean isred;

    public boolean isred() {
        return isred;
    }

    public void setIsred(boolean isred) {
        this.isred = isred;
    }

    public boolean isReward() {
        return isReward;
    }

    public void setIsReward(boolean isReward) {
        this.isReward = isReward;
    }

    public boolean isSubsidies() {
        return subsidies;
    }

    public void setSubsidies(boolean subsidies) {
        this.subsidies = subsidies;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public String getStating() {
        return stating;
    }

    public void setStating(String stating) {
        this.stating = stating;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public MatchTime getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(MatchTime matchTime) {
        this.matchTime = matchTime;
    }

    public String getTeama1Name() {
        return teama1Name;
    }

    public void setTeama1Name(String teama1Name) {
        this.teama1Name = teama1Name;
    }

    public String getTeama1Logo() {
        return teama1Logo;
    }

    public void setTeama1Logo(String teama1Logo) {
        this.teama1Logo = teama1Logo;
    }

    public String getTeama2Name() {
        return teama2Name;
    }

    public void setTeama2Name(String teama2Name) {
        this.teama2Name = teama2Name;
    }

    public String getTeama2Logo() {
        return teama2Logo;
    }

    public void setTeama2Logo(String teama2Logo) {
        this.teama2Logo = teama2Logo;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getMatchNametop() {
        return matchNametop;
    }

    public void setMatchNametop(String matchNametop) {
        this.matchNametop = matchNametop;
    }

    public String getMatchNameBottom() {
        return matchNameBottom;
    }

    public void setMatchNameBottom(String matchNameBottom) {
        this.matchNameBottom = matchNameBottom;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getQiutanId() {
        return qiutanId;
    }

    public void setQiutanId(String qiutanId) {
        this.qiutanId = qiutanId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOnLine() {
        return onLine;
    }

    public void setOnLine(String onLine) {
        this.onLine = onLine;
    }

    public String getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(String loadTime) {
        this.loadTime = loadTime;
    }

    public boolean isMatchAlarm() {
        return matchAlarm;
    }

    public void setMatchAlarm(boolean matchAlarm) {
        this.matchAlarm = matchAlarm;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getMatchGroup() {
        return matchGroup;
    }

    public void setMatchGroup(String matchGroup) {
        this.matchGroup = matchGroup;
    }

    public String getCat_type() {
        return cat_type;
    }

    public void setCat_type(String cat_type) {
        this.cat_type = cat_type;
    }

    public String getCat_imag() {
        return cat_imag;
    }

    public void setCat_imag(String cat_imag) {
        this.cat_imag = cat_imag;
    }

    public String getLeague_imag() {
        return league_imag;
    }

    public void setLeague_imag(String league_imag) {
        this.league_imag = league_imag;
    }
}
