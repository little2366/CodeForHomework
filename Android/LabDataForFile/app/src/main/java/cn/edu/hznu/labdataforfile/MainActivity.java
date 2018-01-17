package cn.edu.hznu.labdataforfile;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class MainActivity extends AppCompatActivity {

    private EditText fileEdit;
    private EditText contentEdit;
    private Button save_data;
    private Button load_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileEdit = (EditText) findViewById(R.id.file_name);
        contentEdit = (EditText)findViewById(R.id.content);
        save_data = (Button)findViewById(R.id.save_data);
        load_data = (Button)findViewById(R.id.load_data);

        save_data.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String file = fileEdit.getText().toString();
                if(!file.trim().equals("")){
                    String context = contentEdit.getText().toString();
                    save(file, context);
                    contentEdit.setText("");
                    Toast.makeText(MainActivity.this, "Data have been saved.", Toast.LENGTH_LONG).show();
                } else{
                    showAlert();
                }

            }
        });

        load_data.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String file = fileEdit.getText().toString();
                if(!file.trim().equals("")){
                    String content = load(file);
                    if(!TextUtils.isEmpty(content)){
                        contentEdit.setText(content);
                        contentEdit.setSelection(content.length());
                        Toast.makeText(MainActivity.this, "Data have been loaded.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    showAlert();
                }

            }
        });
    }


    public void save(String file_name, String content){
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try{
            out = openFileOutput(file_name, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(content);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public String load(String file_name){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try{
            in = openFileInput(file_name);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line = reader.readLine()) != null){
                content.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            if(reader != null){
                try{
                    reader.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    public void showAlert(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Warning");
        dialog.setMessage("文件名不能为空");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){}
        });
        dialog.show();
    }
}
