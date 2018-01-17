package cn.edu.hznu.me;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by asus on 2017/12/20.
 */

public class MyApplication extends Application {
    private int money;

    private MyDatabaseHelper dbHelper;
    private Cursor cursor;
    private SQLiteDatabase db;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    public int getNumber() {
        dbHelper = new MyDatabaseHelper(this, "Seed.db", null, 1);
        db = dbHelper.getWritableDatabase();
        cursor = db.query("Me", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                //遍历Cursor对象，取出数据
                money = cursor.getInt(cursor.getColumnIndex("money"));

            }while(cursor.moveToNext());
        }
        cursor.close();
        return money;
    }

    public void setNumber(int money) {
        dbHelper = new MyDatabaseHelper(this, "Seed.db", null, 1);
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("money", money);
        db.update("Me", values, "name = ?", new String[]{"little"});
        this.money = money;
    }
}
