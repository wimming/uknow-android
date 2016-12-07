package com.xuewen.xuewen;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.adapter.QRListAdapter;
import com.xuewen.bean.QRBean;
import com.xuewen.bean.Question;
import com.xuewen.bean.UUidIBean;
import com.xuewen.networkservice.APITestActivity;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.QRResult;
import com.xuewen.networkservice.QResult;
import com.xuewen.networkservice.UUidIResult;
import com.xuewen.utility.GlobalUtil;
import com.xuewen.utility.MyTextWatch;
import com.xuewen.utility.ToastMsg;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AskActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText editText;
    @BindView((R.id.textView))
    TextView textView;
    @BindView(R.id.sendAskedRequest)
    Button sendAskedRequest;

    @BindView(R.id.avatarUrl)
    ImageView avatarUrl;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.followed)
    TextView followed;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.question_list_view)
    ListView questionListView;

            ;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        //注册
        ButterKnife.bind(this);
        apiService = ApiService.retrofit.create(ApiService.class);

        ImageLoader.getInstance().displayImage("drawable://" +  R.drawable.avatar,avatarUrl, GlobalUtil.getInstance().circleBitmapOptions);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            questionListView.setNestedScrollingEnabled(true);
        }

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
//        QuestionListAdapter questionListAdapter = new QuestionListAdapter(questionList, this);
//        questionListView.setAdapter(questionListAdapter);

        editText.addTextChangedListener(new MyTextWatch(this, 60, textView));

        sendAskedRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendSendAskedRequestService(2, editText.getText().toString(),7);
                Toast.makeText(AskActivity.this, "提问成功", Toast.LENGTH_SHORT);
            }
        });

        renderView(2);
    }


    private void sendSendAskedRequestService(int asker_id, String  description, int answerer_id) {

        Call<QResult> call = apiService.requestQ(asker_id, description, answerer_id); //7
        call.enqueue(new Callback<QResult>() {
            @Override
            public void onResponse(Call<QResult> call, Response<QResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AskActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(AskActivity.this, response.body().errmsg, Toast.LENGTH_LONG).show();
                    return;
                }
                //Toast.makeText(AskActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AskActivity.this, AskedSuccessActivity.class);
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<QResult> call, Throwable t) {
                Toast.makeText(AskActivity.this, ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void renderView(int uid) {

        Call<UUidIResult> call = apiService.requestUUidI(uid);
        call.enqueue(new Callback<UUidIResult>() {
            @Override
            public void onResponse(Call<UUidIResult> call, Response<UUidIResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AskActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(AskActivity.this, response.body().errmsg, Toast.LENGTH_LONG).show();
                    return;
                }

                UUidIBean bean = response.body().data;
                username.setText(bean.username);
                status.setText(bean.status);
                description.setText(bean.description);
                if (bean.followed == 0) {
                    followed.setText("+关注");
                    followed.setBackgroundResource(R.drawable.unfollow_button);
                    //viewHolder.followed.setTextColor(context.getResources().getColor(R.color.main_color));
                }

                final QRListAdapter adapter = new QRListAdapter(bean.answers, AskActivity.this);
                questionListView.setAdapter(adapter);
                questionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(AskActivity.this, QuestionDetailActivity.class);
                        intent.putExtra("id", ((QRBean) parent.getAdapter().getItem(position)).id);
                        startActivity(intent);
                    }
                });

                Toast.makeText(AskActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<UUidIResult> call, Throwable t) {
                Toast.makeText(AskActivity.this, ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
