package com.xuewen.xuewen;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.bean.QQidBean;
import com.xuewen.customview.AudioPlayerView;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.FileService;
import com.xuewen.networkservice.QQidCResult;
import com.xuewen.networkservice.QQidResult;
import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.FileWriter;
import com.xuewen.utility.GlobalUtil;
import com.xuewen.utility.ToastMsg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by ym on 16-10-22.
 */

public class QuestionDetailActivity extends AppCompatActivity {

    private int id;
    private QQidBean data;

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String downloadedFilePath;

    @BindView(R.id.asker_avatarUrl) ImageView asker_avatarUrl;
    @BindView(R.id.asker_username) TextView asker_username;
    @BindView(R.id.askDate) TextView askDate;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.answerer_avatarUrl) ImageView answerer_avatarUrl;
    @BindView(R.id.answerer_username) TextView answerer_username;
    @BindView(R.id.answerer_status) TextView answerer_status;
    @BindView(R.id.answerer_description) TextView answerer_description;
    @BindView(R.id.review) TextView review;
    @BindView(R.id.listen) Button listen;
    @BindView(R.id.audioPlayerView) AudioPlayerView audioPlayerView;

    @BindView(R.id.refresh) SwipeRefreshLayout refresh;
    @BindView(R.id.visibilityController) LinearLayout visibilityController;
    @BindView(R.id.commentLayout) LinearLayout commentLayout;
    @BindView(R.id.good) ImageView good;
    @BindView(R.id.bad) ImageView bad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

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

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (data.commented == 0) {
                    commentLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        id = getIntent().getIntExtra("id", -1);
        if (id == -1) {
            Toast.makeText(QuestionDetailActivity.this, ToastMsg.APPLICATION_ERROR, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // 不可见、开始刷新 -> 加载成功 -> 可见、结束刷新
        visibilityController.setVisibility(View.GONE);
        refresh.setRefreshing(true);

        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<QQidResult> call = apiService.requestQQid(id, CurrentUser.userId);

        call.enqueue(new Callback<QQidResult>() {
            @Override
            public void onResponse(Call<QQidResult> call, Response<QQidResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(QuestionDetailActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(QuestionDetailActivity.this, response.body().errmsg, Toast.LENGTH_LONG).show();
                    return;
                }
                renderView(response.body().data);
                data = response.body().data;

                listen.setOnClickListener(listenClickListener);
                audioPlayerView.prepare(GlobalUtil.getInstance().baseAudioUrl + data.audioUrl, data.audioSeconds);

                visibilityController.setVisibility(View.VISIBLE);
                refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<QQidResult> call, Throwable t) {
                Toast.makeText(QuestionDetailActivity.this, ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        audioPlayerView.release();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.release();
        super.onDestroy();
    }

    private void renderView(QQidBean data) {

        ImageLoader.getInstance().displayImage(GlobalUtil.getInstance().baseAvatarUrl+data.asker_avatarUrl, asker_avatarUrl, GlobalUtil.getInstance().circleBitmapOptions);
        asker_username.setText(data.asker_username);
        askDate.setText(data.askDate);
        description.setText(data.description);
        ImageLoader.getInstance().displayImage(GlobalUtil.getInstance().baseAvatarUrl+data.answerer_avatarUrl, answerer_avatarUrl, GlobalUtil.getInstance().circleBitmapOptions);
        answerer_username.setText(data.answerer_username);
        answerer_status.setText(data.answerer_status);
        answerer_description.setText(data.answerer_description);
        review.setText(data.listeningNum+"人听过，"+data.praiseNum+"人觉得好");
        listen.setText(data.audioSeconds+"''");

    }

    private View.OnClickListener listenClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String url = GlobalUtil.getInstance().baseAudioUrl + data.audioUrl; // your URL here
//            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare(); // might take long! (for buffering, etc)
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            if (downloadedFilePath == null || downloadedFilePath.equals("")) {
//
//                // 显示progress -> 下载完成 -> 收起progress
//                final ProgressDialog dialog = ProgressDialog.show(QuestionDetailActivity.this, "", "语音文件下载中...");
//
//                final String filePath = GlobalUtil.getInstance().baseAudioUrl + data.audioUrl;
//
//                FileService fileService = FileService.retrofit.create(FileService.class);
//                Call<ResponseBody> fileCall = fileService.downloadFile(filePath);
//
//                fileCall.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        dialog.dismiss();
//                        if (!response.isSuccessful()) {
//                            Toast.makeText(QuestionDetailActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_LONG).show();
//                            return;
//                        }
//
//                        boolean writtenToDisk = FileWriter.getInstance().writeResponseBodyToDisk(response.body(), getExternalFilesDir(null) + "", data.id + ".wav");
//                        if (!writtenToDisk) {
//                            Toast.makeText(QuestionDetailActivity.this, ToastMsg.FILE_OPERATION_ERROR, Toast.LENGTH_LONG).show();
//                            return;
//                        }
//
//                        downloadedFilePath = filePath;
//
//                        if (!mediaPlayer.isPlaying()) {
//                            try {
//                                mediaPlayer.setDataSource(downloadedFilePath);
//                                mediaPlayer.prepare();
//                                mediaPlayer.start();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                                Toast.makeText(QuestionDetailActivity.this, ToastMsg.FILE_OPERATION_ERROR, Toast.LENGTH_LONG).show();
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Toast.makeText(QuestionDetailActivity.this, ToastMsg.NETWORK_ERROR + " : " + t.getMessage(), Toast.LENGTH_LONG).show();
//                        dialog.dismiss();
//                    }
//                });
//
//            }
//            else {
//                if (!mediaPlayer.isPlaying()) {
//                    mediaPlayer.start();
//                }
//            }
        }
    };

    private void commentRequest(int praise) {

        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<QQidCResult> call = apiService.requestQQidC(data.id, praise, CurrentUser.userId);

        call.enqueue(new Callback<QQidCResult>() {
            @Override
            public void onResponse(Call<QQidCResult> call, Response<QQidCResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(QuestionDetailActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(QuestionDetailActivity.this, response.body().errmsg, Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(QuestionDetailActivity.this, "评价成功", Toast.LENGTH_LONG).show();
                commentLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<QQidCResult> call, Throwable t) {
                Toast.makeText(QuestionDetailActivity.this, ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
