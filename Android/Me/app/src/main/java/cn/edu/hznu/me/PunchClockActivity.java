package cn.edu.hznu.me;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/*
 更新打卡的两种情况：
 （1）人为的主动打卡
 （2）判断是不是已经第二天了   在启动页面进行判断
 */


public class PunchClockActivity extends AppCompatActivity {

    private Button back_to_main;
    private ImageView for_punch;

    private TextView this_time;
    private TextView habit_title;
    private int punch;
    private int days;
    private String punchTime;
    private TextView habit_days;

    private MyDatabaseHelper dbHelper;
    private Cursor cursor;
    private SQLiteDatabase db;
    private String seed_name;

    private MyApplication application;   //全局变量的使用

    private TextView for_time;
    private TextView longitude;
    private String habit_latitude;
    private String habit_longitude;

    private ImageView imageView;
    private Calendar cal;
    private String year;
    private String month;
    private String day;
    private String sow_time;

    private static final int msgKey1 = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punch_clock);

        //获取到当前是哪个习惯
        Intent intent = getIntent();
        seed_name = intent.getStringExtra("seed_name");
        habit_title = (TextView)findViewById(R.id.habit_title);
        habit_title.setText(seed_name);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();

        back_to_main = (Button)findViewById(R.id.back_to_main);
        back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PunchClockActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //首先活动一启动就要判断今天是否已经打卡
        for_punch = (ImageView)findViewById(R.id.for_punch);
        dbHelper = new MyDatabaseHelper(this, "Seed.db", null, 1);
        db = dbHelper.getWritableDatabase();
        //查询habit表中该习惯的打卡情况
        cursor = db.query("Habit", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(cursor.getColumnIndex("name"));
                if(name.equals(seed_name)) {
                    punch = cursor.getInt(cursor.getColumnIndex("punch"));
                    sow_time = cursor.getString(cursor.getColumnIndex("time"));
                    punchTime = cursor.getString(cursor.getColumnIndex("punch_time"));
                    days = cursor.getInt(cursor.getColumnIndex("days"));
                    habit_latitude = cursor.getString(cursor.getColumnIndex("latitude"));
                    habit_longitude = cursor.getString(cursor.getColumnIndex("longitude"));
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        if(punch == 0){
            for_punch.setTag("" + R.drawable.not_punch);
        }
        else if(punch == 1){
            for_punch.setImageResource(R.drawable.is_punch);
            for_punch.setTag("" + R.drawable.is_punch);
        }

        imageView = (ImageView)findViewById(R.id.imageView);
        if(days >= 0 && days <10 ){
            imageView.setImageResource(R.drawable.plant1);
        }
        else if(days >= 10 && days <20){
            imageView.setImageResource(R.drawable.plant2);
        }
        else if(days >= 20){
            imageView.setImageResource(R.drawable.plant3);
        }

        dbHelper = new MyDatabaseHelper(this, "Seed.db", null, 1);
        db = dbHelper.getWritableDatabase();

        //根据人为打卡与否修改punch
        for_punch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = for_punch.getTag();
                String rTag = (String) tag;
                // 判断类型
                if(rTag.equals("" + R.drawable.not_punch)){
                    for_punch.setImageResource(R.drawable.frame_anim1);
                    AnimationDrawable animationDrawable1 = (AnimationDrawable) for_punch.getDrawable();
                    animationDrawable1.start();
                    for_punch.setTag("" + R.drawable.is_punch);

                    ContentValues values = new ContentValues();

                    SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    String str = formatter.format(curDate);
                    values.put("punch", 1);
                    values.put("punch_time", str);
                    if(days == 0){
                        values.put("days", days + 1);
                    }
                    else {
                        //判断前一天是否打卡，即punchTime是否为昨天
                        if(isYesterday(punchTime)){
                                //更新数据库中的内容
                                //Toast.makeText(PunchClockActivity.this, "yesterday", Toast.LENGTH_LONG).show();
                                values.put("days", days + 1);
                        }
                        else{
                                values.put("days", 1);
                        }
                    }

                    db.update("Habit", values, "name = ?", new String[]{seed_name});

                    values.clear();
                    values.put("name", seed_name);
                    values.put("time", str);
                    db.insert("Punch", null, values);

                    //打卡一次，可以收获1个种子币
                    application = (MyApplication)PunchClockActivity.this.getApplicationContext();
                    int money = application.getNumber();
                    money = money + 1;
                    application.setNumber(money);

                    Toast.makeText(PunchClockActivity.this, "打卡成功", Toast.LENGTH_SHORT).show();
                } else if(rTag.equals("" + R.drawable.is_punch)) {
                    showAlerDialog();

                }
            }
        });

        habit_days = (TextView)findViewById(R.id.habit_days);
        habit_days.setText("已坚持" + String.valueOf(days) + "天>");
        habit_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PunchClockActivity.this, CalendarActivity.class);
                intent.putExtra("seed_name",seed_name);
                startActivity(intent);
            }
        });

        //获取系统当前的时间
        this_time = (TextView)findViewById(R.id.this_time);
        cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        day = String.valueOf(cal.get(Calendar.DATE));
        String time = month + "月" + day + "日";
        this_time.setText(time);

        for_time = (TextView)findViewById(R.id.for_time);
        new TimeThread().start();

    }


    private void showAlerDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("确认取消今天的打卡吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        for_punch.setImageResource(R.drawable.not_punch);
                        for_punch.setTag("" + R.drawable.not_punch);
                        //更新数据库中的内容
                        ContentValues values = new ContentValues();
                        values.put("punch", 0);
                        values.put("days", days - 1);
                        db.update("Habit", values, "name = ?", new String[] {seed_name});

                        db.delete("Punch", "name = ?", new String[]{seed_name});

                        //更新种子币数
                        application = (MyApplication) PunchClockActivity.this.getApplicationContext();
                        int money = application.getNumber();
                        application.setNumber(money - 1);
                    }
                })
                .setNegativeButton("暂不",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){}
                })
                .create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColorStateList(R.color.alertFont));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColorStateList(R.color.alertFont));
    }

    //判断是否为昨天
    public boolean isYesterday(String last_time){
        boolean b = false;

        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy年MM月dd日");
        Calendar ca2=Calendar.getInstance();//当前日期
        ca2.add(Calendar.DATE,-1);//减一天，变为10
        String now_time = sdf3.format(ca2.getTime());
        Toast.makeText(PunchClockActivity.this, now_time, Toast.LENGTH_LONG).show();

        if(now_time.equals(last_time)){
            b = true;
        }
        return b;
    }

    public class TimeThread extends  Thread{
        @Override
        public void run() {
            super.run();
            do{
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case msgKey1:
                    long time = System.currentTimeMillis();
                    Date date = new Date(time);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String timeExpend = getTimeExpend(sow_time, format.format(date));
                    for_time.setText(timeExpend);
                    break;
                default:
                    break;
            }
        }
    };

    private String getTimeExpend(String startTime, String endTime){
        long longStart = getTimeMillis(startTime); //获取开始时间毫秒数
        long longEnd = getTimeMillis(endTime);  //获取结束时间毫秒数
        long longExpend = longEnd - longStart;  //获取时间差
        return formatTime(longExpend);
    }

    private long getTimeMillis(String strTime) {
        long returnMillis = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date d = null;
        try {
            d = sdf.parse(strTime);
            returnMillis = d.getTime();
        } catch (ParseException e) {
            Toast.makeText(PunchClockActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return returnMillis;
    }

    //将毫秒转换为天时分秒
    public static String formatTime(long ms) {

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        return strDay + " : " + strHour + " : " + strMinute + " : " + strSecond;
    }
}
