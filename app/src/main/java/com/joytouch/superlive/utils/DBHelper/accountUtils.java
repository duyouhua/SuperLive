package com.joytouch.superlive.utils.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Administrator on 5/6 0006.
 */
public class accountUtils {
    private static AccounDBHelper helper;
    private SQLiteDatabase db;
    private Context context;

    public accountUtils(Context context) {
        this.context=context;
    }

    public  void create(){
        /**
         * 当一运行,就会创建一个数据库/data/data/包名/account.db
         */
        //创建AccountDBHelper对象,创建数据库,但是不是真实存在的
        helper =new AccounDBHelper(context, "cjzb_area.db", null, 1);
        db=helper.getReadableDatabase();
        String sql = "`area_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '索引ID',\n" +
                "  `area_name` varchar(50) NOT NULL COMMENT '地区名称',\n" +
                "  `area_parent_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '地区父ID',\n" +
                "  `area_sort` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '排序',\n" +
                "  `area_deep` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '地区深度，从1开始',\n" +
                "  `is_del` tinyint(4) DEFAULT '0' COMMENT '是否删除0:未删除;1:已删除',\n" +
                "  `create_time` bigint(13) DEFAULT NULL COMMENT '创建时间',\n" +
                "  `update_time` bigint(13) DEFAULT NULL COMMENT '修改时间',\n" +
                "  PRIMARY KEY (`area_id`),\n" +
                "  KEY `area_parent_id` (`area_parent_id`) USING BTREE";
        db.execSQL(sql);
    }

    /**
     *插入数据
     */
    public void insert(){

        //创建一个真实的可读的数据库,是一个真实存在的数据库,在数据库满了的情况下,返回只读数据库对象

        //创建sql语句
        String sql="insert into mycount (my_userid, my_nickname, my_img,other_userid, nickname, image, time, content, status, left_right) values (?,?)";
        //执行sql语句
        //参数一:sql语句
        //参数二:bindArgs 条件值
        db.execSQL(sql, new String[]{"05", "hello7", "5,1ba3176842", "0243", "ko2jygg", "5,1ba3176842", "100090de251", "你山东人发帖", "1", "0"});
        db.close();
        Log.e("insret", "insert");
    }

    /**
     * 查询数据
     */
    public void query() {
        //创建一个真实的数据库,在数据库满了的情况下,返回只读对象
        String sql = "select name,money from account";
        //执行sql 插入的语句,查询返回的是cursor对象
        //bindArgs 条件值
        //rawQuery从数据库表中查询出原始的数据
        Cursor cursor = db.rawQuery(sql, null);
        // 循环取出数据,当cursor对象指向哪一行,则这一行的数据都保存在对象里面
        // cursor.moveToNext()让游标指向下一个数据,true表示指向了一条数据,false表示指向了结果集的结尾
        while (cursor.moveToNext()) {
            // 从cursor对象中取出列的值,根据列的索引,索引使用columns中的索引
            String name = cursor.getString(0);
            String money = cursor.getString(1);
            System.out.println();
            Log.e("query", "name=" + name + "; money=" + money);
        }
        cursor.close();
        db.close();
    }

    /**
     *
     * 修改数据
     */
    public void update(){
        //创建一个真实的数据库
        //创建sql语句
        String sql="update account set money=? where name=?";
        //执行sql语句
        db.execSQL(sql, new String[]{"60", "qg4"});
        db.close();
    }

    /**
     * 删除数据
     */
    public  void delete(){
        //创建一个真实的数据库
        //创建sql语句
        String sql="delete from account where name=?";
        //执行sql语句
        db.execSQL(sql,new String[]{"qg4"});
        db.close();
        Toast.makeText(context, "操作成功", 0).show();
    }

    /**
     *数据库事务:  转账
     */
    public  void  zz(View view){
        //创建一个真实的数据库
        try {
            //在一组业务操作之前开启事物,保持操作一致,都成功或者失败
            db.beginTransaction();
            String sql="update account set money=? where name=?";
            db.execSQL(sql,new String[]{"20000","lf"});
            db.execSQL(sql,new String[]{"3000","qg"});
            //告诉数据库使用已经成功
            db.setTransactionSuccessful();
        } catch (Exception e) {
        }finally{
            //一组事物结束后结束事物
            db.endTransaction();
            db.close();
        }

    }
}
