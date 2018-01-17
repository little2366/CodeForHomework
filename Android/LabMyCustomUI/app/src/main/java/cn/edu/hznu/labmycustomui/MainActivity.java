package cn.edu.hznu.labmycustomui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TitleView tv = (TitleView)findViewById(R.id.title_bar);
        tv.setTitle("My Page");
    }
}
