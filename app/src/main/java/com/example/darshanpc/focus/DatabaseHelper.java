package com.example.darshanpc.focus;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String database="database.db";

    public DatabaseHelper(Context context) {
        super(context, database,null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE toggle(value VARCHAR(2))");
        sqLiteDatabase.execSQL("CREATE TABLE voiceentry(value TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS toggle");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS voiceentry");
        onCreate(sqLiteDatabase);
    }

    public void voice_toggle(String value)
    {
        SQLiteDatabase sql=this.getWritableDatabase();
        if(value.equals("0"))
        {
            sql.execSQL("DELETE FROM toggle WHERE value='1'");
        }
        else if(value.equals("1"))
        {
            sql.execSQL("DELETE FROM toggle WHERE value='0'");
        }
        ContentValues contentValues=new ContentValues();
        contentValues.put("value",value);
        sql.insert("toggle",null,contentValues);
    }

    public void init_voice_toggle(String value)
    {
        SQLiteDatabase sql=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("value",value);
        sql.insert("toggle",null,contentValues);
    }

    public String get_voice_toggle_value()
    {
        String toggle="";
        SQLiteDatabase sql=this.getWritableDatabase();
        Cursor cursor=sql.rawQuery("SELECT * FROM toggle",null);
        while (cursor.moveToNext())
        {
            toggle=cursor.getString(0);
            Log.i("toggle_value",toggle);
        }
        cursor.close();
        return toggle;
    }

    public void add_entry(String entry)
    {
        SQLiteDatabase sql=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("value",entry);
        sql.insert("voiceentry",null,contentValues);
    }

    public Cursor get_entry()
    {
        SQLiteDatabase sql=this.getWritableDatabase();
        Cursor cursor=sql.rawQuery("SELECT * FROM voiceentry",null);
        return cursor;
    }
    public List get_entry_for_voice()
    {
        List entrylist=new ArrayList();
        int temp=0;
        SQLiteDatabase sql=this.getWritableDatabase();
        Cursor cursor=sql.rawQuery("SELECT * FROM voiceentry",null);
        while (cursor.moveToNext())
        {
            entrylist.add(cursor.getString(0));
            temp++;
        }
        return entrylist;
    }
    public void delete_entry(String entry)
    {
        SQLiteDatabase sql=this.getWritableDatabase();
        Log.i("entry",entry);
        sql.execSQL("DELETE FROM voiceentry WHERE value='"+entry+"'");
    }
}
