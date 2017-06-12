package com.hck.imagemap.Dao;

import java.util.ArrayList;
import java.util.List;

import com.hck.imagemap.db.MessageDB;
import com.hck.imagemap.entity.MessageContent;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MessageDao
{

    public MessageDB helper;

    public MessageDao(Context context)
    {
        helper = new MessageDB(context);
    }

    /**
     * 添加一条记录
     */
    public boolean add(int _num, String _wucha, String _T, String _TOF,
            String _FOT, String _Ten, String _time)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen())
        {
            // 执行添加数据的SQL语句
            db.execSQL(
                    "insert into _result (_num , _wucha ,_T ,_TOF ,_FOT ,_Ten,_time) values (?,?,?,?,?,?,?)",
                    new Object[] { _num, _wucha, _T, _TOF, _FOT, _Ten, _time });
            db.close();
            return true;
        }
        // 如果代码能够执行到这一步，说明上面的添加操作也执行了。所以查询的返回值必定为true
        else
        {
            return false;
        }
    }

    public void delete(String time)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen())
        {
            // 执行添加数据的SQL语句
            db.execSQL("delete from _result where _time=?",
                    new Object[] { time });
            db.close();
        }
    }

    public List<MessageContent> findAll()
    {
        // 定义好要返回的对象
        List<MessageContent> numbers = new ArrayList<MessageContent>();
        SQLiteDatabase db = helper.getReadableDatabase();
        if (db.isOpen())
        {
            // 查询blacknumber表中的所有号码
            Cursor cursor = db.rawQuery(
                    "select _num,_wucha,_T,_TOF,_FOT,_Ten,_time from _result",
                    null);
            // 循环遍历结果集，将每个结果集封装后添加到集合中
            while (cursor.moveToNext())
            {
                MessageContent contents = new MessageContent();
                contents.set_num(cursor.getInt(0));
                contents.set_wucha(cursor.getString(1));
                contents.set_T(cursor.getString(2));
                contents.set_TOF(cursor.getString(3));
                contents.set_FOT(cursor.getString(4));
                contents.set_Ten(cursor.getString(5));
                contents.set_time(cursor.getString(6));
                numbers.add(contents);
                contents = null;
            }
            cursor.close();
            db.close();
        }
        return numbers;
    }
}
