package com.joytouch.superlive.javabean;

/**
 * Created by sks on 2016/7/24.
 */
public class RedChaiBase {
    private String code,message,timestamp;
    private RedChaiResult result;

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

    public RedChaiResult getResult() {
        return result;
    }

    public void setResult(RedChaiResult result) {
        this.result = result;
    }
}
