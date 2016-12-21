package com.joytouch.superlive.utils;

/**
 * Created by yj on 2016/5/3.
 * 聊天天
 */
public class ChatUtils {
    public ChatUtils() {
    }
    public native void startIm(String username, String display_name, String server, int port, String passwd, int simple_mode, int compress);
    public native void closeIm();
    public native void reset(String username, String display_name, String passwd);

    public native boolean joinRoom(String roomName);
    public native boolean leaveRoom(String roomName);

}
