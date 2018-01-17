package cn.edu.hznu.me;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.Manifest;

public class SowActivity extends AppCompatActivity {

    private Button back_to_market;
    private TextView finish_sow;
    private TextView address;
    private TextView position;
    private LocationClient mLocationClient;
    private LinearLayout add_img;
    private ImageView sow_img;
    private Uri imageUri;

    private MyDatabaseHelper dbHelper;
    private String seed_name;
    private Calendar cal;
    private String year;
    private String month;
    private String day;
    private EditText add_note;
    private String note = "";
    private String imgPath = "";
    private String latitude = "";
    private String longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sow);

        //获取到要创建哪个习惯
        Intent intent = getIntent();
        seed_name = intent.getStringExtra("seed_name");
        Toast.makeText(SowActivity.this, seed_name, Toast.LENGTH_SHORT).show();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();

        back_to_market = (Button)findViewById(R.id.back_to_market);
        back_to_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SowActivity.this, HabitsMarketActivity.class);
                startActivity(intent);
            }
        });


        address = (TextView)findViewById(R.id.address);
        position = (TextView)findViewById(R.id.position);

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MylocationListener());
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(SowActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(SowActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(SowActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(SowActivity.this, permissions, 1);
        }else{
            requestLocation();
        }

        add_img = (LinearLayout)findViewById(R.id.add_img);
        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SowActivity.this, SelectPicPopupWindow.class);
                intent.putExtra("seed_name", seed_name);
                startActivityForResult(intent, 1);
            }
        });

        //点击完成创建数据库并存储相应的信息
        finish_sow = (TextView) findViewById(R.id.finish_sow);

        dbHelper = new MyDatabaseHelper(this, "Seed.db", null, 1);
        finish_sow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", seed_name);   //习惯的名称
                values.put("days", 0);    //习惯坚持的天数
//                cal = Calendar.getInstance();
//                cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//                year = String.valueOf(cal.get(Calendar.YEAR));
//                month = String.valueOf(cal.get(Calendar.MONTH) + 1);
//                day = String.valueOf(cal.get(Calendar.DATE));
                long time = System.currentTimeMillis();
                Date date = new Date(time);
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                values.put("time", format.format(date));            //习惯创建的时间
                values.put("punch", 0);             //今天打卡的情况  0表示未打卡，1表示打过卡
                values.put("punch_time", "yyyy年MM月dd日");    //打卡的时间

                add_note = (EditText)findViewById(R.id.add_note);
                note = add_note.getText().toString();
                values.put("note", note);             //记录的情况

                values.put("imgPath", imgPath);      //存储的图片的路径
                values.put("latitude", latitude);    //播种的维度
                values.put("longitude", longitude);  //播种的经度
                //不要忘记最后插入数据
                db.insert("Habit", null, values);


                values.clear();


                Intent intent = new Intent(SowActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){

                    //将拍摄的照片显示出来
                    String img = data.getStringExtra("img");
                    Bitmap bitmap = null;

                    //判断返回的数据是String还是Uri
                    if (Patterns.WEB_URL.matcher(img).matches() || URLUtil.isValidUrl(img)) {
                        imageUri = Uri.parse(img);
                        imgPath = getImagePath(imageUri);
                        //通过Uri设置当前显示的图片
                        //bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //bitmap = BitmapFactory.decodeFile(imgPath);
                    }
                    else {
                        imgPath = img;
                    }

                    //通过文件绝对路径的方式加载图片
                    File file = new File(imgPath);
                    if(file.exists()){
                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        opts.inSampleSize = 4;

                        //解决加载大图出现返回null的问题
                        bitmap = BitmapFactory.decodeFile(imgPath, opts);
                        if (bitmap == null) {
                            //如果图片为null, 图片不完整则删除掉图片
                            byte[] bytes = new byte[(int) file.length() + 1];
                            try{
                                FileInputStream inputStream = new FileInputStream(imgPath);
                                inputStream.read(bytes);
                                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                if (bitmap == null) {
                                    file.delete();
                                }
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                         bitmap = BitmapFactory.decodeFile(imgPath);
                        }
                    }


                    //修改外面的布局的宽度和高度
                    ViewGroup.LayoutParams lp;
                    lp= add_img.getLayoutParams();
                    lp.height = 320;
                    add_img.setLayoutParams(lp);

                    sow_img = (ImageView) findViewById(R.id.sow_img);
                    ViewGroup.LayoutParams para;
                    para = sow_img.getLayoutParams();
                    para.height = 310;
                    sow_img.setLayoutParams(para);

                    sow_img.setImageBitmap(bitmap);

                }
                break;
            default:
                break;
        }
    }

    //将Uri转换为Path
    private String getImagePath(final Uri uri){
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    //设置Bitmap的大小
    public Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        //实时更新当前的位置
        option.setScanSpan(5000);
        //强制使用GPS定位
        //.setLocationMode(LocationClientOption.locationMode.Device_Sensors);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:
                if(grantResults.length > 0){
                    for(int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else{
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
        }
    }

    public class MylocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(final BDLocation location){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder currentAddress = new StringBuilder();
                    currentAddress.append("埋在杭州师范大学附近");
                    //currentAddress.append("埋在").append(location.getCity()).append(location.getDistrict()).append(location.getStreet()).append("附近");
                    address.setText(currentAddress);
                    StringBuilder currentPosition = new StringBuilder();
                    currentPosition.append("维度：").append(location.getLatitude()).append("\n");
                    currentPosition.append("经度：").append(location.getLongitude());
                    position.setText(currentPosition);
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                }
            });
        }

        /*@Override
        public void onConnectHotSpotMessage(String s, int i){
        }*/
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mLocationClient.stop();
    }

}
