package cn.edu.hznu.me;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class LaunchActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private Cursor cursor;
    private SQLiteDatabase db;
    private String last_time;
    private String now_time;

    private Calendar cal;
    private String year;
    private String month;
    private String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();
        //加载启动界面
        setContentView(R.layout.activity_launch);

        dbHelper = new MyDatabaseHelper(this, "Seed.db", null, 1);
        db = dbHelper.getWritableDatabase();
        cursor = db.query("Time", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                last_time = cursor.getString(cursor.getColumnIndex("time"));
            }while(cursor.moveToNext());
        }
        cursor.close();

        cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        year = String.valueOf(cal.get(Calendar.YEAR));
        month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        day = String.valueOf(cal.get(Calendar.DATE));
        now_time = year + "年" + month + "月" + day + "日";

        //不是今天
        if(!isToday()) {
            //更新打卡的记录
            Toast.makeText(LaunchActivity.this, last_time, Toast.LENGTH_LONG).show();
            ContentValues values = new ContentValues();
            values.put("punch", 0);
            db.update("Habit", values, null, null);
        }

        //更新时间
        ContentValues values = new ContentValues();
        values.put("time", now_time);
        db.update("Time", values, null, null);

        Integer time = 2000;    //设置等待时间，单位为毫秒
        Handler handler = new Handler();
        //当计时结束时，跳转至主界面
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                LaunchActivity.this.finish();
            }
        }, time);

    }

    //判断是不是今天
    public boolean isToday(){
        boolean b = false;
        if(now_time.equals(last_time)){
            b = true;
        }
        return b;
    }

}
