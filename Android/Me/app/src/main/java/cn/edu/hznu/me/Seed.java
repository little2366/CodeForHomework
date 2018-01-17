package cn.edu.hznu.me;

/**
 * Created by asus on 2017/12/14.
 */

public class Seed {
    private String seed_name;  //种子名称
    private int seed_pic;   //种子图片

    public Seed(String seed_name, int seed_pic) {
        this.seed_name = seed_name;
        this.seed_pic = seed_pic;
    }
    public String getName(){
        return this.seed_name;
    }
    public int getPic(){
        return this.seed_pic;
    }
}
