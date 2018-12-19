package com.example.root.androidcebimdebylkerdurmaz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Veritabani extends SQLiteOpenHelper {

    public Veritabani(Context context) {
        super(context, "veritabani",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table cebimde(id integer not null primary key autoincrement,tarih text,dakika integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists cebimde");
        onCreate(db);
    }


    public Cursor listele()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            Cursor cursor=db.rawQuery("select * from cebimde",null);
            return cursor;
        }
        catch (Exception e)
        {

        }
        return null;
    }

    public void sil(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.delete("cebimde","id=?",new String[]{id});
        }
        catch (Exception e)
        {

        }
    }

    public void sil()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.execSQL("delete from cebimde");
        }
        catch (Exception e)
        {

        }
    }
    public void dakikaEkle(String tarih,int yeniDakika)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        int eskiDakika=0,guncelDakika;
        try {
            cursor = db.rawQuery("select dakika from cebimde where tarih=?", new String[] {tarih});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                eskiDakika = cursor.getInt(cursor.getColumnIndex("dakika"));
                guncelDakika=eskiDakika+yeniDakika;
                ContentValues contentValues=new ContentValues();
                contentValues.put("dakika",guncelDakika);
                db.update("cebimde",contentValues, "tarih=?", new String[] {tarih});
            }
            else
            {
                ContentValues contentValues=new ContentValues();
                contentValues.put("tarih",tarih);
                contentValues.put("dakika",yeniDakika);
                db.insert("cebimde",null,contentValues);
            }

        }finally {
            cursor.close();
        }
        db.close();
    }

    public int bugunDakika(String tarih)
    {
        int dakika=0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select dakika from cebimde where tarih=?", new String[] {tarih});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                dakika = cursor.getInt(cursor.getColumnIndex("dakika"));
            }
        }finally {
            cursor.close();
        }
        return dakika;
    }
}
