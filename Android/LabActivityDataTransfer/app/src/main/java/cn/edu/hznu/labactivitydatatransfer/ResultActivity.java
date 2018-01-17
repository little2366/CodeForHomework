package cn.edu.hznu.labactivitydatatransfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by asus on 2017/9/27.
 */

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView name = (TextView) findViewById(R.id.name);
        TextView passwd = (TextView) findViewById(R.id.passwd);
        TextView gender = (TextView) findViewById(R.id.gender);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        name.setText(bundle.getString("name"));
        passwd.setText(bundle.getString("passwd"));
        gender.setText(bundle.getString("gender"));

        Button btn_back = (Button)findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                /*直接销毁当前的活动*/
               /*finish();*/

               /*返回到上一个活动*/
               Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
