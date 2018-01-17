package cn.edu.hznu.me;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by asus on 2017/12/14.
 */

public class SeedAdapter extends RecyclerView.Adapter<SeedAdapter.ViewHolder> implements View.OnClickListener{
    private List<Seed> mSeedList;
    private OnItemClickListener mOnItemClickListener = null;

    private  String[]  datas;
    public SeedAdapter(String[] datas) {
        this.datas = datas;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View seedView;
        ImageView seed_pic;
        TextView seed_name;

        public ViewHolder(View view){
            super(view);
            seedView = view;
            seed_name = (TextView)view.findViewById(R.id.seed_name);
            seed_pic = (ImageView)view.findViewById(R.id.seed_pic);
        }
    }

    public SeedAdapter(List<Seed> seedList){
        mSeedList = seedList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seed_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        /*holder.seedView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                Seed seed = mSeedList.get(position);
                Intent intent = new Intent(SeedAdapter, SowActivity.class);
                startActivity(intent);
            }
        });*/
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Seed seed = mSeedList.get(position);
        holder.seed_name.setText(seed.getName());
        holder.itemView.setTag(position);
        holder.seed_pic.setImageResource(seed.getPic());
    }

    @Override
    public int getItemCount(){
        return mSeedList.size();
    }

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


}
