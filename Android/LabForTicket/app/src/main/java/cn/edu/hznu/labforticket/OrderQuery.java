package cn.edu.hznu.labforticket;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by asus on 2017/12/3.
 */

public class OrderQuery  extends AppCompatActivity {
    private Button finish;
    private Button notFinish;
    private LinearLayout finishView;
    private LinearLayout notFinishView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        //不显示应用程序的标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();

        finish = (Button)findViewById(R.id.finish);
        notFinish = (Button) findViewById(R.id.notFinish);
        finishView = (LinearLayout)findViewById(R.id.finishView);
        notFinishView = (LinearLayout) findViewById(R.id.notFinishView);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notFinish.setBackgroundResource(R.color.narBar);
                finish.setBackgroundResource(R.drawable.buttonstyle);
                finishView.setVisibility(View.VISIBLE);
                notFinishView.setVisibility(View.GONE);
            }
        });

        notFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish.setBackgroundResource(R.color.narBar);
                notFinish.setBackgroundResource(R.drawable.buttonstyle);
                notFinishView.setVisibility(View.VISIBLE);
                finishView.setVisibility(View.GONE);
            }
        });


    }
}
