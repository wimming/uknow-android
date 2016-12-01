package com.xuewen.xuewen;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.bean.Question;
import com.xuewen.utility.GlobalUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AskActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText editText;
    @BindView((R.id.textView))
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        //注册
        ButterKnife.bind(this);

        ImageView headimgurl = (ImageView) findViewById(R.id.headimgurl);
        ImageLoader.getInstance().displayImage("drawable://" +  R.drawable.avatar,headimgurl, GlobalUtil.getInstance().circleBitmapOptions);

        ListView questionListView = (ListView) findViewById(R.id.question_list_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            questionListView.setNestedScrollingEnabled(true);
        }

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

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(AskActivity.this, s + "", Toast.LENGTH_SHORT).show();
                // 前面xml属性设置最大60  maxLength
                if (s.length() == 60) {
                    Toast.makeText(AskActivity.this, "最多编辑60个", Toast.LENGTH_LONG).show();
                }
                textView.setText(s.length() + "/60");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }
}
