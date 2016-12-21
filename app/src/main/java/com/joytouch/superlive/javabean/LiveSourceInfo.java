package com.joytouch.superlive.javabean;

/**
 * Created by yj on 2016/4/14.
 * 直播源的信息
 */
public class LiveSourceInfo {
    private boolean selector;
    private String source;
    private String sourceName;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public boolean isSelector() {
        return selector;
    }

    public void setSelector(boolean selector) {
        this.selector = selector;
    }
}
