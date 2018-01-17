package cn.edu.hznu.me;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalendarActivity extends AppCompatActivity {

    private TextView back_to_punch;
    private TextView mTextSelectMonth;
    private ImageButton mLastMonthView;
    private ImageButton mNextMonthView;
    private CalendarView mCalendarView;

    private MyDatabaseHelper dbHelper;
    private Cursor cursor;
    private SQLiteDatabase db;
    private String seed_name;

    List<String> mData = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Intent intent = getIntent();
        seed_name = intent.getStringExtra("seed_name");


        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();

        back_to_punch = (TextView)findViewById(R.id.back_to_punch);
        back_to_punch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mTextSelectMonth = (TextView) findViewById(R.id.txt_select_month);
        mLastMonthView = (ImageButton) findViewById(R.id.img_select_last_month);
        mLastMonthView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.setLastMonth();
                mTextSelectMonth.setText(mCalendarView.getDate());
            }
        });
        mNextMonthView = (ImageButton) findViewById(R.id.img_select_next_month);
        mNextMonthView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.setNextMonth();
                mTextSelectMonth.setText(mCalendarView.getDate());
            }
        });

        mCalendarView = (CalendarView) findViewById(R.id.calendar);
        mData = new ArrayList<>();

        dbHelper = new MyDatabaseHelper(this, "Seed.db", null, 1);
        db = dbHelper.getWritableDatabase();
        //查询punch表中该习惯的打卡情况
        cursor = db.query("Punch", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(cursor.getColumnIndex("name"));
                if(name.equals(seed_name)) {
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    String regEx="[^0-9]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(time);
                    mData.add(m.replaceAll("").trim());
                }
            }while(cursor.moveToNext());
        }
        cursor.close();

//        mData = new ArrayList<>();
//        mData.add("20160801");
//        mData.add("20160802");
//        mData.add("20160803");
//        mData.add("20160816");
//        mData.add("20160817");
//        mData.add("20160826");
//        mData.add("20160910");
//        mData.add("20160911");
//        mData.add("20160912");
//        mData.add("20171224");
        mCalendarView.setOptionalDate(mData);

        // 设置点击事件
        mCalendarView.setOnClickDate(new CalendarView.OnClickListener() {
            @Override
            public void onClickDateListener(int year, int month, int day) {
                Toast.makeText(getApplication(), year + "年" + month + "月" + day + "天", Toast.LENGTH_SHORT).show();

                // 获取已选择日期
                List<String> dates = mCalendarView.getSelectedDates();
                for (String date : dates) {
                    Log.d("test", "date: " + date);
                }
            }
        });

        mTextSelectMonth.setText(mCalendarView.getDate());

    }
}

