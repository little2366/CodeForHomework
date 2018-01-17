package cn.edu.hznu.labaddressclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Arrays;

public class QueryActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private String str = "按姓名";
    private String selection = "name = ?";
    private String kw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                str = radioButton.getText().toString();   //str用来获取当前radioButton选择的是哪一个
                if(str.equals("按姓名")){
                    selection = "name = ?";
                }
                else {
                    selection = "mobile = ?";
                }
            }
        });

        Button queryData = (Button)findViewById(R.id.queryData);
        queryData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //查询数据

                EditText keyWordEdit = (EditText) findViewById(R.id.keyWord);
                String keyWord = keyWordEdit.getText().toString();
                kw = keyWord;

                if(!TextUtils.isEmpty(keyWord)) {
                    //启动下一个活动,并传递数据
                    Intent intent = new Intent(QueryActivity.this, ShowActivity.class);
                    intent.putExtra("seletion", selection);
                    intent.putExtra("kw", kw) ;
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(QueryActivity.this);
                    dialog.setTitle("Warning");
                    dialog.setMessage("请输入你要查找的关键字！");
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
