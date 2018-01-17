package cn.edu.hznu.labaddressclient;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Button addData = (Button)findViewById(R.id.addData);
        addData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //添加数据

                EditText nameEdit = (EditText)findViewById(R.id.name);
                EditText mobileEdit = (EditText)findViewById(R.id.mobile);
                String name = nameEdit.getText().toString();
                String mobile = mobileEdit.getText().toString();

                if((!TextUtils.isEmpty(name))&&(!TextUtils.isEmpty(mobile))){
                    Uri uri = Uri.parse("content://cn.edu.hznu.labdataforsqlite.provider/contact");
                    ContentValues values = new ContentValues();
                    values.put("name", name);
                    values.put("mobile", mobile);
                    Uri newUri = getContentResolver().insert(uri, values);
                    String newId = newUri.getPathSegments().get(1);
                    //跳转回主页面
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(AddActivity.this);
                    dialog.setTitle("Warning");
                    dialog.setMessage("姓名或手机号码不能为空");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){}
                    });
                    dialog.show();
                }
            }
        });
    }
}
