package com.joytouch.superlive.javabean;

import java.io.Serializable;

/**
 * Created by lzx on 2016/5/16.
 * 调用源类
 */
public class LiveSource implements Serializable{
    private String match_name,match_id,live_id,url_type,url_js,live_url,qiutan_match_id,url_reg,
            url_replace;
    private boolean isSelect = false;

    public String getMatch_name() {
        return match_name;
    }

    public void setMatch_name(String match_name) {
        this.match_name = match_name;
    }

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getLive_id() {
        return live_id;
    }

    public void setLive_id(String live_id) {
        this.live_id = live_id;
    }

    public String getUrl_type() {
        return url_type;
    }

    public void setUrl_type(String url_type) {
        this.url_type = url_type;
    }

    public String getUrl_js() {
        return url_js;
    }

    public void setUrl_js(String url_js) {
        this.url_js = url_js;
    }

    public String getLive_url() {
        return live_url;
    }

    public void setLive_url(String live_url) {
        this.live_url = live_url;
    }

    public String getQiutan_match_id() {
        return qiutan_match_id;
    }

    public void setQiutan_match_id(String qiutan_match_id) {
        this.qiutan_match_id = qiutan_match_id;
    }

    public String getUrl_reg() {
        return url_reg;
    }

    public void setUrl_reg(String url_reg) {
        this.url_reg = url_reg;
    }

    public String getUrl_replace() {
        return url_replace;
    }

    public void setUrl_replace(String url_replace) {
        this.url_replace = url_replace;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
