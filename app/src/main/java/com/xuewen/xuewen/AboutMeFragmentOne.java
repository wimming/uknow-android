package com.xuewen.xuewen;

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

import com.xuewen.adapter.AboutMeQuestionListAnswerAdapter;
import com.xuewen.bean.QRBean;
import com.xuewen.bean.UUidBean;

import java.util.ArrayList;
import java.util.List;


public class AboutMeFragmentOne extends Fragment {

    public List<UUidBean.Answer> dataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_me_fragment_one, container, false);
        ListView questionListView = (ListView) rootView.findViewById(R.id.aboutMe_answer_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            questionListView.setNestedScrollingEnabled(true);
        }


        AboutMeQuestionListAnswerAdapter questionListAdapter = new AboutMeQuestionListAnswerAdapter(dataList, getActivity());

        questionListView.setAdapter(questionListAdapter);
        questionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AnswerQuestionActivity.class);
                intent.putExtra("id", ((UUidBean.Answer) parent.getAdapter().getItem(position)).id);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
