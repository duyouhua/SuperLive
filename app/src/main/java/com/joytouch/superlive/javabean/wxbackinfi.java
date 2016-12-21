package com.joytouch.superlive.javabean;

/**
 * Created by Administrator on 2016/4/27.
 */
public class wxbackinfi {
    private String appid;
    private String partnerId;
    private String packageValue;
    private String prepayId;
    private String nonceStr;
    private String timeStamp;
    private String sign;

    public wxbackinfi(String appid, String partnerId, String packageValue, String prepayId, String nonceStr, String timeStamp, String sign) {
        this.appid = appid;
        this.partnerId = partnerId;
        this.packageValue = packageValue;
        this.prepayId = prepayId;
        this.nonceStr = nonceStr;
        this.timeStamp = timeStamp;
        this.sign = sign;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
