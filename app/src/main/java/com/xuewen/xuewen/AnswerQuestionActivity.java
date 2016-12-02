package com.xuewen.xuewen;


import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.SpeechRecognizer;
import com.xuewen.networkservice.APITestActivity;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.QQidAResult;
import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.ListenHelper;
import com.xuewen.utility.MediaHelper;
import com.xuewen.utility.ToastMsg;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerQuestionActivity extends AppCompatActivity {

    final static  String voice_length_show_text0 = "录音最常2分钟";
    final  static  String voice_status_show_text0 = "点击按钮开始";
    final  static  String voice_status_show_text1 = "点击按钮结束";
    final  static  String voice_status_show_text2 = "点击按钮试听";
    final  static  int RECOND_LENGTH = 120000;

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
            Toast.makeText(AnswerQuestionActivity.this, "ni ha mei you lu yin", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiService.retrofit.create(ApiService.class);

        File file = new File(filePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileBody = MultipartBody.Part.createFormData("audio", file.getName(), requestBody);

        Call<QQidAResult> call = apiService.requestQQidA(3,
                fileBody,
                RequestBody.create(MediaType.parse("multipart/form-data"), CurrentUser.userId+""));
        call.enqueue(new Callback<QQidAResult>() {
            @Override
            public void onResponse(Call<QQidAResult> call, Response<QQidAResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AnswerQuestionActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(AnswerQuestionActivity.this, response.body().errmsg, Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(AnswerQuestionActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<QQidAResult> call, Throwable t) {
                Toast.makeText(AnswerQuestionActivity.this, ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    String filePath = null;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    private int recordLength = RECOND_LENGTH; //2分钟

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

                int remaining_time = mediaHelper.maxMillis() - mediaHelper.currentMillis();
                voice_length_show.setText((time.format(remaining_time)));

                if (remaining_time <= 0) {
                    speak.setImageResource(R.drawable.stop);
                }

            }
            playHandler.postDelayed(playRunnable, 200);
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);
        ButterKnife.bind(this);

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

                    ListenHelper.showTip(AnswerQuestionActivity.this, "aaa");
                    page_state = PAGE_STATE.STATE1;

                    transToStatus1();
                    handler_countdown.post(r);

                    mIat =  ListenHelper.setOnResultListenerWithNoDialog(AnswerQuestionActivity.this, new ListenHelper.OnResultListener() {

                        //成功后回调
                        @Override
                        public void onResult(String fileId, String result) {
//                        ListenHelper.showTip(AnswerQuestionActivity.this, result);
//                        ListenHelper.showTip(AnswerQuestionActivity.this, ListenHelper.getListenerPath(fileId));
//                        voice_length_show.setText(result);
                            filePath = ListenHelper.getListenerPath(fileId);
                            //进入 状态2
                            page_state = PAGE_STATE.STATE2;
                            transToStatus2();
                        }

                        //失败后回调
                        @Override
                        public void onError(String errorMsg) {
                            ListenHelper.showTip(AnswerQuestionActivity.this, errorMsg + "哈哈哈 出错啦");
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
                    }
                    return;

                } else if (page_state == PAGE_STATE.STATE2){

                    ListenHelper.showTip(AnswerQuestionActivity.this, "bbb");
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
                            speak.setImageResource(R.drawable.star_full);

                            playHandler.post(playRunnable);
                        }
                        else if (state == STATE.PLAYING) {

                            state = STATE.PAUSE;
                            mediaHelper.pause();
                            speak.setImageResource(R.drawable.playing);
                        }
                        else if (state == STATE.PAUSE) {

                            state = STATE.PLAYING;
                            mediaHelper.resume();
                            speak.setImageResource(R.drawable.star_full);
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
        recordLength = RECOND_LENGTH;
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
