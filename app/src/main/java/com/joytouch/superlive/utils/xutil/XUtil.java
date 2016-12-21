package com.joytouch.superlive.utils.xutil;

import android.os.Environment;

import com.joytouch.superlive.javabean.messagelist;
import com.joytouch.superlive.javabean.messagelistAll;
import com.joytouch.superlive.javabean.talkOneTone;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.File;

/**
 * 使用xutil创建数据库的辅助类
 1.setDbName 设置数据库的名称
 2.setDbDir 设置数据库存放的路径
 3.setDbVersion 设置数据库的版本
 4.setAllowTransaction(true) 设置允许开启事务
 5.setDbUpgradeListener 设置一个版本升级的监听方法
 * Created by Administrator on 5/6 0006.
 */
public class XUtil {
    static DbManager.DaoConfig daoConfig;
    //创建私信的数据库
    public static DbManager.DaoConfig getDaoConfig(){
        File file=new File(Environment.getExternalStorageDirectory().getPath());
        if(daoConfig==null){
            daoConfig=new DbManager.DaoConfig()
                    .setDbName("Pletter.db")
                    .setDbDir(file)
                    .setDbVersion(2)
                    .setAllowTransaction(true)
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                            try {
                                db.dropTable(talkOneTone.class);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
        return daoConfig;
    }
    /**
     * 创建通知列表需要适配的数据表
     * @return
     */
    public static DbManager.DaoConfig getMessageListDaoConfig(){
        File file=new File(Environment.getExternalStorageDirectory().getPath());
        if(daoConfig==null){
            daoConfig=new DbManager.DaoConfig()
                    .setDbName("MessageTable.db")
                    .setDbDir(file)
                    .setDbVersion(2)
                    .setAllowTransaction(true)
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                            try {
                                // sp.edit().putInt("hongdian", 0).commit();
                                db.dropTable(messagelist.class);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
        return daoConfig;
    }


    //创建通知的数据库(存储所有接收到的数据)
    public static DbManager.DaoConfig getMessageList(){
        File file=new File(Environment.getExternalStorageDirectory().getPath());
        if(daoConfig==null){
            daoConfig=new DbManager.DaoConfig()
                    .setDbName("MessageTableAll.db")
                    .setDbDir(file)
                    .setDbVersion(2)
                    .setAllowTransaction(true)
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                            try {
                                db.dropTable(messagelistAll.class);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
        return daoConfig;
    }
}