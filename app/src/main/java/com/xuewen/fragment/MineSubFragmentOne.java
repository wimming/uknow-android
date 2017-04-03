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

import com.xuewen.adapter.MineAnswersAdapter;
import com.xuewen.bean.UUidBean;
import com.xuewen.xuewen.QuestionAnswerActivity;
import com.xuewen.xuewen.QuestionDetailActivity;
import com.xuewen.xuewen.R;

import java.util.ArrayList;
import java.util.List;


public class MineSubFragmentOne extends Fragment {

    public List<UUidBean.Answer> dataList = new ArrayList<>();

    public MineAnswersAdapter dataListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_me_fragment_one, container, false);
        ListView dataListView = (ListView) rootView.findViewById(R.id.aboutMe_answer_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dataListView.setNestedScrollingEnabled(true);
        }


        dataListAdapter = new MineAnswersAdapter(dataList, getActivity());

        dataListView.setAdapter(dataListAdapter);
        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!((UUidBean.Answer) parent.getAdapter().getItem(position)).finished) {
                    Intent intent = new Intent(getActivity(), QuestionAnswerActivity.class);
                    intent.putExtra("id", ((UUidBean.Answer) parent.getAdapter().getItem(position)).id);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
                    intent.putExtra("id", ((UUidBean.Answer) parent.getAdapter().getItem(position)).id);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }
}
