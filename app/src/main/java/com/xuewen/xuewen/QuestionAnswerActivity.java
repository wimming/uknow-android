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
import com.xuewen.utility.GlobalUtil;
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

    final static String voice_length_show_text0 = "录音最长2分钟";
    final static String voice_status_show_text0 = "点击按钮开始";
    final static String voice_status_show_text1 = "点击按钮结束";
    final static String voice_status_show_text2 = "点击按钮试听";
    final static int RECORD_LENGTH = 120000;

    private int ACTUAL_RECORD_LENGTH = 2; //至少1s

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
        if (filePath == null) {
            Toast.makeText(QuestionAnswerActivity.this, ToastMsg.EMPTY_RECORD, Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog dialog = ProgressDialog.show(QuestionAnswerActivity.this, "", "语音文件发送中...");

        ApiService apiService = ApiService.retrofit.create(ApiService.class);

        File file = new File(filePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileBody = MultipartBody.Part.createFormData("audio", file.getName(), requestBody);

        Call<QQidAResult> call = apiService.requestQQidA(id,
                RequestBody.create(MediaType.parse("multipart/form-data"), CurrentUser.userId+""),
                RequestBody.create(MediaType.parse("multipart/form-data"), ACTUAL_RECORD_LENGTH + ""),
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

                mIat.cancel();
                mIat.destroy();
                handler_countdown.removeCallbacks(r);
                playHandler.removeCallbacks(playRunnable);
                Intent intent = new Intent(QuestionAnswerActivity.this, QuestionAnswerSuccessActivity.class);
//                Intent intent = new Intent(QuestionAnswerActivity.this, QuestionDetailActivity.class);
                intent.putExtra("id", id);
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

    String filePath = null;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    private int recordLength = RECORD_LENGTH; //2分钟

    Handler handler_countdown = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            if (recordLength > 0) {
                voice_length_show.setText(time.format(recordLength));
                handler_countdown.postDelayed(this, 1000);
                recordLength -= 1000;
            }
        }
    };

    SpeechRecognizer mIat = null;

    enum STATE {
        IDLE,
        PLAYING,
        PAUSE
    }

    enum PAGE_STATE {
        STATE0,
        STATE1,
        STATE2
    }

    private PAGE_STATE page_state = PAGE_STATE.STATE0;

    private MediaHelper mediaHelper;
    private Handler playHandler = new Handler();
    private STATE state = STATE.IDLE;
    private Runnable playRunnable = new Runnable() {
        @Override
        public void run() {
            if (state == STATE.PLAYING) {

                //int remaining_time = mediaHelper.maxMillis() - mediaHelper.currentMillis();
                int currentMillis = mediaHelper.currentMillis();
                int maxMillis = mediaHelper.maxMillis();
                voice_length_show.setText(time.format(currentMillis) + "/" + time.format(maxMillis));

                if (currentMillis >= maxMillis) {
                    speak.setImageResource(R.drawable.stop);
                }

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

        // 此方案有缺陷，不采用之
//        // 这个的refresh只是为了缓存加载的一种方案 直接把refresh设置为 height = matchparent 就不用同时控制 内容和 refresh
//        // 注意的是 refresh里面必须有 子控件 它才有效果
//        refresh.setVisibility(View.VISIBLE);
//        refresh.setRefreshing(true);

        // retrieve data
        // 开始刷新 -> 加载 -> 结束刷新
        // 不可见 -> 加载成功 -> 可见
        visibilityController.setVisibility(View.INVISIBLE);
        refresh.setEnabled(false);  // 阻止手动刷新
        requestAndRender();




        //初始为0
        transToStatus0();

        mediaHelper = new MediaHelper();

        //mediaHelper设置为动态生成的  应该对没个新生成的对象监听才有效果
//        mediaHelper.setOnResultListener(new MediaHelper.OnResultListener() {
//            @Override
//            public void onResult() {
//                state = STATE.IDLE;
//                speak.setImageResource(R.drawable.playing);
//            }
//        });

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (page_state == PAGE_STATE.STATE0) {

                    page_state = PAGE_STATE.STATE1;

                    transToStatus1();
                    handler_countdown.post(r);

                    mIat =  ListenHelper.setOnResultListenerWithNoDialog(QuestionAnswerActivity.this, new ListenHelper.OnResultListener() {

                        //成功后回调
                        @Override
                        public void onResult(String fileId, String result) {
//                        ListenHelper.showTip(QuestionAnswerActivity.this, result);
//                        ListenHelper.showTip(QuestionAnswerActivity.this, ListenHelper.getListenerPath(fileId));
//                        voice_length_show.setText(result);
                            filePath = ListenHelper.getListenerPath(fileId);
                            //进入 状态2
                            page_state = PAGE_STATE.STATE2;
                            transToStatus2();
                            try {
                                MediaPlayer tempPlay = new MediaPlayer();
                                tempPlay.setDataSource(filePath);
                                tempPlay.prepare();
                                ACTUAL_RECORD_LENGTH =  (int)Math.ceil(tempPlay.getDuration() / 1000.0);
                                tempPlay.release();
                            } catch (Exception e) {

                            }
                        }

                        //失败后回调
                        @Override
                        public void onError(String errorMsg) {
                            Toast.makeText(QuestionAnswerActivity.this, ToastMsg.RECORD_FAILED, Toast.LENGTH_SHORT).show();

                            speak.setImageResource(R.drawable.microphone);

                            page_state = PAGE_STATE.STATE0;
                            transToStatus0();
                            voice_status_show.setText("输入有错误 请重新输入");
                        }
                    });


                } else if (page_state == PAGE_STATE.STATE1) {

                    if (mIat != null) {
                        mIat.stopListening();
                        handler_countdown.removeCallbacks(r);
                        //这里的话 不设置进入 STATE2 是否进入由 讯飞的回调函数决定
                        //讯飞回调函数 OnError有个延迟 所以需要给用户提示一下
                        speak.setImageResource(R.drawable.microphone);
                        Toast.makeText(QuestionAnswerActivity.this, "分析中", Toast.LENGTH_SHORT).show();

                    }
                    return;

                } else if (page_state == PAGE_STATE.STATE2){

                    if (filePath != null) {

                        // 状态机为  idle -> play -> pause(这个一可能为自己点击的  一方面可能系统自动完成的) -play
                        // 系统自动完成播放的 需要我们在回调中设置等待状态  这里之前有个bug
                        // 注意 系统自动完成的无需设置为idle 因为无需重复加载同一个资源 让media play重新播放即可

                        //目前 利用 星号 表示正在播放 正方形表示中途停止 三角形表示音频从头开始播放
                        if (state == STATE.IDLE) {

                            state = STATE.PLAYING;

                            mediaHelper.play(filePath, new MediaHelper.OnResultListener() {
                                @Override
                                public void onResult() {
                                    //当自然播放完成以后 必须直接设置为等待状态 这样的话 点击时就可以直接播放了
                                    state = STATE.PAUSE;
                                    speak.setImageResource(R.drawable.stop);
                                }
                            });
                            speak.setImageResource(R.drawable.pause);

                            playHandler.post(playRunnable);
                        }
                        else if (state == STATE.PLAYING) {

                            state = STATE.PAUSE;
                            mediaHelper.pause();
                            speak.setImageResource(R.drawable.stop);
                        }
                        else if (state == STATE.PAUSE) {

                            state = STATE.PLAYING;
                            mediaHelper.resume();
                            speak.setImageResource(R.drawable.pause);
                        }
                        else {
                            //
                        }

                        return;
                    }

                } else {

                }

            }
        });

        re_recorded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transToStatus0();
                page_state = PAGE_STATE.STATE0;
            }
        });

    }

    private void requestAndRender() {
        refresh.setRefreshing(true);
        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<QQidResult> call = apiService.requestQQid(id, CurrentUser.userId);

        call.enqueue(new Callback<QQidResult>() {
            @Override
            public void onResponse(Call<QQidResult> call, Response<QQidResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(QuestionAnswerActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    refresh.setRefreshing(false);
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(QuestionAnswerActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                    refresh.setRefreshing(false);
                    return;
                }
                renderView(response.body().data);
                visibilityController.setVisibility(View.VISIBLE);
                refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<QQidResult> call, Throwable t) {
                Toast.makeText(QuestionAnswerActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                refresh.setRefreshing(false);
            }
        });
    }

    private void renderView(QQidBean data) {

        ImageLoader.getInstance().displayImage(GlobalUtil.getInstance().baseAvatarUrl+data.asker_avatarUrl, asker_avatarUrl, GlobalUtil.getInstance().circleBitmapOptions);
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

    //0 hide else show
    private void buttonsShowORHide(int num) {

        if (num  == 0) {
            re_recorded.setVisibility(View.GONE);
            send_recorded.setVisibility(View.GONE);
        } else {
            re_recorded.setVisibility(View.VISIBLE);
            send_recorded.setVisibility(View.VISIBLE);
        }
    }

    private void transToStatus0 () {

//        ListenHelper.clearPlay();
//        mediaHelper.stop();

        if (state == STATE.PLAYING){
            mediaHelper.pause();
        }

        state = STATE.IDLE;
        speak.setTag("0");
        speak.setImageResource(R.drawable.microphone);
        recordLength = RECORD_LENGTH;
        voice_length_show.setText(voice_length_show_text0);
        voice_status_show.setText(voice_status_show_text0);
        buttonsShowORHide(0);
    }

    private void transToStatus1() {
        speak.setImageResource(R.drawable.playing);
        voice_status_show.setText(voice_status_show_text1);
        buttonsShowORHide(0);
    }

    private void transToStatus2 () {
        speak.setImageResource(R.drawable.stop);
        voice_status_show.setText(voice_status_show_text2);
        buttonsShowORHide(1);
    }

}
