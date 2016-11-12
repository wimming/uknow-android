package com.xuewen.xuewen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by ym on 16-10-28.
 */

public class SearchActivity extends AppCompatActivity {

    private GridView gridView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //多行多列网格 九宫格布局 gridview
        setContentView(R.layout.activity_search);
        gridView = (GridView) findViewById(R.id.gridView);

        String[] buttonsText = {"软件工程2fdsafasdfasdf", "中山大学", "互联网", "安卓开发", "腾讯", "奖学金" };
        //simpleAdapter数据源
        List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < buttonsText.length; i++) {
            Map tempMap = new HashMap<String, Object>();
            tempMap.put("button", buttonsText[i]);
            datalist.add(tempMap);
        }

        String[] from = {"button"};
        int[] to = {R.id.button};

        SimpleAdapter adapter = new SimpleAdapter(this, datalist, R.layout.search_recommend_gridview_item, from, to);
        gridView.setAdapter(adapter);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.search_recommend_gridview_item,R.id.button, data);
//        ArrayAdapter adapter = new ArrayAdapter() {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view;
//                if (convertView == null) {
//                    view = LayoutInflater.from(getContext()).inflate(R.layout.search_recommend_gridview_item, null);
//                } else {
//                    view = convertView;
//                }
//                Button button = (Button) view.findViewById(R.id.button);
//                button.setText(data[position]);
//                return view;
//            }
//        };
        gridView.setAdapter(adapter);
    }
}
