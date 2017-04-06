package com.xuewen.xuewen;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.bean.QQidBean;
import com.xuewen.customview.AudioPlayerView;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.QQidCResult;
import com.xuewen.networkservice.QQidResult;
import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.Global;
import com.xuewen.utility.ToastMsg;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ym on 16-10-22.
 */

public class QuestionDetailActivity extends AppCompatActivity {

    @BindView(R.id.asker_avatarUrl) ImageView asker_avatarUrl;
    @BindView(R.id.asker_username) TextView asker_username;
    @BindView(R.id.askDate) TextView askDate;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.answerer_avatarUrl) ImageView answerer_avatarUrl;
    @BindView(R.id.answerer_username) TextView answerer_username;
    @BindView(R.id.answerer_status) TextView answerer_status;
    @BindView(R.id.answerer_description) TextView answerer_description;
    @BindView(R.id.review) TextView review;

    @BindView(R.id.audioPlayerView) AudioPlayerView audioPlayerView;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.refresh) SwipeRefreshLayout refresh;
    @BindView(R.id.visibilityController) LinearLayout visibilityController;
    @BindView(R.id.commentLayout) LinearLayout commentLayout;
    @BindView(R.id.good) ImageView good;
    @BindView(R.id.bad) ImageView bad;


    private int id;
    private QQidBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light);

        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentRequest(1);
            }
        });
        bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentRequest(0);
            }
        });

        // retrieve data
        id = getIntent().getIntExtra("id", -1);
        if (id == -1) {
            Toast.makeText(QuestionDetailActivity.this, ToastMsg.APPLICATION_ERROR, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // retrieve data
        // 开始刷新 -> 加载 -> 结束刷新
        // 不可见 -> 加载成功 -> 可见
        visibilityController.setVisibility(View.INVISIBLE);
        refresh.setEnabled(false);  // 阻止手动刷新
        requestAndRender(id);

    }

    @Override
    protected void onPause() {
        audioPlayerView.release();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void requestAndRender(int id) {
        refresh.setRefreshing(true);
        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<QQidResult> call = apiService.requestQQid(id, CurrentUser.userId);

        call.enqueue(new Callback<QQidResult>() {
            @Override
            public void onResponse(Call<QQidResult> call, Response<QQidResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(QuestionDetailActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    refresh.setRefreshing(false);
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(QuestionDetailActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                    refresh.setRefreshing(false);
                    return;
                }
                renderView(response.body().data);
                data = response.body().data;

                visibilityController.setVisibility(View.VISIBLE);
                refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<QQidResult> call, Throwable t) {
                Toast.makeText(QuestionDetailActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                refresh.setRefreshing(false);
            }
        });

    }

    private void renderView(final QQidBean data) {
        ImageLoader.getInstance().displayImage(Global.getInstance().baseAvatarUrl+data.asker_avatarUrl, asker_avatarUrl, Global.getInstance().circleBitmapOptions);
        asker_username.setText(data.asker_username);
        description.setText(data.description);
        ImageLoader.getInstance().displayImage(Global.getInstance().baseAvatarUrl+data.answerer_avatarUrl, answerer_avatarUrl, Global.getInstance().circleBitmapOptions);
        answerer_username.setText(data.answerer_username);
        answerer_status.setText(data.answerer_status);
        answerer_description.setText(data.answerer_description);
        review.setText(data.listeningNum+"人听过，"+data.praiseNum+"人觉得好");

        audioPlayerView.prepare(Global.getInstance().baseAudioUrl + data.audioUrl, data.audioSeconds);
        audioPlayerView.setOnCompleteListener(new AudioPlayerView.OnCompleteListener() {
            @Override
            public void onComplete() {
                if (data.commented == 0) {
                    commentLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
            Date date = format.parse(data.askDate);
            PrettyTime prettyTime = new PrettyTime(new Locale("ZH_CN"));
            askDate.setText(prettyTime.format(date));
        } catch (ParseException e) {
            askDate.setText("未知时间");
            e.printStackTrace();
        }
    }

    private void commentRequest(final int praise) {

        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<QQidCResult> call = apiService.requestQQidC(data.id, praise, CurrentUser.userId);

        call.enqueue(new Callback<QQidCResult>() {
            @Override
            public void onResponse(Call<QQidCResult> call, Response<QQidCResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(QuestionDetailActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(QuestionDetailActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(QuestionDetailActivity.this, ToastMsg.COMMENT_SUCCESS, Toast.LENGTH_SHORT).show();
                commentLayout.setVisibility(View.INVISIBLE);

                if (praise == 1) {
                    review.setText(data.listeningNum+1+"人听过，"+data.praiseNum+1+"人觉得好");
                }
                else {
                    review.setText(data.listeningNum+1+"人听过，"+data.praiseNum+"人觉得好");
                }

                MainActivity.getDataKeeper().questionsCached = false;
            }

            @Override
            public void onFailure(Call<QQidCResult> call, Throwable t) {
                Toast.makeText(QuestionDetailActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
