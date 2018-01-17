package cn.edu.hznu.intenttest;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*为第一个按钮添加事件*/
        Button button1 = (Button) findViewById(R.id.btn1);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, AnotherActivity.class);
                //传递字符串数据
                intent.putExtra("msg","这是利用显式 Intent 启动的活动。");
                startActivity(intent);
            }
        });

        /*为第二个按钮添加事件*/
        Button button2 = (Button) findViewById(R.id.btn2);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent("cn.edu.hznu.intenttest.CUSTOMIZED_ACTION");
                intent.addCategory("cn.edu.hznu.intenttest.NEW_CATEGORY");
                //传递字符串数据
                intent.putExtra("msg","这是利用隐式 Intent 启动的活动。");
                startActivity(intent);
            }
        });

        /*为第三个按钮添加事件*/
        Button button3 = (Button) findViewById(R.id.btn3);
        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:10086"));
                startActivity(intent);
            }
        });

    }
}
