package com.xuewen.xuewen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xuewen.bean.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ym on 16-10-23.
 */

public class RecommendationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_0, container, false);

        ListView questionListView = (ListView)rootView.findViewById(R.id.question_list_view);
        List<Question> questionList = new ArrayList<>();
        Question q;
        q = new Question("师兄好，软件学院的学生毕业后有哪些出路呢？");
        q.ans_description = "张三，超级学霸";
        q.heard = 100;
        q.liked = 10;
        questionList.add(q);
        q = new Question("师兄好，软件学院的学生毕业后有哪些出路呢？");
        q.ans_description = "张三，超级学霸";
        q.heard = 100;
        q.liked = 10;
        questionList.add(q);
        QuestionListAdapter questionListAdapter = new QuestionListAdapter(questionList, getActivity());

        questionListView.setAdapter(questionListAdapter);
        questionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
