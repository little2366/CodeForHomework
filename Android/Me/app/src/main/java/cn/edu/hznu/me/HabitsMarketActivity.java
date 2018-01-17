package cn.edu.hznu.me;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class HabitsMarketActivity extends AppCompatActivity{
    private Button click_habit;
    private Button click_me;
    private ImageView search;
    private LinearLayout for_click;
    private LinearLayout for_search;
    private LinearLayout click_layout;
    private LinearLayout search_layout;
    private TextView not_search;

    private ViewPager mViewPaper;
    private List<ImageView> images;
    private List<View> dots;
    private int currentItem;
    //记录上一次点的位置
    private int oldPosition = 0;
    //存放图片的id
    private int[] imageIds = new int[]{
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3
    };
    //存放图片的标题
    private String[]  titles = new String[]{
            "业精于勤荒于嬉",
            "保剑锋从磨砺出，梅花香自苦寒来",
            "坚持就是胜利"
    };
    private TextView title;
    private ViewPagerAdapter adapter;
    private ScheduledExecutorService scheduledExecutorService;

    private List<Seed> seedList = new ArrayList<>();
    private RecyclerView recyclerView;
    private String[] seed_name = new String[] {"吃早饭", "多喝水", "早睡", "跑步", "画画", "看书", "摄影", "学英语", "早起", "背单词", "吃水果", "锻炼"};

    String[] result = {};
    String[] seedName = {};

    private ListView result_list;
    private EditText search_what;

    private LinearLayout not_exist;
    private TextView create_seed;
    private TextView create_what;

    private MyDatabaseHelper dbHelper;
    private Cursor cursor;
    private SQLiteDatabase db;

    private MyApplication application;   //全局变量的使用

    private LinearLayout passage1;
    private LinearLayout passage2;

    private int money;

    //下拉刷新
//    private SwipeRefreshLayout mSwipeLayout;
//    private boolean isRefresh = false;//是否刷新中

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits_market);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();

        click_habit = (Button) findViewById(R.id.click_habit);
        click_habit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HabitsMarketActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        click_me = (Button) findViewById(R.id.click_me);
        click_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HabitsMarketActivity.this, AboutMeActivity.class);
                startActivity(intent);
            }
        });

        search = (ImageView) findViewById(R.id.search);
        for_click = (LinearLayout)findViewById(R.id.for_click);
        for_search = (LinearLayout)findViewById(R.id.for_search);
        click_layout = (LinearLayout)findViewById(R.id.click_layout);
        search_layout = (LinearLayout)findViewById(R.id.search_layout);

        not_search = (TextView)findViewById(R.id.not_search);
        dbHelper = new MyDatabaseHelper(this, "Seed.db", null, 1);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for_click.setVisibility(View.GONE);
                click_layout.setVisibility(View.GONE);
                for_search.setVisibility(View.VISIBLE);
                search_layout.setVisibility(View.VISIBLE);

                db = dbHelper.getWritableDatabase();
                cursor = db.query("Seeds", null, null, null, null, null, null);
                if(cursor.moveToFirst()){
                    do{
                        //遍历Cursor对象，取出数据
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        seedName = Arrays.copyOf(seedName, seedName.length + 1);
                        seedName[seedName.length - 1] = name;

                    }while(cursor.moveToNext());
                }
                cursor.close();

                search_what = (EditText)findViewById(R.id.search_what);
                search_what.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // 输入的内容变化的监听
                        String[] temp = {};
                        final String search = search_what.getText().toString();
                        for(int i = 0; i<seedName.length; ++i){
                            //包含搜索条件的字符串
                            if(seedName[i].indexOf(search) != -1){
                                Log.d("HabitsMarketActivity", seedName[i]);
                                temp = Arrays.copyOf(temp, temp.length + 1);
                                temp[temp.length - 1] = seedName[i];
                            }
                        }

                        result = temp;

                        not_exist = (LinearLayout)findViewById(R.id.not_exist);
                        create_seed = (TextView)findViewById(R.id.create_seed);
                        create_what = (TextView)findViewById(R.id.create_what);

                        //查找不到相关的种子记录
                        if(result.length == 0){
                            if(not_exist.getVisibility() == View.GONE){
                                not_exist.setVisibility(View.VISIBLE);
                                result_list.setVisibility(View.GONE);
                            }
                            not_exist.setVisibility(View.VISIBLE);
                            result_list.setVisibility(View.GONE);
                            create_what.setText("种子“" + search + "”尚未被研发");
                            create_seed.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    application = (MyApplication)HabitsMarketActivity.this.getApplicationContext();
                                    if(application.getNumber() - 20 <0){
                                        AlertDialog dialog = new AlertDialog.Builder(HabitsMarketActivity.this)
                                                .setMessage("呜呜呜，当前的种子币不够研发种子~")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                }).create();
                                        dialog.show();
                                    }
                                    else {
                                        AlertDialog dialog = new AlertDialog.Builder(HabitsMarketActivity.this)
                                                .setMessage("研发专属的种子需要花费20个种子币，确认研发吗？")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        db = dbHelper.getWritableDatabase();
                                                        //向Seeds表中插入新数据
                                                        ContentValues values = new ContentValues();
                                                        values.put("name", search);
                                                        db.insert("Seeds", null, values);

                                                        //扣除种子币
                                                        application = (MyApplication) HabitsMarketActivity.this.getApplicationContext();
                                                        money = application.getNumber();
                                                        application.setNumber(money - 20);

                                                        Intent intent = new Intent(HabitsMarketActivity.this, SowActivity.class);
                                                        intent.putExtra("seed_name", search);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                })
                                                .create();
                                        dialog.show();
                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColorStateList(R.color.alertFont));
                                        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColorStateList(R.color.alertFont));
                                    }
                                }

                            });
                        }
                        else {
                            if(not_exist.getVisibility() == View.VISIBLE){
                                not_exist.setVisibility(View.GONE);
                                result_list.setVisibility(View.VISIBLE);
                            }
                            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(HabitsMarketActivity.this, android.R.layout.simple_list_item_1, result);
                            adapter1.notifyDataSetChanged();
                            result_list = (ListView)findViewById(R.id.result_list);
                            result_list.setAdapter(adapter1);
                        }

                    }

                    // 输入前的监听
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    // 输入后的监听
                    @Override
                    public void afterTextChanged(Editable s) {}
                });

            }
        });

        result_list = (ListView)findViewById(R.id.result_list);
        //添加列表的点击事件
        result_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                db = dbHelper.getWritableDatabase();
                cursor = db.query("Habit", null, null, null, null, null, null);
                int already = 0;
                if(cursor.moveToFirst()){
                    do{
                        //遍历Cursor对象，取出数据
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        if(result[position].equals(name)) {
                            already = 1;
                            break;
                        }

                    }while(cursor.moveToNext());
                }
                cursor.close();
                if(already == 0){
                    Intent intent = new Intent(HabitsMarketActivity.this, SowActivity.class);
                    intent.putExtra("seed_name", result[position]);
                    startActivity(intent);
                }
                else if(already == 1){
                    AlertDialog dialog = new AlertDialog.Builder(HabitsMarketActivity.this)
                            .setMessage("你已经播种过了，去习惯田里看看吧")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create();
                    dialog.show();
                }
            }
        });

        not_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for_click.setVisibility(View.VISIBLE);
                click_layout.setVisibility(View.VISIBLE);
                for_search.setVisibility(View.GONE);
                search_layout.setVisibility(View.GONE);
            }
        });

        mViewPaper = (ViewPager) findViewById(R.id.vp);

        //显示的图片
        images = new ArrayList<ImageView>();
        for(int i = 0; i < imageIds.length; i++){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageIds[i]);
            images.add(imageView);
        }
        //显示的小点
        dots = new ArrayList<View>();
        dots.add(findViewById(R.id.dot_0));
        dots.add(findViewById(R.id.dot_1));
        dots.add(findViewById(R.id.dot_2));

        title = (TextView) findViewById(R.id.title);
        title.setText(titles[0]);

        adapter = new ViewPagerAdapter();
        mViewPaper.setAdapter(adapter);

        mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageSelected(int position) {
                title.setText(titles[position]);
                dots.get(position).setBackgroundResource(R.drawable.dot_focused);
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);

                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}

            @Override
            public void onPageScrollStateChanged(int arg0) {}
        });

        initSeeds();
        recyclerView = (RecyclerView)findViewById(R.id.seed);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        SeedAdapter adapter = new SeedAdapter(seedList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new SeedAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view , int position){
                //Toast.makeText(HabitsMarketActivity.this, seed_name[position], Toast.LENGTH_SHORT).show();
                db = dbHelper.getWritableDatabase();
                cursor = db.query("Habit", null, null, null, null, null, null);
                int already = 0;
                if(cursor.moveToFirst()){
                    do{
                        //遍历Cursor对象，取出数据
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        if(seed_name[position].equals(name)) {
                            already = 1;
                            break;
                        }

                    }while(cursor.moveToNext());
                }
                cursor.close();
                if(already == 0){
                    Intent intent = new Intent(HabitsMarketActivity.this, SowActivity.class);
                    intent.putExtra("seed_name", seed_name[position]);
                    startActivity(intent);
                }
                else if(already == 1){
                    AlertDialog dialog = new AlertDialog.Builder(HabitsMarketActivity.this)
                            .setMessage("你已经播种过了，去习惯田里看看吧")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create();
                    dialog.show();
                }

            }
        });

        //设置SwipeRefreshLayout
