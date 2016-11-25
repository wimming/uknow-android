package com.xuewen.xuewen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.bean.QQidBean;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.QQidResult;
import com.xuewen.utility.GlobalUtil;
import com.xuewen.utility.ToastMsg;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ym on 16-10-22.
 */

public class QuestionDetailActivity extends AppCompatActivity {

    private int id;

    @BindView(R.id.asker_avatarUrl) ImageView asker_avatarUrl;
    @BindView(R.id.asker_username) TextView asker_username;
    @BindView(R.id.askDate) TextView askDate;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.answerer_avatarUrl) ImageView answerer_avatarUrl;
    @BindView(R.id.answerer_username) TextView answerer_username;
    @BindView(R.id.answerer_status) TextView answerer_status;
    @BindView(R.id.answerer_description) TextView answerer_description;
    @BindView(R.id.review) TextView review;

//    private ImageView header_avatar;
//    private ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        avatar = (ImageView) findViewById(R.id.avatar);
//        header_avatar = (ImageView) findViewById(R.id.header_avatar);
//
//        ImageLoader.getInstance().displayImage("drawable://" +  R.drawable.avatar, avatar, GlobalUtil.getInstance().circleBitmapOptions);
//        ImageLoader.getInstance().displayImage("drawable://" +  R.drawable.avatar, header_avatar, GlobalUtil.getInstance().circleBitmapOptions);

        id = getIntent().getIntExtra("id", -1);
        if (id == -1) {
            Toast.makeText(QuestionDetailActivity.this, ToastMsg.APPLICATION_ERROR, Toast.LENGTH_LONG).show();
            finish();
        }

        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<QQidResult> call =
                apiService.requestQQid(id);

        call.enqueue(new Callback<QQidResult>() {
            @Override
            public void onResponse(Call<QQidResult> call, Response<QQidResult> response) {
                if (response.body().status != 200) {
                    Toast.makeText(QuestionDetailActivity.this, response.body().errmsg, Toast.LENGTH_LONG).show();
                    return;
                }
                renderView(response.body().data);
            }

            @Override
            public void onFailure(Call<QQidResult> call, Throwable t) {
                Toast.makeText(QuestionDetailActivity.this, ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    void renderView(QQidBean data) {

        ImageLoader.getInstance().displayImage("drawable://" +  R.drawable.avatar, asker_avatarUrl, GlobalUtil.getInstance().circleBitmapOptions);
        asker_username.setText(data.asker_username);
        askDate.setText(data.askDate);
        description.setText(data.description);
        ImageLoader.getInstance().displayImage("drawable://" +  R.drawable.avatar, answerer_avatarUrl, GlobalUtil.getInstance().circleBitmapOptions);
        answerer_username.setText(data.answerer_username);
        answerer_status.setText(data.answerer_status);
        answerer_description.setText(data.answerer_description);
        review.setText(data.listeningNum+"人听过，"+data.praiseNum+"人觉得好");

    }
}
