package cn.edu.hznu.me;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.DateFormat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by asus on 2017/12/5.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;
    private Calendar cal;
    private String year;
    private String month;
    private String day;

    private String[] habit_name = new String[] {"吃早饭", "多喝水", "早睡", "跑步", "画画", "看书", "摄影", "学英语", "早起", "背单词", "吃水果", "锻炼"};

    //习惯的相关数据
    public static final String CREATE_HABIT = "create table Habit ("
            + "id integer primary key autoincrement,"
            + "name text,"
            + "days integer,"
            + "time text,"
            + "punch integer,"
            + "punch_time text,"
            + "note text,"
            + "imgPath text,"
            + "latitude text,"
            + "longitude text)";

    //种子的相关数据
    public static final String CREATE_SEED = "create table Seeds ("
            + "id integer primary key autoincrement,"
            + "name text)";

    //种子币的相关数据
    public static final String CREATE_ME = "create table Me ("
            + "id integer primary key autoincrement,"
            + "name text,"
            + "money integer)";

    //时间的记录
    public static final String CREATE_TIME = "create table Time ("
            + "id integer primary key autoincrement,"
            + "time text)";

    //打卡日期的相关记录
    public static final String CREATE_PUNCH = "create table Punch ("
            + "id integer primary key autoincrement,"
            + "name text,"
            + "time text)";




    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //execSQL()执行建表
        db.execSQL(CREATE_HABIT);
        db.execSQL(CREATE_SEED);
        db.execSQL(CREATE_ME);
        db.execSQL(CREATE_TIME);
        db.execSQL(CREATE_PUNCH);
        Toast.makeText(mContext, "Create Succees", Toast.LENGTH_LONG).show();

        //向Seeds表中写入原有的数据项
        for(int i=0; i<habit_name.length; ++i){
            ContentValues values = new ContentValues();
            values.put("name", habit_name[i]);   //习惯的名称
            db.insert("Seeds", null, values);
        }

        //向Me表中给money赋初始值
        ContentValues values = new ContentValues();
        values.put("name", "little");
        values.put("money", 66);   //习惯的名称
        db.insert("Me", null, values);

        //向Time表中写入初始数据数据
        values.clear();
        cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        year = String.valueOf(cal.get(Calendar.YEAR));
        month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        day = String.valueOf(cal.get(Calendar.DATE));
        String time = year + "年" + month + "月" + day + "日";
        values.put("time", time);
        db.insert("Time", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        switch(oldVersion) {
            case 1:
                db.execSQL(CREATE_HABIT);
                db.execSQL(CREATE_SEED);
                db.execSQL(CREATE_ME);
                db.execSQL(CREATE_TIME);
            case 2:
            default:
        }
    }

}
