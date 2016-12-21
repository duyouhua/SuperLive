package com.joytouch.superlive.utils.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 当前类是创建数据录的帮助类
 * @author fengdongfei
 *
 */
public class AccounDBHelper extends SQLiteOpenHelper {

    /**
     * 有参构造函数
     * 用于创造数据库
     * @param context   上下文
     * @param name      数据库名称
     * @param factory   游标工厂.当值是null的时候表示使用默认的游标,指向哪里,那一行的数据就存储在curson对象里
     * @param version   版本号,最低是1
     */
    public AccounDBHelper(Context context, String name, CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    /**
     * 第一次创建数据库时被调用,用于初始化数据库
     * 往表中插入初始的数据记录
     * do SQLite数据实例对象
     */
    /**
     * CREATE TABLE `pletter` (
     `id` int(11) NOT NULL,
     `my_userid` varchar(64) NOT NULL,
     `my_nickname` varchar(64) NOT NULL,
     `my_img` varchar(64) DEFAULT NULL,
     `other_userid` varchar(64) NOT NULL,
     `nickname` varchar(255) NOT NULL,
     `image` varchar(64) DEFAULT NULL,
     `time` int(11) DEFAULT NULL,
     `content` varchar(255) NOT NULL,
     `status` varchar(2) NOT NULL,
     `left_right` varchar(64) NOT NULL,
     PRIMARY KEY (`id`)
     )
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //在account数据库中创建account表
        String sql="CREATE TABLE `mycount` (\n" +
                "  `id` integer primary key autoincrement NOT NULL,\n" +
                "  `my_userid` varchar(64) NOT NULL,\n" +
                "  `my_nickname` varchar(64) NOT NULL,\n" +
                "  `my_img` varchar(64) DEFAULT NULL,\n" +
                "  `other_userid` varchar(64) NOT NULL,\n" +
                "  `nickname` varchar(255) NOT NULL,\n" +
                "  `image` varchar(64) DEFAULT NULL,\n" +
                "  `time` int(11) DEFAULT NULL,\n" +
                "  `content` varchar(255) NOT NULL,\n" +
                "  `status` varchar(2) NOT NULL,\n" +
                "  `left_right` varchar(64) NOT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ";
        //create table account (id integer primary key autoincrement,
        // my_userid varchar(64) NOT NULL,
        // my_nickname varchar(64) NOT NULL,
        // my_img varchar(64) NOT NULL,
        // other_userid varchar(64) NOT NULL,
        // nickname varchar(64) NOT NULL,
        // image varchar(64) NOT NULL,
        // time varchar(64),
        // content  NOT NULL),
        //status varchar(64) NOT NULL,
        // left_right varchar(64) NOT NULL)
        db.execSQL(sql);
    }
    /**
     * 升级数据库时被调用,即数据库的版本号发生变化
     * 参数一:数据库实体对象
     * 参数二:旧的版本
     * 参数三:新的版本
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
