package cn.edu.hznu.labdataforsqlite;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private EditText nameEdit;
    private EditText mobileEdit;
    private Button addData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dbHelper = new MyDatabaseHelper(this, "contact.db", null, 1);
        addData = (Button)findViewById(R.id.add);
        addData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //根据用户输入的数据组装数据
                nameEdit = (EditText)findViewById(R.id.name);
                mobileEdit = (EditText)findViewById(R.id.mobile);
                String name = nameEdit.getText().toString();
                String mobile = mobileEdit.getText().toString();
                if((!TextUtils.isEmpty(name))&&(!TextUtils.isEmpty(mobile))){
                    values.put("name",name);
                    values.put("mobile",mobile);
                    db.insert("contact", null, values);
                    //跳转回主页面
                    Intent intent = new Intent(addActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(addActivity.this);
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
