package cn.edu.hznu.me;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static cn.edu.hznu.me.R.id.habitName;
import static cn.edu.hznu.me.R.id.parent;

public class MainActivity extends AppCompatActivity {
    private List<Habit> myHabits = new ArrayList<>();
    private Button click_market;
    private Button click_me;
    private SwipeLayout listView;

    private MyDatabaseHelper dbHelper;
    private Cursor cursor;
    private SQLiteDatabase db;
    private HabitAdapter adapter;
    private TextView habitName;

    //存放图片的id
    private int[] habit_img = new int[]{
            R.drawable.breakfast,
            R.drawable.water,
            R.drawable.sleep,
            R.drawable.run,
            R.drawable.paint,
            R.drawable.read,
            R.drawable.photo,
            R.drawable.english,
            R.drawable.sun,
            R.drawable.word,
            R.drawable.fruit,
            R.drawable.sport
    };
    //存放习惯的名字
    private String[] habit_name = new String[] {"吃早饭", "多喝水", "早睡", "跑步", "画画", "看书", "摄影", "学英语", "早起", "背单词", "吃水果", "锻炼"};

    private ImageView set_alarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();

        click_market = (Button) findViewById(R.id.click_market);
        click_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HabitsMarketActivity.class);
                startActivity(intent);
            }
        });
        click_me = (Button) findViewById(R.id.click_me);
        click_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutMeActivity.class);
                startActivity(intent);
            }
        });

        dbHelper = new MyDatabaseHelper(this, "Seed.db", null, 1);

        db = dbHelper.getWritableDatabase();

        //查询habit表中所有的数据
        cursor = db.query("Habit", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                //遍历Cursor对象，取出数据
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int days = cursor.getInt(cursor.getColumnIndex("days"));
                int punch = cursor.getInt(cursor.getColumnIndex("punch"));

                Habit habit_item = new Habit(name, days, punch);
                myHabits.add(habit_item);

            }while(cursor.moveToNext());
        }
        cursor.close();

        //initHabits();
        adapter = new HabitAdapter(MainActivity.this, R.layout.habit_item, myHabits);
        listView = (SwipeLayout) findViewById(R.id.myHabits);
        listView.setAdapter(adapter);

        //添加列表的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(MainActivity.this, PunchClockActivity.class);
                habitName = (TextView)listView.getChildAt(position).findViewById(R.id.habitName);
                intent.putExtra("seed_name", habitName.getText().toString());
                startActivity(intent);
            }
        });


        set_alarm = (ImageView) findViewById(R.id.set_alarm);
        set_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "alarm", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, SetAlarmActivity.class);
                startActivity(intent);
            }
        });

    }

//    private void initHabits(){
//        Habit getUp = new Habit(R.drawable.sun, "早起", 9, R.drawable.check);
//        myHabits.add(getUp);
//        Habit english = new Habit(R.drawable.english, "学英语", 6, R.drawable.check);
//        myHabits.add(english);
//        Habit word = new Habit(R.drawable.word, "背单词", 15, R.drawable.check);
//        myHabits.add(word);
//    }

    /*
     自定义适配器HabitAdapter
     */
     class HabitAdapter extends ArrayAdapter<Habit> {
        private int resourceId;

        public HabitAdapter(Context context, int textViewResourceId, List<Habit> objects){
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            final Habit habit = getItem(position);
            View view;
            ViewHolder viewHolder = null;
            if(convertView == null){
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.habitImg = (ImageView) view.findViewById(R.id.habitImg);
                viewHolder.habitName = (TextView)view.findViewById(R.id.habitName);
                viewHolder.days= (TextView)view.findViewById(R.id.days);
                viewHolder.checkImg = (ImageView)view.findViewById(R.id.checkImg);
                view.setTag(viewHolder);
            }else{
                view = convertView;
                viewHolder = (ViewHolder)view.getTag();
            }

            //设置习惯的图片
            int flag = 0;
            viewHolder.habitName.setText(habit.getName());
            for(int i=0; i<habit_name.length; ++i){
                if((habit.getName()).equals(habit_name[i])){
                    flag = 1;
                    viewHolder.habitImg.setBackgroundResource(habit_img[i]);
                }
            }
            if(flag == 0){
                viewHolder.habitImg.setBackgroundResource(R.drawable.seed);
            }
            String days = "已坚持" + String.valueOf(habit.getDays()) + "天";
            viewHolder.days.setText(days);
            if(habit.getPunch() == 1){
                viewHolder.checkImg.setBackgroundResource(R.drawable.check);
            }


            //删除当前项
            viewHolder.delete = view.findViewById(R.id.delete_button);
            viewHolder.delete.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    showAlerDialog(position);
                }
            });

            return view;
        }

        private void showAlerDialog(final int position) {
            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setMessage("删除习惯会删除所有的图文记录，确认删除？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            db = dbHelper.getWritableDatabase();
                            habitName = (TextView)listView.getChildAt(position).findViewById(R.id.habitName);
                            db.delete("Habit", "name = ?", new String[] { habitName.getText().toString() });
                            myHabits.remove(position);
                            notifyDataSetChanged();
                            listView.turnNormal();
                        }
                    })
                    .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){}
                    })
                    .create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColorStateList(R.color.alertFont));
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColorStateList(R.color.alertFont));
        }


        class ViewHolder {
            ImageView habitImg;
            TextView habitName;
            TextView days;
            ImageView checkImg;
            View delete;
        }
    }
}
