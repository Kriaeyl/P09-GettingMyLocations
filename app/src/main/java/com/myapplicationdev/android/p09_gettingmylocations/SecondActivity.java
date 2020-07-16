package com.myapplicationdev.android.p09_gettingmylocations;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    Button b1;
    TextView tv1;
    ArrayList<String> lines;
    ArrayAdapter aa;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        tv1 = findViewById(R.id.textView2);
        b1 = findViewById(R.id.button4);
        lv = findViewById(R.id.lv);
        lines = new ArrayList<>();
        aa = new ArrayAdapter(SecondActivity.this, android.R.layout.simple_list_item_1, lines);
        lv.setAdapter(aa);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lines.clear();
                String folderLocation= Environment.getExternalStorageDirectory().getAbsolutePath() + "/P09Folder";
                File targetFile = new File(folderLocation, "data.txt");
                if (targetFile.exists()){
                    String data ="";
                    try {
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br= new BufferedReader(reader);
                        String line = br.readLine();
                        while (line != null) {
                            lines.add(line);
                            line = br.readLine();
                        }
                        br.close();
                        reader.close();
                    } catch (Exception e) {
                        Toast.makeText(SecondActivity.this, "Failed to read!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    tv1.setText("Number of Records: " + lines.size());
                    aa.notifyDataSetChanged();
                }
            }
        });
        b1.performClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        b1.performClick();
    }
}