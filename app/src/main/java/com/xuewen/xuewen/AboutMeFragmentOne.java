package com.xuewen.xuewen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xuewen.bean.Question;
import com.xuewen.networkservice.APITestActivity;

import java.util.ArrayList;
import java.util.List;


public class AboutMeFragmentOne extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_me_fragment_one, container, false);
        ListView questionListView = (ListView) rootView.findViewById(R.id.aboutMe_answer_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            questionListView.setNestedScrollingEnabled(true);
        }


        List<Question> questionList = new ArrayList<>();

        Question q;
        for (int i = 0; i < 10; ++i) {
            q = new Question("张三的回答");
            q.ans_description = "师兄好，软件学院的学生毕业后有哪些出路呢？";
            questionList.add(q);
        }

        AboutMeQuestionListAnswerAdapter questionListAdapter = new AboutMeQuestionListAnswerAdapter(questionList, getActivity());

        questionListView.setAdapter(questionListAdapter);
        questionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), APITestActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
