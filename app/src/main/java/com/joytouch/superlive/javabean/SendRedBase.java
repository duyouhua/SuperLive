package com.joytouch.superlive.javabean;

/**
 * Created by sks on 2016/7/26.
 */
public class SendRedBase {
    private String code;
    private String message;
    private String timestamp;
    private SendRedResult result;

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

    public SendRedResult getResult() {
        return result;
    }

    public void setResult(SendRedResult result) {
        this.result = result;
    }
}
