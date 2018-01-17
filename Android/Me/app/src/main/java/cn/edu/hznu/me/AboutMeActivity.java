package cn.edu.hznu.me;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class AboutMeActivity extends AppCompatActivity {

    private List<Memory> myMemory = new ArrayList<>();
    private Button click_habit;
    private Button click_market;

    // 持有这个动画的引用，让他可以在动画执行中途取消
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;

    private MyDatabaseHelper dbHelper;
    private Cursor cursor;
    private SQLiteDatabase db;

    private TextView habit_amount;
    private TextView money;

    private MyApplication application;   //全局变量的使用

    private int now_money;
    private ImageView set_alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        click_habit = (Button) findViewById(R.id.click_habit);
        click_habit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutMeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        click_market = (Button) findViewById(R.id.click_market);
        click_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutMeActivity.this, HabitsMarketActivity.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.ctl_toolbar);
        setSupportActionBar(toolbar);
        //将back按钮去掉
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        CollapsingToolbarLayout collapsing_toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsing_toolbar_layout.setTitle("little    ");
        //设置展开的时候标题显示字体颜色
        collapsing_toolbar_layout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsing_toolbar_layout.setExpandedTitleGravity(Gravity.CENTER);
        //设置折叠的时候标题显示字体颜色
        collapsing_toolbar_layout.setCollapsedTitleTextColor(Color.WHITE);
        //设置折叠时候标题对齐位置
        collapsing_toolbar_layout.setCollapsedTitleGravity(Gravity.CENTER);

        dbHelper = new MyDatabaseHelper(this, "Seed.db", null, 1);
        db = dbHelper.getWritableDatabase();
        //查询habit表中所有的数据
        cursor = db.query("Habit", null, null, null, null, null, null);
        int amount = 0;
        if(cursor.moveToFirst()){
            do{
                //遍历Cursor对象，取出数据
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String note = cursor.getString(cursor.getColumnIndex("note"));
                String imgPath = cursor.getString(cursor.getColumnIndex("imgPath"));

                amount++;
                Memory memory_item = new Memory(name, time, note, imgPath);
                myMemory.add(memory_item);

            }while(cursor.moveToNext());
        }
        cursor.close();


        habit_amount = (TextView) findViewById(R.id.habit_amount);
        habit_amount.setText(String.valueOf(amount));

        money = (TextView) findViewById(R.id.money);
        application = (MyApplication)AboutMeActivity.this.getApplicationContext();
        now_money = application.getNumber();
        money.setText(String.valueOf(now_money));

        //initMemory();
        MemoryAdapter adapter = new MemoryAdapter(AboutMeActivity.this, R.layout.memory_item, myMemory);
        ListView listView = (ListView) findViewById(R.id.memory);
        listView.setAdapter(adapter);

    }

    public static void start(Context mContext) {
        mContext.startActivity(new Intent(mContext, AboutMeActivity.class));
    }

    //    private void initMemory() {
//        Memory getUp = new Memory("早起", "2017-12-15", "我要每天早起去吃早饭，要坚持哦", R.drawable.fruit);
//        myMemory.add(getUp);
//        Memory sleep = new Memory("早起", "2017-12-15", "我要每天早起去吃早饭，要坚持哦", R.drawable.english);
//        myMemory.add(sleep);
//        Memory run = new Memory("早起", "2017-12-15", "我要每天早起去吃早饭，要坚持哦", R.drawable.breakfast);
//        myMemory.add(run);
//    }

    class MemoryAdapter extends ArrayAdapter<Memory> {
        private int resourceId;

        public MemoryAdapter(Context context, int textViewResourceId, List<Memory> objects){
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            final Memory memory = getItem(position);
            final View view;
            final ViewHolder viewHolder;
            if(convertView == null){
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.memory_name = (TextView) view.findViewById(R.id.memory_name);
                viewHolder.memory_time = (TextView) view.findViewById(R.id.memory_time);
                viewHolder.memory_note = (TextView) view.findViewById(R.id.memory_note);
                viewHolder.memory_img = (ImageView)view.findViewById(R.id.memory_img);
                view.setTag(viewHolder);
            }else{
                view = convertView;
                viewHolder = (ViewHolder)view.getTag();
            }

            viewHolder.memory_name.setText(memory.getName());
            String time = String.valueOf(memory.getTime());
            viewHolder.memory_time.setText(time);
            String note = String.valueOf(memory.getNote());
            viewHolder.memory_note.setText(note);
            String imagePath = memory.getImg();
            if(imagePath.equals("")){
                viewHolder.memory_img.setMaxWidth(10);
            }
            else{
                File file = new File(imagePath);
                if(file.exists()){
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inSampleSize = 4;

                    //解决加载大图出现返回null的问题
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath, opts);
                    if (bitmap == null) {
                        //如果图片为null, 图片不完整则删除掉图片
                        byte[] bytes = new byte[(int) file.length() + 1];
                        try{
                            FileInputStream inputStream = new FileInputStream(imagePath);
                            inputStream.read(bytes);
                            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            if (bitmap == null) {
                                file.delete();
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                    }
                    viewHolder.memory_img.setImageBitmap(bitmap);
                 }
            }


            viewHolder.memory_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zoomImageFromThumb(viewHolder.memory_img, memory.getImg());
                }
            });

            // 系统默认的短动画执行时间 200
            mShortAnimationDuration = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            return view;
        }


        class ViewHolder {
            TextView memory_name;
            TextView memory_time;
            TextView memory_note;
            ImageView memory_img;
        }
    }


    //Path转Uri
    public static Uri getImageStreamFromExternal(String imageName) {
        File externalPubPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
        );

        File picPath = new File(externalPubPath, imageName);
        Uri uri = null;
        if(picPath.exists()) {
            uri = Uri.fromFile(picPath);
        }

        return uri;
    }

    private void zoomImageFromThumb(final View thumbView, String imagePath) {
        // 如果有动画正在运行，取消这个动画
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // 加载显示大图的ImageView
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);
        //expandedImageView.setImageResource(imageResId);

        File file = new File(imagePath);
        if(file.exists()){
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 4;

            //解决加载大图出现返回null的问题
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, opts);
            if (bitmap == null) {
                //如果图片为null, 图片不完整则删除掉图片
                byte[] bytes = new byte[(int) file.length() + 1];
                try{
                    FileInputStream inputStream = new FileInputStream(imagePath);
                    inputStream.read(bytes);
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    if (bitmap == null) {
                        file.delete();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
            expandedImageView.setImageBitmap(bitmap);
        }


        // 计算初始小图的边界位置和最终大图的边界位置。
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // 小图的边界就是小ImageView的边界，大图的边界因为是铺满全屏的，所以就是整个布局的边界。
        // 然后根据偏移量得到正确的坐标。
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // 计算初始的缩放比例。最终的缩放比例为1。并调整缩放方向，使看着协调。
        float startScale=0;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // 横向缩放
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // 竖向缩放
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // 隐藏小图，并显示大图
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // 将大图的缩放中心点移到左上角。默认是从中心缩放
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        //对大图进行缩放动画
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // 点击大图时，反向缩放大图，然后隐藏大图，显示小图。
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

}
