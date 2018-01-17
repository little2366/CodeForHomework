package cn.edu.hznu.me;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Passage1Activity extends AppCompatActivity {

    private ImageView passage1_back_market;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passage1);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();

        passage1_back_market = (ImageView)findViewById(R.id.passage1_back_market);
        passage1_back_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Passage1Activity.this, HabitsMarketActivity.class);
                startActivity(intent);
            }
        });
    }
}
