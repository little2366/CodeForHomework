package cn.edu.hznu.labforticket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button order_query;
    private Button confirm_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        order_query = (Button) findViewById(R.id.order_query);
        order_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderQuery.class);
                startActivity(intent);
            }
        });

        confirm_order = (Button) findViewById(R.id.confirm_order);
        confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ToPayActivity.class);
                startActivity(intent);
            }
        });
    }
}
