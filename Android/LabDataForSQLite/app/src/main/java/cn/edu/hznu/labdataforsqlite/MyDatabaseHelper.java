package cn.edu.hznu.labdataforsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by asus on 2017/11/14.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper{

    public static final String CREATE_CONTACT = "create table contact ("
            + "id integer primary key autoincrement,"
            + "name text,"
            + "mobile text)";


    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //execSQL()执行建表
        db.execSQL(CREATE_CONTACT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        switch(oldVersion) {
            case 1:
                db.execSQL(CREATE_CONTACT);
            case 2:
            default:
        }
    }
}
