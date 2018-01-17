package cn.edu.hznu.me;

/**
 * Created by asus on 2017/12/15.
 */

public class Memory {
    private String memory_name; //习惯名
    private String memory_time;
    private String memory_note;   //记录的内容
    private String memory_img;

    public Memory(String name, String time, String note, String img ){
        this.memory_name = name;
        this.memory_time = time;
        this.memory_note = note;
        this.memory_img = img;
    }
    public String getName(){
        return this.memory_name;
    }
    public String getTime(){
        return this.memory_time;
    }
    public String getNote(){
        return this.memory_note;
    }
    public String getImg(){
        return this.memory_img;
    }
}
