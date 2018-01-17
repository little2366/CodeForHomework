package cn.edu.hznu.labdataforsqlite;

/**
 * Created by asus on 2017/11/14.
 */

public class contact {
    private String name;
    private String mobile;
    private boolean isCheck;

    public contact(String name, String mobile){
        this.name = name;
        this.mobile = mobile;
    }
    public String getName(){
        return name;
    }
    public String getMobile(){
        return mobile;
    }
    public void setCheck(boolean isCheck){
        this.isCheck = isCheck;
    }
    public boolean getIsCheck(){
        return isCheck;
    }
}
