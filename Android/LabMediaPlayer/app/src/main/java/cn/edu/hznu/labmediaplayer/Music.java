package cn.edu.hznu.labmediaplayer;

/**
 * Created by asus on 2017/11/29.
 */

public class Music {
    private String music;
    private String url;
    private String title;
    private String time;

    public Music(String music, String url, String title, String time){
        this.music = music;
        this.url = url;
        this.title = title;
        this.time = time;
    }
    public String getMusic(){
        return music;
    }
    public String getUrl() {
        return url;
    }
    public String getTitle(){
        return  title;
    }
    public String getTime(){
        return time;
    }
}
