package com.xuewen.xuewen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class SettingActivity extends AppCompatActivity {

//    @BindView(R.id.listView) ListView listView;

    private static  final String[] textData = {"使用帮助","关于学问", "意见反馈", "版本更新"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ListView listView = (ListView) findViewById(R.id.listView);
        List<Map<String, String>> list_data = new ArrayList<Map<String, String>>();
        for (int i = 0; i < textData.length; i++) {
            Map<String, String> tempMap = new HashMap<String, String>();
            tempMap.put("textview", textData[i]);
            list_data.add(tempMap);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list_data,
                R.layout.activity_setting_item, new String[] {"textview"}, new int[] {R.id.textview});

        listView.setAdapter(simpleAdapter);
    }
}
