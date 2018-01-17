package cn.edu.hznu.labmediaplayer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    private List<Music> musicList = new ArrayList<>();
    private MusicAdapter adapter;
    private ListView listView;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Music music;
    private TextView music_title;
    private TextView music_time;
    private int nowPos = -1;      //标记位，用来表示当前选中歌曲的位置,从0开始
    private ImageView img;
    private Animation rotate;

    //底部的五个按钮
    private Button pause;
    private Button play;
    private Button previous;
    private Button stop;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
        }else{
            readMusic();
        }

        adapter = new MusicAdapter(MainActivity.this, R.layout.music_item, musicList);
        adapter.setNowPos(nowPos);
        listView = (ListView)findViewById(R.id.music_list);
        listView.setAdapter(adapter);

        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        img = (ImageView) findViewById(R.id.img);

        //添加列表的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                try {
                    nowPos = position;
                    adapter.setNowPos(nowPos);
                    adapter.notifyDataSetChanged();
                    playMusic();
                    setMessage();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //添加按钮事件
        pause = (Button) findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.clearAnimation();
                mediaPlayer.pause();
            }
        });
        play = (Button) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                img.setAnimation(rotate);
                img.startAnimation(rotate);
                mediaPlayer.start();
            }
        });
        previous = (Button) findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                try {
                    if(nowPos == 0)
                        nowPos = listView.getCount()-1;
                    else
                        nowPos = nowPos - 1;
                    adapter.setNowPos(nowPos);
                    adapter.notifyDataSetChanged();
                    /*listView.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.smoothScrollToPosition(nowPos);
                        }
                    });*/
                    listView.setSelection(nowPos);
                    playMusic();
                    setMessage();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                img.clearAnimation();
                mediaPlayer.stop();
            }
        });
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                try {
                    nowPos = (nowPos + 1) % (listView.getCount());
                    adapter.setNowPos(nowPos);
                    adapter.notifyDataSetChanged();
                    /*listView.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.smoothScrollToPosition(nowPos);
                        }
                    });*/
                    listView.setSelection(nowPos);
                    playMusic();
                    setMessage();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    nowPos = (nowPos + 1) % (listView.getCount());
                    adapter.setNowPos(nowPos);
                    adapter.notifyDataSetChanged();
                    listView.setSelection(nowPos);
                    playMusic();
                    setMessage();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void readMusic(){
        Cursor cursor = null;
        int count = 1;    //count用来记录歌曲的id
        try{
            cursor =
                    getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            if(cursor.moveToFirst()){
                do{
                    //歌曲id
                    int id = count;
                    count++;
                    //歌曲的名称
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                    //歌曲的歌手名
                    String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    //歌曲的文件路径
                    String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    //歌曲的总播放时长（单位毫秒）
                    int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    int second = duration / 1000;
                    int minute = second / 60;
                    String time;
                    if(minute < 10){
                        time = "0" + minute;
                    }else{
                        time = String.valueOf(minute);
                    }
                    second = second - minute*60;
                    if(second < 10){
                        time = time + ":0" + second;
                    }else{
                        time = time + ":" + second;
                    }

                    String music = String.valueOf(id) + "." + title + " - " + artist + "  " + time;
                    Music contact_item = new Music(music, url, title, time);
                    musicList.add(contact_item);

                }while(cursor.moveToNext());
                adapter.notifyDataSetChanged();
             }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (cursor != null){
                cursor.close();
            }
        }
    }

    /*public View getViewByPosition(int pos, ListView listView) {
        int firstListItemPosition, lastListItemPosition;
        firstListItemPosition = listView.getFirstVisiblePosition();
        lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        Log.d("MainActivity", String.valueOf(nowPos));
        Log.d("MainActivity", String.valueOf(firstListItemPosition) + " " + String.valueOf(lastListItemPosition) + " " + listView.getChildCount());

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }*/

    //关于mediaPlayer的一系列操作
    private void playMusic(){
        try {
            img.setAnimation(rotate);
            img.startAnimation(rotate);
            mediaPlayer.stop();
            mediaPlayer.reset();
            music = musicList.get(nowPos);
            mediaPlayer.setDataSource(music.getUrl());
            mediaPlayer.prepare();
            mediaPlayer.start();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //修改底部栏的信息
    private void setMessage(){
        /*View v = getViewByPosition(lastPos, listView);
        v.setBackgroundResource(R.color.notSelect);
        v = getViewByPosition(nowPos, listView);
        v.setBackgroundResource(R.color.isSelect);*/

        music_title = (TextView) findViewById(R.id.music_title);
        music_title.setText(music.getTitle());
        music_time = (TextView) findViewById(R.id.music_time);
        music_time.setText(music.getTime());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readMusic();
                }
                break;
            default:
        }
    }

}
