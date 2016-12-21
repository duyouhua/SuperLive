package com.joytouch.superlive.javabean;

import java.util.List;

/**
 * Created by sks on 2016/7/24.
 */
public class RedChaiResult {
    private String res_code;
    private List<RedChaiUser> redpacketer_user_list;
    private RedChaiUser recpacketer_user;
    private RedChaiRedpacketer redpacketer;

    public String getRes_code() {
        return res_code;
    }

    public void setRes_code(String res_code) {
        this.res_code = res_code;
    }

    public List<RedChaiUser> getRedpacketer_user_list() {
        return redpacketer_user_list;
    }

    public void setRedpacketer_user_list(List<RedChaiUser> redpacketer_user_list) {
        this.redpacketer_user_list = redpacketer_user_list;
    }

    public RedChaiUser getRecpacketer_user() {
        return recpacketer_user;
    }

    public void setRecpacketer_user(RedChaiUser recpacketer_user) {
        this.recpacketer_user = recpacketer_user;
    }

    public RedChaiRedpacketer getRedpacketer() {
        return redpacketer;
    }

    public void setRedpacketer(RedChaiRedpacketer redpacketer) {
        this.redpacketer = redpacketer;
    }
}
