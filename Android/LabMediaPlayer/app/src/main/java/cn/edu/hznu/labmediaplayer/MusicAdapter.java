package cn.edu.hznu.labmediaplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asus on 2017/11/29.
 */

public class MusicAdapter extends ArrayAdapter<Music> {

    private int resourceId;
    private int nowPos;

    public MusicAdapter(Context context, int textViewResourceId, List<Music> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    public void setNowPos(int nowPos){
        this.nowPos = nowPos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Music music = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        //view.setBackgroundResource(R.color.notSelect);
        if(position == nowPos)
            view.setBackgroundResource(R.color.isSelect);
        TextView musicMessage = (TextView) view.findViewById(R.id.music);
        musicMessage.setText(music.getMusic());
        return view;
    }

}

