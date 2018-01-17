package cn.edu.hznu.me;

import android.content.Context;

public class AlarmsSetting {

    //是否开启提醒
    public static final String ALARM_SETTING_ENBLE = "alarm_setting_enble";
    public static final String ALARM_SETTING_HOUR = "alarm_setting_hour";
    public static final String ALARM_SETTING_MINUTES = "alarm_setting_minutes";
    public static final String ALARM_SETTING_DAYSOFWEEK = "alarm_setting_daysofweek";


    public static final String ALARM_SETTING_NEXT_ALARM="alarm_next_alarm";

    public static final String ALARM_ALERT_ACTION = "com.kidcare.alarm_alert";

    //类似键值对，从中获取数据或读取数据
    private SharedPreferenceUtil spUtil;

    public AlarmsSetting(Context context) {
        spUtil = SharedPreferenceUtil.getInstance(context);
    }

    public boolean isInEnble() {
        return spUtil.getBoolean(ALARM_SETTING_ENBLE , false);
    }

    public void setInEnble(boolean isShake) {
        spUtil.putBoolean(ALARM_SETTING_ENBLE, isShake);
    }

    public int getInHour() {
        return spUtil.getInt(ALARM_SETTING_HOUR);
    }

    public void setInHour(int hour) {
        spUtil.putInt(ALARM_SETTING_HOUR, hour);
    }

    public int getInMinutes() {
        return spUtil.getInt(ALARM_SETTING_MINUTES);
    }

    public void setInMinutes(int minutes) {
        spUtil.putInt(ALARM_SETTING_MINUTES, minutes);
    }

    public int getInDays() {
        return spUtil.getInt(ALARM_SETTING_DAYSOFWEEK);
    }

    public void setInDays(int days) {
        spUtil.putInt(ALARM_SETTING_DAYSOFWEEK, days);
    }


    public long getNextAlarm() {
        return spUtil.getLong(ALARM_SETTING_NEXT_ALARM);
    }

    public void setNextAlarm(long timestamp) {spUtil.putLong(ALARM_SETTING_NEXT_ALARM, timestamp);}
}
