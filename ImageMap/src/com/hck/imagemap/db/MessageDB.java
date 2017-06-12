package com.hck.imagemap.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessageDB extends SQLiteOpenHelper
{

    public MessageDB(Context context)
    {
        // 参数一：上下文对象，参数二：数据库名称，参数三：游标工厂对象，null表示使用系统默认的，参数四：当前数据库的版本号
        super(context, "jingdu.db", null, 1);
    }

    /**
     * 数据库第一次被创建的时候执行 oncreate(). 一般用于指定数据库的表结构
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table if not exists _result (_id integer primary key autoincrement, _num integer, _wucha varcher(20),_T varcher(20),_TOF varcher(20),_FOT varcher(20),_Ten varcher(20),_time varcher(20))");
    }

    /**
     * 当数据库的版本号升级的时候 调用的方法. 一般用于升级程序后,更新数据库的表结构.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

}