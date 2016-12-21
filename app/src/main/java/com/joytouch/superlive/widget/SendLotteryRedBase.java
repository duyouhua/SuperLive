package com.joytouch.superlive.widget;

/**
 * Created by sks on 2016/7/25.
 */
public class SendLotteryRedBase {
    private String code;
    private String message;
    private String timestamp;
    private LottertRedResult result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public LottertRedResult getResult() {
        return result;
    }

    public void setResult(LottertRedResult result) {
        this.result = result;
    }
}
