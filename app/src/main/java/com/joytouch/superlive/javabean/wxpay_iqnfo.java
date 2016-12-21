package com.joytouch.superlive.javabean;

/**
 * Created by Administrator on 2016/4/24.
 */
public class wxpay_iqnfo {
    private wxbackinfi list;
    private String message;
    private String status;

    public wxpay_iqnfo(wxbackinfi list, String message, String status) {
        this.list = list;
        this.message = message;
        this.status = status;
    }

    public wxbackinfi getList() {
        return list;
    }

    public void setList(wxbackinfi list) {
        this.list = list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
