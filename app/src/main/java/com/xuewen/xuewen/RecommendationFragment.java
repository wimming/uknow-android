package com.xuewen.xuewen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

        ListView questionListView = (ListView)rootView.findViewById(R.id.question_list_view);
        List<Question> questionList = new ArrayList<>();

        Question q;
        for (int i = 0; i < 10; ++i) {
            q = new Question("师兄好，软件学院的学生毕业后有哪些出路呢？");
            q.ans_description = "张三 | 清华大学计算机系，ACM校队队长，喜欢钻研算法，喜欢钻研算法";
            q.heard = 100;
            q.liked = 10;
            q.ans_headimgurl = "http://www.jd.com/favicon.ico";
            questionList.add(q);
        }

        QuestionListAdapter questionListAdapter = new QuestionListAdapter(questionList, getActivity());

        questionListView.setAdapter(questionListAdapter);
        questionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
//                startActivity(intent);

                ApiService apiService = ApiService.retrofit.create(ApiService.class);
                final Call<QRResult> call =
                        apiService.requestQR();

                call.enqueue(new Callback<QRResult>() {
                    @Override
                    public void onResponse(Call<QRResult> call, Response<QRResult> response) {
                        Log.e("MyRequest when success ", response.body().data.get(1).listeningNum+"");
                        Toast.makeText(getActivity(), "MyRequest when success "+response.body().data.get(1).listeningNum+"", Toast.LENGTH_LONG).show();
//
//                        String str = "{\n" +
//                                "  'status': 200,\n" +
//                                "  'result': [ \n" +
//                                "    {\n" +
//                                "      'que__id': 1,\n" +
//                                "      'que__description': '问题描述',\n" +
//                                "      'ans_id': 11,\n" +
//                                "      'ans_status': '回答者身份',\n" +
//                                "      'ans_description': '回答者描述',\n" +
//                                "      'ans_headimgurl': '回答者头像路径',\n" +
//                                "      'heard': 12,\n" +
//                                "      'liked': 14\n" +
//                                "    },\n" +
//                                "    {\n" +
//                                "      'que__id': 1,\n" +
//                                "      'que__description': '问题描述',\n" +
//                                "      'ans_id': 11,\n" +
//                                "      'ans_status': '回答者身份',\n" +
//                                "      'ans_description': '回答者描述',\n" +
//                                "      'ans_headimgurl': '回答者头像路径',\n" +
//                                "      'heard': 12,\n" +
//                                "      'liked': 14\n" +
//                                "    }\n" +
//                                "  ]\n" +
//                                "}";
//                        Gson gson = new Gson();
//                        NetworkResult networkResult = gson.fromJson(str, NetworkResult.class);
                    }
                    @Override
                    public void onFailure(Call<QRResult> call, Throwable t) {
                        Log.e("MyRequest when failure", "Something went wrong: " + t.getMessage());
                        Toast.makeText(getActivity(), "MyRequest when success "+t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return rootView;
    }
}
