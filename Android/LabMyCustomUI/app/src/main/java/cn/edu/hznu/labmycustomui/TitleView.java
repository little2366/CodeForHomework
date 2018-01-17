package cn.edu.hznu.labmycustomui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.AttributedCharacterIterator;

/**
 * Created by asus on 2017/10/18.
 */

public class TitleView extends RelativeLayout {
    private Button btn;
    static private TextView title;

    //构造函数
    public TitleView(Context context, AttributeSet attrs){
        super(context, attrs);
        //加载布局
        LayoutInflater.from(context).inflate(R.layout.title_bar, this);
        //获取控件
        btn = (Button)findViewById(R.id.btn);
        title = (TextView)findViewById(R.id.title);

        //为返回按钮添加点击事件
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"点击了返回按钮",Toast.LENGTH_SHORT).show();
                ((Activity)getContext()).finish();
            }
        });
    }

    //设置标题显示的内容
    public void setTitle(String myTitle){
        title.setText(myTitle);
    }

}
