package cn.edu.hznu.firstlab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by asus on 2017/9/14.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);           //加载布局
        //获取按钮
        Button button1 = (Button)findViewById(R.id.button_view_message);
        //为按钮注册事件监听
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //输出用户提示信息
                Toast.makeText(MainActivity.this,"You click Button",Toast.LENGTH_SHORT).show();
                //启动MessageActivity
                Intent intent = new Intent(MainActivity.this,MessageActivity.class);
                startActivity(intent);
            }
        });
    }
}
