package cn.edu.hznu.labbroadcast;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/11/1.
 * ActivityCollector用于管理所有的活动
 */

public class ActivityCollector {

    /*创建一个用来存储当前所有活动的列表*/
    public static List<Activity> activities = new ArrayList<>();

    /*添加活动*/
    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    /*删除活动*/
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    /*关闭所有活动*/
    public static void finishAll(){
        for(Activity activity : activities){
            /*若当前活动没有停止，则强制停止该活动*/
            if( !activity.isFinishing() ){
                 activity.finish();
            }
        }
        /*清除所有的活动*/
        activities.clear();
    }
}
