package cn.edu.hznu.labbasicui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private EditText txtNumber;
    private Button btnChange;
    private Button btnShow;
    private int value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取布局中的组件
        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        txtNumber = (EditText)findViewById(R.id.txt_number);
        btnChange = (Button)findViewById(R.id.btn_change);
        btnShow = (Button)findViewById(R.id.btn_show);
        btnChange.setOnClickListener(this);  //???
        btnShow.setOnClickListener(this);  //注册事件监听
    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        switch(id){
            //点击了修改按钮
            case R.id.btn_change:
                String strValue=txtNumber.getText().toString();
                if(strValue != null){
                    //转换为int类型的数据
                    value = Integer.parseInt(strValue);
                    if(value>=0 && value<=100){
                        progressBar.setProgress(value);   //修改进度条当前的值
                    }
                    else{
                        //数据不合法，弹出消息框消息
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        //设置弹出框的标题
                        dialog.setTitle("Indication");
                        //设置弹出框的信息
                        dialog.setMessage("输入的数字不合法！");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which){
                            }
                        });
                        dialog.show();
                    }
                }
                txtNumber.setText("");
                break;

            //点击了查看按钮
            case R.id.btn_show:
                //弹出消息框消息显示该值
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                //设置弹出框的标题
                dialog.setTitle("Progress Value");
                //设置弹出框的信息
                int progress = progressBar.getProgress();
                String str = "当前值：" + progress;
                dialog.setMessage(str);
                dialog.setCancelable(false);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which){
                    }
                });
                dialog.show();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
