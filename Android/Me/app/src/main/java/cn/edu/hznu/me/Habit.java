package cn.edu.hznu.me;

/**
 * Created by asus on 2017/12/5.
 */

public class Habit {
    private String name;   //习惯名称
    private int days;     //已坚持的天数
    private int punch;

    public Habit( String name, int days, int punch){
        this.name = name;
        this.days = days;
        this.punch = punch;
    }
    public String getName(){
        return this.name;
    }
    public int getDays(){
        return this.days;
    }
    public int getPunch(){
        return this.punch;
    }
}
