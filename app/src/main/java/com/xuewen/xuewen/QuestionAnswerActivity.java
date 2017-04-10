package com.xuewen.xuewen;


import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.SpeechRecognizer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.bean.QQidBean;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.QQidAResult;
import com.xuewen.networkservice.QQidResult;
import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.Global;
import com.xuewen.utility.ListenHelper;
import com.xuewen.utility.MediaHelper;
import com.xuewen.utility.ToastMsg;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionAnswerActivity extends AppCompatActivity {

    enum PRE_LISTEN_STATE {
        IDLE,
        PLAYING,
        PAUSE
    }
    enum RECORD_STATE {
        PRERECORD,
        RECORDING,
        RECORDED
    }

    final static String text_record_max_length = "录音最长2分钟";
    final static String text_start_record = "点击按钮开始";
    final static String text_stop_record = "点击按钮结束";
    final static String text_start_pre_listen = "点击按钮试听";
    final static int MAX_RECORD_LENGTH = 120000;

    private int id;

    @BindView(R.id.asker_avatarUrl)
    ImageView asker_avatarUrl;
    @BindView(R.id.asker_username)
    TextView asker_username;
    @BindView(R.id.askDate)
    TextView askDate;
    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.visibilityController)
    LinearLayout visibilityController;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.speak)
    ImageView speak;
    @BindView(R.id.voice_length_show)
    TextView voice_length_show;
    @BindView(R.id.voice_status_show)
    TextView voice_status_show;
    @BindView(R.id.re_recorded)
    TextView re_recorded;
    @BindView(R.id.send_recorded)
    TextView send_recorded;
    @OnClick(R.id.send_recorded)
    void onClick() {
        postAnswer();
    }

    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");

    //
    private SpeechRecognizer mIat = null;
    private MediaHelper mediaHelper = new MediaHelper();

    private String filePath = null;
    private int actual_record_length = 1000;  // 至少1s
    private String recognizeResult = null;

    private RECORD_STATE recordState = RECORD_STATE.PRERECORD;
    private PRE_LISTEN_STATE preListenState = PRE_LISTEN_STATE.IDLE;

    private int recordCountdown = MAX_RECORD_LENGTH;  // 2分钟

    private Handler countDownHandler = new Handler();
    private Handler playHandler = new Handler();
    //

    private Runnable countdownRunnable = new Runnable() {
        @Override
        public void run() {
            if (recordState == RECORD_STATE.RECORDING && recordCountdown > 0) {
                voice_length_show.setText(time.format(recordCountdown));
                countDownHandler.postDelayed(this, 1000);
                recordCountdown -= 1000;
            }
        }
    };
    private Runnable playRunnable = new Runnable() {
        @Override
        public void run() {
            if (preListenState == PRE_LISTEN_STATE.PLAYING) {
                int currentMillis = mediaHelper.currentMillis();
                int maxMillis = mediaHelper.maxMillis();
                voice_length_show.setText(time.format(currentMillis) + "/" + time.format(maxMillis));
            }
            playHandler.postDelayed(playRunnable, 200);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);
        ButterKnife.bind(this);

        id = getIntent().getIntExtra("id", -1);
        if (id == -1) {
            Toast.makeText(QuestionAnswerActivity.this, ToastMsg.APPLICATION_ERROR, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light);

        // retrieve data
        // 开始刷新 -> 加载 -> 结束刷新
        // 不可见 -> 加载成功 -> 可见
        visibilityController.setVisibility(View.INVISIBLE);
        refresh.setEnabled(false);  // 阻止手动刷新
        requestAndRender();

        // mediaHelper设置为动态生成的  应该对没个新生成的对象监听才有效果
//        mediaHelper.setOnResultListener(new MediaHelper.OnResultListener() {
//            @Override
//            public void onResult() {
//                state = STATE.IDLE;
//                speak.setImageResource(R.drawable.stop);
//            }
//        });

        speak.setOnClickListener(onClickListener);

        re_recorded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStatus0();
            }
        });

        //初始为0
        goToStatus0();

    }

    @Override
    protected void onPause() {
        goToStatus0();
        mediaHelper.release();
        super.onPause();
    }

    private void requestAndRender() {
        refresh.setRefreshing(true);
        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<QQidResult> call = apiService.requestQQid(id, CurrentUser.userId);

        call.enqueue(new Callback<QQidResult>() {
            @Override
            public void onResponse(Call<QQidResult> call, Response<QQidResult> response) {
                refresh.setRefreshing(false);

                if (!response.isSuccessful()) {
                    Toast.makeText(QuestionAnswerActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(QuestionAnswerActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                    return;
                }
                renderView(response.body().data);
                visibilityController.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<QQidResult> call, Throwable t) {
                Toast.makeText(QuestionAnswerActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                refresh.setRefreshing(false);
            }
        });
    }

    private void renderView(QQidBean data) {

        ImageLoader.getInstance().displayImage(Global.getInstance().baseAvatarUrl+data.asker_avatarUrl, asker_avatarUrl, Global.getInstance().circleBitmapOptions);
        asker_username.setText(data.asker_username);
        description.setText(data.description);

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

    private void showButtons(boolean show) {
        if (!show) {
            re_recorded.setVisibility(View.GONE);
            send_recorded.setVisibility(View.GONE);
        } else {
            re_recorded.setVisibility(View.VISIBLE);
            send_recorded.setVisibility(View.VISIBLE);
        }
    }

    private void prepare() {
        if (mIat != null) {
            mIat.cancel();
            mIat.destroy();
            mIat = null;
        }

        mediaHelper.reset();

        filePath = null;
        recognizeResult = null;
        actual_record_length = 1000;  // 至少1s

        recordState = RECORD_STATE.PRERECORD;
        preListenState = PRE_LISTEN_STATE.IDLE;

        recordCountdown = MAX_RECORD_LENGTH;  // 2分钟

        countDownHandler.removeCallbacks(countdownRunnable);
        playHandler.removeCallbacks(playRunnable);
    }

    private void goToStatus0 () {

        prepare();

        recordState = RECORD_STATE.PRERECORD;

        voice_length_show.setText(text_record_max_length);
        voice_status_show.setText(text_start_record);
        speak.setImageResource(R.drawable.microphone);

        showButtons(false);

    }

    private void goToStatus1() {

        recordState = RECORD_STATE.RECORDING;

        countDownHandler.post(countdownRunnable);

        voice_status_show.setText(text_stop_record);
        speak.setImageResource(R.drawable.stop);

        showButtons(false);
    }

    private void goToStatus2 () {

        recordState = RECORD_STATE.RECORDED;

        countDownHandler.removeCallbacks(countdownRunnable);

        voice_length_show.setText(time.format(actual_record_length));
        voice_status_show.setText(text_start_pre_listen);
        speak.setImageResource(R.drawable.play);

        showButtons(true);
    }

    private void postAnswer() {
        if (filePath == null || recognizeResult == null) {
            Toast.makeText(QuestionAnswerActivity.this, ToastMsg.EMPTY_RECORD, Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(filePath);

        final ProgressDialog dialog = ProgressDialog.show(QuestionAnswerActivity.this, "", "语音文件发送中...");

        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileBody = MultipartBody.Part.createFormData("audio", file.getName(), requestBody);

        Call<QQidAResult> call = apiService.requestQQidA(id,
                RequestBody.create(MediaType.parse("multipart/form-data"), CurrentUser.userId+""),
                RequestBody.create(MediaType.parse("multipart/form-data"), (int) Math.ceil(actual_record_length / 1000.0)+""),
                RequestBody.create(MediaType.parse("multipart/form-data"), recognizeResult),
                fileBody);
        call.enqueue(new Callback<QQidAResult>() {
            @Override
            public void onResponse(Call<QQidAResult> call, Response<QQidAResult> response) {
                dialog.dismiss();

                if (!response.isSuccessful()) {
                    Toast.makeText(QuestionAnswerActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(QuestionAnswerActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                goToStatus0();

                MainActivity.getDataKeeper().mineCached = false;

                Intent intent = new Intent(QuestionAnswerActivity.this, QuestionAnswerSuccessActivity.class);
                startActivity(intent);

                finish();
            }
            @Override
            public void onFailure(Call<QQidAResult> call, Throwable t) {
                Toast.makeText(QuestionAnswerActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (recordState == RECORD_STATE.PRERECORD) {

                goToStatus1();

                mIat = ListenHelper.listenWithoutDialogWithCallback(QuestionAnswerActivity.this, new ListenHelper.OnResultListener() {

                    // 成功后进入状态RECORDED
                    @Override
                    public void onResult(String fileId, String result) {

                        goToStatus2();

                        recognizeResult = result;
                        filePath = ListenHelper.getListenerPath(fileId);
                        try {
                            MediaPlayer lengthGetter = new MediaPlayer();
                            lengthGetter.setDataSource(filePath);
                            lengthGetter.prepare();
                            actual_record_length = lengthGetter.getDuration();
                            lengthGetter.release();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // 失败后进入状态PRERECORD
                    @Override
                    public void onError(String errorMsg) {
                        Toast.makeText(QuestionAnswerActivity.this, ToastMsg.RECORD_FAILED, Toast.LENGTH_SHORT).show();

                        goToStatus0();

                        voice_status_show.setText("输入有错误 请重新输入");
                    }
                });

            } else if (recordState == RECORD_STATE.RECORDING) {

                if (mIat == null) {
                    Toast.makeText(QuestionAnswerActivity.this, ToastMsg.APPLICATION_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }

                mIat.stopListening();

                // 提前remove
                countDownHandler.removeCallbacks(countdownRunnable);
                speak.setImageResource(R.drawable.microphone);
                // 讯飞回调函数 OnError有个延迟 所以需要给用户提示一下
                Toast.makeText(QuestionAnswerActivity.this, "分析中", Toast.LENGTH_SHORT).show();
                // 不设置进入STATE2 是否进入由讯飞的回调函数决定

            } else if (recordState == RECORD_STATE.RECORDED) {

                if (filePath == null) {
                    Toast.makeText(QuestionAnswerActivity.this, ToastMsg.APPLICATION_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }

                // 状态机为  idle -> (play <-> pause) -> idle
                if (preListenState == PRE_LISTEN_STATE.IDLE) {

                    preListenState = PRE_LISTEN_STATE.PLAYING;

                    speak.setImageResource(R.drawable.pause);
                    playHandler.post(playRunnable);
                    mediaHelper.playOnce(filePath, new MediaHelper.OnResultListener() {
                        @Override
                        public void onResult() {
                            preListenState = PRE_LISTEN_STATE.IDLE;

                            speak.setImageResource(R.drawable.play);
                            voice_length_show.setText("00:00" + "/" + time.format(mediaHelper.maxMillis()));
                            playHandler.removeCallbacks(playRunnable);
                        }
                    });
                }
                else if (preListenState == PRE_LISTEN_STATE.PLAYING) {

                    preListenState = PRE_LISTEN_STATE.PAUSE;

                    mediaHelper.pause();
                    speak.setImageResource(R.drawable.play);
                }
                else if (preListenState == PRE_LISTEN_STATE.PAUSE) {

                    preListenState = PRE_LISTEN_STATE.PLAYING;

                    mediaHelper.start();
                    speak.setImageResource(R.drawable.pause);
                }

            }

        }
    };

}
