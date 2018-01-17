package cn.edu.hznu.labaddressclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by asus on 2017/11/23.
 */

public class ShowActivity extends AppCompatActivity {

    private ContactAdapter adapter;
    private Cursor cursor;
    private ListView listView;

    private List<Contact> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent intent = getIntent();
        String selection = intent.getStringExtra("seletion");
        String k = intent.getStringExtra("kw");

        String[] keyWord = {};

        /*实现数据的模糊查询*/
        if(selection.equals("name = ?")){
            Uri uri = Uri.parse("content://cn.edu.hznu.labdataforsqlite.provider/contact");
            //先查询出所有的数据
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    //包含字符串
                    if(name.indexOf(k) != -1){
                        String id = cursor.getString(cursor.getColumnIndex("id"));
                        keyWord = Arrays.copyOf(keyWord, keyWord.length + 1);
                        keyWord[keyWord.length - 1] = id;
                    }
                }
            }
        }
        else if(selection.equals("mobile = ?")){
            Uri uri = Uri.parse("content://cn.edu.hznu.labdataforsqlite.provider/contact");
            //先查询出所有的数据
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
                    //包含字符串
                    if(mobile.indexOf(k) != -1){
                        String id = cursor.getString(cursor.getColumnIndex("id"));
                        keyWord = Arrays.copyOf(keyWord, keyWord.length + 1);
                        keyWord[keyWord.length - 1] = id;
                    }
                }
            }
        }

        Uri uri = Uri.parse("content://cn.edu.hznu.labdataforsqlite.provider/contact/#");
        cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String mobile = cursor.getString(cursor.getColumnIndex("mobile"));

                for(int i=0; i<keyWord.length; ++i){
                    if(String.valueOf(id).equals(keyWord[i])){
                        Contact contact_item = new Contact(name, mobile);
                        contactList.add(contact_item);
                    }
                }

            }
        }

        if(contactList.size() == 0){
            AlertDialog.Builder dialog = new AlertDialog.Builder(ShowActivity.this);
            dialog.setTitle("Warning");
            dialog.setMessage("抱歉，没有找到相关的联系人");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){}
            });
            dialog.show();

        }
        else{
            adapter = new ContactAdapter(ShowActivity.this, R.layout.contact_item, contactList);
            listView = (ListView)findViewById(R.id.list_view);
            listView.setAdapter(adapter);
        }
    }
}
