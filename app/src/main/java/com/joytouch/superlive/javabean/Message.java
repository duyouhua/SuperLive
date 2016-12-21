package com.joytouch.superlive.javabean;

import java.util.List;

/**
 * Created by sks on 2016/4/7.
 */
public class Message{

    public RecommendBean Recommend;
    public String message;
    public String status;


    public static class RecommendBean {
        /**
         * image : 2,5585e10798
         * level : 1
         * userid : 04f68e63-4edf-4104-83e5-9484a51c905d
         * username : ph_ee5dcf79c9c
         */

        public List<recom_item> HotAnchor;
        /**
         * image : 2,4e1072ea7d
         * level :
         * userid : a54be33a-b07b-4799-be10-e34f62a6f589
         * username : 无敌至尊宝
         */

        public List<recom_item> RichPeople;
        /**
         * image : 3,67fa4b967d
         * level : 1
         * userid : 2ef9ff4293a4496899208e2abd9811cd
         * username : 11
         */

        public List<recom_item> SeniorPlayer;
        public List<recom_item> WeekRank;

    }
}
