package cn.edu.hznu.me;

/**
 * Created by asus on 2017/12/16.
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;


public class SelectPicPopupWindow extends Activity implements OnClickListener {

    //private LinearLayout pop_choose;
    private TextView take_photo;
    private TextView pick_photo;
    private TextView cancel;
    private String seed_name;

    public Uri imageUri;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_layout);
        Intent intent = getIntent();
        seed_name = intent.getStringExtra("seed_name");

        take_photo = (TextView) this.findViewById(R.id.take_photo);
        pick_photo = (TextView) this.findViewById(R.id.pick_photo);
        cancel = (TextView) this.findViewById(R.id.cancel);

        //pop_choose = (LinearLayout) findViewById(R.id.pop_choose);

        //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        /*pop_choose.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });*/
        //添加按钮监听
        cancel.setOnClickListener(this);
        pick_photo.setOnClickListener(this);
        take_photo.setOnClickListener(this);
    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //finish();
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
                takePhoto();
                break;
            case R.id.pick_photo:
                pickPhoto();
                break;
            case R.id.cancel:
                finish();
            default:
                break;
        }
        //finish();
    }

    public void takePhoto() {
        String imagePath = Environment.getExternalStorageDirectory() + "/image";
        File file = new File(imagePath);
        if(!file.exists()){
            file.mkdirs();
        };
        String imgName = seed_name + ".jpg";
        File outputImage = new File(imagePath, imgName);
        //File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(this, "cn.edu.hznu.me.fileprovider", outputImage);
        }else{
            imageUri = Uri.fromFile(outputImage);
        }

        Intent intent  = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    public void pickPhoto(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else{
            openAlbum();
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    //返回数据到上一个活动SowActivity
                    Intent intent = new Intent();
                    intent.putExtra("img", String.valueOf(imageUri));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    if(Build.VERSION.SDK_INT >= 19){
                        handleImageOnKitKat(data);
                    }
                    else{
                        handleImageBeforeKitKat(data);
                    }
                }
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this, uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri, null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri, String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath){
        if(imagePath != null){
            //返回数据到上一个活动SowActivity
            Intent intent = new Intent();
            intent.putExtra("img", imagePath);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}