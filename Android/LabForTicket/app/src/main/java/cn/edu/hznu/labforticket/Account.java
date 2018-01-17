package cn.edu.hznu.labforticket;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by asus on 2017/12/3.
 */

public class Account extends AppCompatActivity {

    private Button bottom_order_btn;
    private Button bottom_service_btn;
    private Button bottom_query_btn;
    private Button bottom_account_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        //不显示应用程序的标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();

        //主导航四个按钮
        bottom_order_btn = (Button)findViewById(R.id.bottom_order_btn);
        bottom_service_btn = (Button)findViewById(R.id.bottom_service_btn);
        bottom_query_btn = (Button)findViewById(R.id.bottom_query_btn);
        bottom_account_btn = (Button)findViewById(R.id.bottom_account_btn);

        //主导航按钮背景
        bottom_account_btn.setBackgroundColor(Color.parseColor("#585757"));

        //主导航按钮转跳
        bottom_order_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //转跳到 车票预订
                //Intent intent = new Intent(Account.this, OrderTicket.class);
                //startActivity(intent);
            }
        });
        bottom_service_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //转跳到 商旅服务
                //Intent intent = new Intent(Account.this, TravelService.class);
                //startActivity(intent);
            }
        });
        bottom_query_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //转跳到 订单查询
                Intent intent = new Intent(Account.this, OrderQuery.class);
                startActivity(intent);
            }
        });


    }
}
