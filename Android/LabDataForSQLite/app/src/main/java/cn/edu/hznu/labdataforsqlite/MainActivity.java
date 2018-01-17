package cn.edu.hznu.labdataforsqlite;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private int[] deleteId ={};
    private ContactAdapter adapter;
    private Cursor cursor;
    private SQLiteDatabase db;
    private ListView listView;

    private List<contact> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDatabaseHelper(this, "contact.db", null, 1);
        db = dbHelper.getWritableDatabase();
        //查询contact表中所有的数据
        cursor = db.query("contact", null, null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                //遍历Cursor对象，取出数据
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String mobile = cursor.getString(cursor.getColumnIndex("mobile"));

                contact contact_item = new contact(name, mobile);
                contactList.add(contact_item);

            }while(cursor.moveToNext());
        }
        cursor.close();

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, data);
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);*/

        adapter = new ContactAdapter(MainActivity.this, R.layout.contact_item, contactList);
        listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        //添加列表的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                contact contact = contactList.get(position);
                String mobile = "tel:" + contact.getMobile();
                //点击列表的电话号码，拨打该号码
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(mobile));
                startActivity(intent);
            }
        });
    }

    //在活动中使用Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            //跳转到添加数据界面
            case R.id.add_item:
                Intent intent = new Intent(MainActivity.this, addActivity.class);
                startActivity(intent);
                break;
            case R.id.delete_item:
                //查询contact表中所有的数据
                int[] allId = {};
                cursor = db.query("contact", null, null, null, null, null, null);
                if(cursor.moveToFirst()) {
                    do {
                        //遍历Cursor对象，取出数据
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        allId = Arrays.copyOf(allId, allId.length + 1);
                        allId[allId.length - 1] = id;

                    } while (cursor.moveToNext());
                }
                cursor.close();
                if(allId.length == 0){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("Warning");
                    dialog.setMessage("当前联系人列表为空，请先添加联系人吧！");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){}
                    });
                    dialog.show();
                }else {
                    for(int i=0; i<allId.length; ++i){
                        contact contact = contactList.get(i);
                        boolean isCheck = contact.getIsCheck();
                        if(isCheck){
                            String toast = "You click " + i;
                            //Toast.makeText(MainActivity.this, toast, Toast.LENGTH_LONG).show();
                            deleteId = Arrays.copyOf(deleteId, deleteId.length + 1);
                            deleteId[deleteId.length - 1] = allId[i];
                        }
                    }
                    if(deleteId.length == 0){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("Warning");
                        dialog.setMessage("您还未选中要删除的联系人！");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){}
                        });
                        dialog.show();
                    }else {
                        for (int i = 0; i < deleteId.length; ++i) {
                            String delete = String.valueOf(deleteId[i]);
                            db.delete("contact", "id = ?", new String[]{delete});
                        }

                        contactList.clear();
                        //查询contact表中所有的数据
                        cursor = db.query("contact", null, null, null, null, null, null);
                        if (cursor.moveToFirst()) {
                            do {
                                //遍历Cursor对象，取出数据
                                String name = cursor.getString(cursor.getColumnIndex("name"));
                                String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
                                Log.d("MainActivity", "name is " + name);
                                Log.d("MainActivity", "mobile is " + mobile);
                                contact contact_item = new contact(name, mobile);
                                contactList.add(contact_item);

                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                        listView.setAdapter(null);
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                        /*ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();
                        int count = adapter.getCount();
                        if (count >= 0) {
                            listView.setAdapter(new ContactAdapter(MainActivity.this, R.layout.contact_item, contactList));
                        }*/
                    }
                }
                break;
            default:
        }
        return true;
    }

}
