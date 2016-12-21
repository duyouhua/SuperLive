package com.joytouch.superlive.javabean;

import java.util.List;

/**
 * Created by Administrator on 5/23 0023.
 */
public class lotteryDetailInfo {

    private String message;
    private QueInfoBean que_info;
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public QueInfoBean getQue_info() {
        return que_info;
    }

    public void setQue_info(QueInfoBean que_info) {
        this.que_info = que_info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class QueInfoBean {
        private String content;
        private String guess_id;
        private String my_option;
        private String my_option_money;
        private String option_left;
        private String option_right;
        private String predict_money;
        private String room_bet;
        private String start_time_s;
        private String status;
        private String stop_time;
        private String stop_time_s;
        private String total_left;
        private String total_right;
        private String win_option;
        private List<String> total_money;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getGuess_id() {
            return guess_id;
        }

        public void setGuess_id(String guess_id) {
            this.guess_id = guess_id;
        }

        public String getMy_option() {
            return my_option;
        }

        public void setMy_option(String my_option) {
            this.my_option = my_option;
        }

        public String getMy_option_money() {
            return my_option_money;
        }

        public void setMy_option_money(String my_option_money) {
            this.my_option_money = my_option_money;
        }

        public String getOption_left() {
            return option_left;
        }

        public void setOption_left(String option_left) {
            this.option_left = option_left;
        }

        public String getOption_right() {
            return option_right;
        }

        public void setOption_right(String option_right) {
            this.option_right = option_right;
        }

        public String getPredict_money() {
            return predict_money;
        }

        public void setPredict_money(String predict_money) {
            this.predict_money = predict_money;
        }

        public String getRoom_bet() {
            return room_bet;
        }

        public void setRoom_bet(String room_bet) {
            this.room_bet = room_bet;
        }

        public String getStart_time_s() {
            return start_time_s;
        }

        public void setStart_time_s(String start_time_s) {
            this.start_time_s = start_time_s;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStop_time() {
            return stop_time;
        }

        public void setStop_time(String stop_time) {
            this.stop_time = stop_time;
        }

        public String getStop_time_s() {
            return stop_time_s;
        }

        public void setStop_time_s(String stop_time_s) {
            this.stop_time_s = stop_time_s;
        }

        public String getTotal_left() {
            return total_left;
        }

        public void setTotal_left(String total_left) {
            this.total_left = total_left;
        }

        public String getTotal_right() {
            return total_right;
        }

        public void setTotal_right(String total_right) {
            this.total_right = total_right;
        }

        public String getWin_option() {
            return win_option;
        }

        public void setWin_option(String win_option) {
            this.win_option = win_option;
        }

        public List<String> getTotal_money() {
            return total_money;
        }

        public void setTotal_money(List<String> total_money) {
            this.total_money = total_money;
        }
    }
}
