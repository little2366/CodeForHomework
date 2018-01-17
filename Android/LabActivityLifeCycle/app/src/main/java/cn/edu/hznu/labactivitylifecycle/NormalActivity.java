package cn.edu.hznu.labactivitylifecycle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by asus on 2017/9/28.
 */

public class NormalActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_layout);
    }
}
