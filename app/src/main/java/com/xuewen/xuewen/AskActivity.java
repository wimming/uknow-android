package com.xuewen.xuewen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.bean.Question;
import com.xuewen.utility.GlobalUtil;

import java.util.ArrayList;
import java.util.List;

public class AskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        ImageView headimgurl = (ImageView) findViewById(R.id.headimgurl);
        ImageLoader.getInstance().displayImage("drawable://" +  R.drawable.avatar,headimgurl, GlobalUtil.getInstance().circleBitmapOptions);

        ListView questionListView = (ListView) findViewById(R.id.question_list_view);
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

        QuestionListAdapter questionListAdapter = new QuestionListAdapter(questionList, this);
        questionListView.setAdapter(questionListAdapter);


    }
}