//        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
//        mSwipeLayout.setColorSchemeColors(Color.BLUE,
//                Color.GREEN,
//                Color.YELLOW,
//                Color.RED);
//
//        // 设置手指在屏幕下拉多少距离会触发下拉刷新
//        mSwipeLayout.setDistanceToTriggerSync(250);
//        // 设定下拉圆圈的背景
//        mSwipeLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
//        // 设置圆圈的大小
//        mSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
//        //设置下拉刷新的监听
//        mSwipeLayout.setOnRefreshListener(this);

        passage1 = (LinearLayout)findViewById(R.id.passage1);
        passage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HabitsMarketActivity.this, Passage1Activity.class);
                startActivity(intent);
            }
        });

        passage2 = (LinearLayout)findViewById(R.id.passage2);
        passage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HabitsMarketActivity.this, Passage2Activity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 自定义Adapter
     */
    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            // TODO Auto-generated method stub
//          super.destroyItem(container, position, object);
//          view.removeView(view.getChildAt(position));
//          view.removeViewAt(position);
            view.removeView(images.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            // TODO Auto-generated method stub
            view.addView(images.get(position));
            return images.get(position);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 利用线程池定时执行动画轮播
     */
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
                new ViewPageTask(),
                2,
                2,
                TimeUnit.SECONDS);
    }


    /**
     * 图片轮播任务
     */
    private class ViewPageTask implements Runnable{

        @Override
        public void run() {
            currentItem = (currentItem + 1) % imageIds.length;
            mHandler.sendEmptyMessage(0);
        }
    }

    /**
     * 接收子线程传递过来的数据
     */
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            mViewPaper.setCurrentItem(currentItem);
        };
    };
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }

    private void initSeeds(){
        Seed seed1 = new Seed("吃早饭", R.drawable.breakfast );
        seedList.add(seed1);
        Seed seed2 = new Seed("多喝水", R.drawable.water );
        seedList.add(seed2);
        Seed seed3 = new Seed("早睡",  R.drawable.sleep);
        seedList.add(seed3);
        Seed seed4 = new Seed("跑步",  R.drawable.run);
        seedList.add(seed4);
        Seed seed5 = new Seed("画画", R.drawable.paint);
        seedList.add(seed5);
        Seed seed6 = new Seed("看书",  R.drawable.read);
        seedList.add(seed6);
        Seed seed7 = new Seed("摄影", R.drawable.photo);
        seedList.add(seed7);
        Seed seed8 = new Seed("学英语", R.drawable.english);
        seedList.add(seed8);
        Seed seed9 = new Seed("早起",  R.drawable.sun);
        seedList.add(seed9);
        Seed seed10 = new Seed("背单词", R.drawable.word);
        seedList.add(seed10);
        Seed seed11 = new Seed("吃水果", R.drawable.fruit);
        seedList.add(seed11);
        Seed seed12 = new Seed("锻炼", R.drawable.sport);
        seedList.add(seed12);
    }


    /*
     * 下拉刷新：监听器SwipeRefreshLayout.OnRefreshListener中的方法，当下拉刷新后触发
     */
//    public void onRefresh() {
//        //检查是否处于刷新状态
//        if (!isRefresh) {
//            isRefresh = true;
//            //模拟加载网络数据，这里设置4秒，正好能看到4色进度条
//            new Handler().postDelayed(new Runnable() {
//                public void run() {
//
//                    //显示或隐藏刷新进度条
//                    mSwipeLayout.setRefreshing(false);
//                    isRefresh = false;
//                }
//            }, 4000);
//        }
//    }
}
