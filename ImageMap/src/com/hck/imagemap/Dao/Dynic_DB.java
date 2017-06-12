package com.hck.imagemap.Dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Dynic_DB extends SQLiteOpenHelper
{
    static final String DB_NAME = "Dynic.db";
    static final String TABLE_NAME = "Dynic";
    static final String ID = "id";
    static final String NUM = "num";
    static final String WUCHA = "wucha";
    static final String T = "T";
    static final String TOF = "TOF";
    static final String FOT = "FOT";
    static final String TEN = "Ten";
    static final String TIME = "time";

    public Dynic_DB(Context context)
    {
        super(context, DB_NAME, null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // TODO Auto-generated method stub
        String sql = "create table if not exists " + TABLE_NAME + "(" + ID
                + " integer primary key autoincrement, " + NUM + " integer, "
                + WUCHA + " varcher(20)," + T + " varcher(20)," + TOF
                + " varcher(20)," + FOT + " varcher(20)," + TEN
                + " varcher(20)," + TIME + " varcher(20))";
        db.execSQL(sql);
        initData(db);
    }

    private void initData(SQLiteDatabase db)
    {
    }

    public int saveDynicResult(Dynic d)
    {
        ContentValues values = new ContentValues();
        values.put(NUM, d.getNum());
        values.put(WUCHA, d.getWucha());
        values.put(T, d.getT());
        values.put(TOF, d.getTOF());
        values.put(FOT, d.getFOT());
        values.put(TEN, d.getTen());
        values.put(TIME, d.getTime());
        SQLiteDatabase db = getWritableDatabase();
        long insert = db.insert(TABLE_NAME, null, values);
        return (int) insert;
    }

    public ArrayList<Dynic> queryAll()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Dynic> arrayDynic = new ArrayList<Dynic>();
        while (c.moveToNext())
        {
            int num = c.getInt(c.getColumnIndex(NUM));
            String wucha = c.getString(c.getColumnIndex(WUCHA));
            String t = c.getString(c.getColumnIndex(T));
            String tof = c.getString(c.getColumnIndex(TOF));
            String fot = c.getString(c.getColumnIndex(FOT));
            String ten = c.getString(c.getColumnIndex(TEN));
            String time = c.getString(c.getColumnIndex(TIME));
            Dynic dynic = new Dynic(num, wucha, t, tof, fot, ten, time);
            arrayDynic.add(dynic);
        }
        return arrayDynic;
    }

    public int delete(String time)
    {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete(TABLE_NAME, TIME + "=?", new String[] { time });
        return delete;
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
    {
        // TODO Auto-generated method stub

    }

}
