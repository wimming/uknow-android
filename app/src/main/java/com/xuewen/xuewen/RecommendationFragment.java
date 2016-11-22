package com.xuewen.xuewen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.xuewen.adapter.QRListAdapter;
import com.xuewen.bean.QRBean;
import com.xuewen.bean.Question;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.QQidResult;
import com.xuewen.networkservice.QRResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ym on 16-10-23.
 */

public class RecommendationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_0, container, false);

//        ListView questionListView = (ListView)rootView.findViewById(R.id.question_list_view);
//        List<Question> questionList = new ArrayList<>();
//
//        Question q;
//        for (int i = 0; i < 10; ++i) {
//            q = new Question("师兄好，软件学院的学生毕业后有哪些出路呢？");
//            q.ans_description = "张三 | 清华大学计算机系，ACM校队队长，喜欢钻研算法，喜欢钻研算法";
//            q.heard = 100;
//            q.liked = 10;
//            q.ans_headimgurl = "http://www.jd.com/favicon.ico";
//            questionList.add(q);
//        }
//
//        QuestionListAdapter questionListAdapter = new QuestionListAdapter(questionList, getActivity());
//
//        questionListView.setAdapter(questionListAdapter);
//        questionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
////                startActivity(intent);
//
//            }
//        });

        final ListView listView = (ListView)rootView.findViewById(R.id.listView);
        final List<QRBean> list = new ArrayList<>();
        final QRListAdapter adapter = new QRListAdapter(list, getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
                startActivity(intent);

            }
        });

        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<QRResult> call =
                apiService.requestQR();

        call.enqueue(new Callback<QRResult>() {
            @Override
            public void onResponse(Call<QRResult> call, Response<QRResult> response) {
                Toast.makeText(getActivity(), "request success, list size : "+response.body().data.size()+"", Toast.LENGTH_LONG).show();
                list.clear();
                for (QRBean item : response.body().data) {
                    list.add(item);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<QRResult> call, Throwable t) {
                Toast.makeText(getActivity(), "request failure : "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }
}
