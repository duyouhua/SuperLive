package com.joytouch.superlive.utils;

import android.content.Context;
import android.os.Environment;

import com.joytouch.superlive.javabean.TableChatLottery;

import org.xutils.DbManager;
import org.xutils.DbManager.DaoConfig;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by yj on 2016/5/29.
 * 数据库工具表
 */
public class DBUtils {
    DbManager manager;
    public DBUtils(Context context) {
        File file=new File(Environment.getExternalStorageDirectory().getPath());
        DaoConfig config = new DaoConfig();
        config.setDbName("lottery"); //db名
        config.setDbVersion(1);  //db版本
        config.setDbDir(file);
        config.setAllowTransaction(true);
        manager = x.getDb(config);
    }
    public void save(TableChatLottery table ){
        try {
            manager.save(table);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    public TableChatLottery selector(TableChatLottery where){
        List<TableChatLottery> users = null;
        try {
            users = manager.selector(TableChatLottery.class)
                    .where("guessid","=",where.getGuessid() )
                    .and("roomid", "=", where.getRoomid())
                    .findAll();
            try {
                manager.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(users!=null&&users.size()>0) {
            return users.get(0);
        }else{
            return null;
        }
    }
    public void delete(){
        try {
            WhereBuilder builder = WhereBuilder.b("time","<",(System.currentTimeMillis()-12*60*60*1000));
            manager.delete(TableChatLottery.class,
                    builder);
            try {
                manager.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ;
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
