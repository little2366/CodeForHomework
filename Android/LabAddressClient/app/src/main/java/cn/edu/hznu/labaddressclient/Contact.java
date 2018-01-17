package cn.edu.hznu.labaddressclient;

/**
 * Created by asus on 2017/11/14.
 */

public class Contact {
    private String name;
    private String mobile;

    public Contact(String name, String mobile){
        this.name = name;
        this.mobile = mobile;
    }
    public String getName(){
        return name;
    }
    public String getMobile(){
        return mobile;
    }
}
