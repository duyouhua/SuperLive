package com.joytouch.superlive.javabean;

/**
 * Created by yj on 2016/4/27.
 * 竞猜信息表
 */
public class LotteryInfo {
    private boolean isOdds;//是否是擂台
    private boolean isEnd;//是否已截止
    private boolean isSelf;//是否是自己参与的
    private boolean isWin;//自己是否赢了
    private boolean isSeltOdds;//是否是自己抢到了擂台
    private boolean isOddsSelector;//擂台是否被选择
    private boolean isOpenLottery;//shi否开奖
    private int oddsSelector;//擂台被选择那边了
    private int selfeSelector;//自己选择了哪边
    private String endtime;//结束时间
    private long stopTime;//截止时间戳
    private String odds;//赔率
    private String title;//竞猜标题
    private String leftselector;//左边选项内容
    private String rightselector;//右边选项内容
    private String leftgold;//左边投注金币
    private String rightgold;//右边投注金币
    private String imagehead;//擂主头像
    private String betting;//自己的投注数
    private String earnings;//预计收益
    private String wingold;//赢的钱数
    private String openSelector;//赢的选项
    private String anchorGold;//打赏主播的金额
    private String bettingGolde;//每次投注金额
    private String matchid;//竞猜id
    private String Add_user_id;
    private String win_option;
    private  String status;
    private boolean isSelfOpenLiving;

    public boolean isSelfOpenLiving() {
        return isSelfOpenLiving;
    }

    public void setIsSelfOpenLiving(boolean isSelfOpenLiving) {
        this.isSelfOpenLiving = isSelfOpenLiving;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWin_option() {
        return win_option;
    }

    public void setWin_option(String win_option) {
        this.win_option = win_option;
    }

    public String getMatchid() {
        return matchid;
    }

    public void setMatchid(String matchid) {
        this.matchid = matchid;
    }

    public String getBettingGolde() {
        return bettingGolde;
    }

    public void setBettingGolde(String bettingGolde) {
        this.bettingGolde = bettingGolde;
    }

    public String getOpenSelector() {
        return openSelector;
    }

    public void setOpenSelector(String openSelector) {
        this.openSelector = openSelector;
    }

    public String getAnchorGold() {
        return anchorGold;
    }

    public void setAnchorGold(String anchorGold) {
        this.anchorGold = anchorGold;
    }

    public String getWingold() {
        return wingold;
    }

    public void setWingold(String wingold) {
        this.wingold = wingold;
    }

    public String getBetting() {
        return betting;
    }

    public void setBetting(String betting) {
        this.betting = betting;
    }

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }

    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLeftselector() {
        return leftselector;
    }

    public void setLeftselector(String leftselector) {
        this.leftselector = leftselector;
    }

    public String getRightselector() {
        return rightselector;
    }

    public void setRightselector(String rightselector) {
        this.rightselector = rightselector;
    }

    public String getLeftgold() {
        return leftgold;
    }

    public void setLeftgold(String leftgold) {
        this.leftgold = leftgold;
    }

    public String getRightgold() {
        return rightgold;
    }

    public void setRightgold(String rightgold) {
        this.rightgold = rightgold;
    }

    public String getImagehead() {
        return imagehead;
    }

    public void setImagehead(String imagehead) {
        this.imagehead = imagehead;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getSelfeSelector() {
        return selfeSelector;
    }

    public void setSelfeSelector(int selfeSelector) {
        this.selfeSelector = selfeSelector;
    }

    public int getOddsSelector() {
        return oddsSelector;
    }

    public void setOddsSelector(int oddsSelector) {
        this.oddsSelector = oddsSelector;
    }

    public boolean isOpenLottery() {
        return isOpenLottery;
    }

    public void setIsOpenLottery(boolean isOpenLottery) {
        this.isOpenLottery = isOpenLottery;
    }

    public boolean isOddsSelector() {
        return isOddsSelector;
    }

    public void setIsOddsSelector(boolean isOddsSelector) {
        this.isOddsSelector = isOddsSelector;
    }

    public boolean isOdds() {
        return isOdds;
    }

    public void setIsOdds(boolean isOdds) {
        this.isOdds = isOdds;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setIsSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }
    public boolean isWin() {
        return isWin;
    }

    public void setIsWin(boolean isWin) {
        this.isWin = isWin;
    }

    public boolean isSeltOdds() {
        return isSeltOdds;
    }

    public void setIsSeltOdds(boolean isSeltOdds) {
        this.isSeltOdds = isSeltOdds;
    }

    public String getAdd_user_id() {
        return Add_user_id;
    }

    public void setAdd_user_id(String add_user_id) {
        Add_user_id = add_user_id;
    }
}
