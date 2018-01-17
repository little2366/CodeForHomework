package cn.edu.hznu.me;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SetAlarmActivity extends AppCompatActivity implements View.OnClickListener {

    private Button back_to_habit;
    private TextView finish_set_alarm;

    private TextView forSwitch;
    private TextView mInTime;
    private LinearLayout setTime;
    private AlarmsSetting alarmsSetting;
    private GridView gridview;
    private WeekGridAdpter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        back_to_habit = (Button) findViewById(R.id.back_to_habit);
        back_to_habit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetAlarmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        finish_set_alarm = (TextView) findViewById(R.id.finish_set_alarm);
        finish_set_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetAlarmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        alarmsSetting = new AlarmsSetting(this);

        forSwitch = (TextView) findViewById(R.id.forSwitch);

        initView();
    }

    private void initView() {
        //初始化一个AlarmsSetting
        alarmsSetting = new AlarmsSetting(this);


        //设置是否开启提醒
        forSwitch = (TextView) findViewById(R.id.forSwitch);
        forSwitch.setOnClickListener(this);
        forSwitch.setSelected(alarmsSetting.isInEnble() ? true : false);

        //设置提醒的时间
        setTime = (LinearLayout) findViewById(R.id.setTime);
        setTime.setOnClickListener(this);

        mInTime = (TextView) findViewById(R.id.set_time);
        setTime(alarmsSetting.getInHour(), alarmsSetting.getInMinutes());

        //设置提醒的时间
        gridview = (GridView) findViewById(R.id.gridview);
        gridAdapter = new WeekGridAdpter(this, alarmsSetting, 1);
        gridview.setAdapter(gridAdapter);
    }

    //用来设置时间选择框中的日期
    public void setTime(int hour, int minute) {
        String mHour = "" + hour;
        String mMinute = "" + minute;
        if (hour / 10 == 0) mHour = "0" + mHour;
        if (minute / 10 == 0) mMinute = "0" + mMinute;
        mInTime.setText(mHour + ":" + mMinute);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forSwitch:
                if (v.isSelected()) {
                    alarmsSetting.setInEnble(false);
                    v.setSelected(false);
                    AlarmOpreation.cancelAlert(SetAlarmActivity.this, 1);
                } else {
                    alarmsSetting.setInEnble(true);
                    v.setSelected(true);
                    AlarmOpreation.enableAlert(SetAlarmActivity.this, 1, alarmsSetting);
                }
                break;
            case R.id.setTime:
                showTimePickerDialog();
                break;
        }
    }

    public void showTimePickerDialog() {
        TimePickerFragment timePicker = new TimePickerFragment();
        timePicker.setTime(alarmsSetting.getInHour(), alarmsSetting.getInMinutes());
        timePicker.show(getFragmentManager(), "timePicker");
        timePicker.setOnSelectListener(new TimePickerFragment.OnSelectListener() {
            @Override
            public void getValue(int hourOfDay, int minute) {

                alarmsSetting.setInHour(hourOfDay);
                alarmsSetting.setInMinutes(minute);
                setTime(hourOfDay, minute);
                AlarmOpreation.cancelAlert(SetAlarmActivity.this, 1);
                AlarmOpreation.enableAlert(SetAlarmActivity.this, 1, alarmsSetting);
            }
        });
    }
}
