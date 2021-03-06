package com.xuewen.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.xuewen.adapter.MineAsksAdapter;
import com.xuewen.bean.UUidBean;
import com.xuewen.xuewen.QuestionDetailActivity;
import com.xuewen.xuewen.R;

import java.util.ArrayList;
import java.util.List;


public class MineSubFragmentTwo extends Fragment {

    public List<UUidBean.Asked> dataList = new ArrayList<>();

    public MineAsksAdapter dataListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mine_sub_two, container, false);
        ListView dataListView = (ListView) rootView.findViewById(R.id.aboutMe_ask_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dataListView.setNestedScrollingEnabled(true);
        }


        dataListAdapter = new MineAsksAdapter(dataList, getActivity());

        dataListView.setAdapter(dataListAdapter);
        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (((UUidBean.Asked) parent.getAdapter().getItem(position)).finished) {
                    Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
                    intent.putExtra("id", ((UUidBean.Asked) parent.getAdapter().getItem(position)).id);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getActivity(), "TA还没有回答这个问题哦", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }
}
