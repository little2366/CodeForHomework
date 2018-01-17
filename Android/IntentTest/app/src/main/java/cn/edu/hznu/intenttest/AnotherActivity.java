package cn.edu.hznu.intenttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AnotherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);
        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");
        TextView tv = (TextView)findViewById(R.id.tv_message);
        tv.setText(msg);
    }
}
