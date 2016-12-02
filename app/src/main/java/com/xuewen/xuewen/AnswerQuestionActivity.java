package com.xuewen.xuewen;


import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iflytek.cloud.SpeechRecognizer;
import com.xuewen.utility.ListenHelper;
import com.xuewen.utility.MediaHelper;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private MediaHelper mediaHelper;
    private Handler playHandler = new Handler();
    private STATE state = STATE.IDLE;
    private Runnable playRunnable = new Runnable() {
        @Override
        public void run() {
            if (state == STATE.PLAYING) {
                voice_length_show.setText((time.format(mediaHelper.maxMillis() - mediaHelper.currentMillis())));
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
        mediaHelper.setOnResultListener(new MediaHelper.OnResultListener() {
            @Override
            public void onResult() {
                state = STATE.IDLE;
                speak.setImageResource(R.drawable.playing);
            }
        });

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //tag状态
                // 当前为 status2
                if (speak.getTag().equals("2")) {
                    if (filePath != null) {

//                        if (ListenHelper.isPlaying()) {
//                            speak.setImageResource(R.drawable.star_full);
//                            ListenHelper.stopPlay();
//
//                        } else {
//                            ListenHelper.playListener(filePath);
//                            speak.setImageResource(R.drawable.playing);
//                        }



                        if (state == STATE.IDLE) {
                            state = STATE.PLAYING;
                            mediaHelper.play(filePath);
                            speak.setImageResource(R.drawable.star_full);

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
                            speak.setImageResource(R.drawable.star_full);
                        }
                        else {
                            //
                        }

//                        if (MediaHelper.isPlaying()) {
//                            speak.setImageResource(R.drawable.playing);
//                            MediaHelper.stop();
//                            return;
//                        } else {
//                            speak.setImageResource(R.drawable.star_full);
//                            MediaHelper.play(filePath, new MediaHelper.OnResultListener() {
//                                @Override
//                                public void onResult() {
//                                    speak.setImageResource(R.drawable.playing);
//                                }
//                            });
//                        }

                        return;
                    }
                }

                //当前为status1
                if (speak.getTag().equals("1")) {

                    if (mIat != null) {
                        mIat.stopListening();
                        handler_countdown.removeCallbacks(r);
                        speak.setImageResource(R.drawable.stop);
                        voice_status_show.setText(voice_status_show_text2);
                        buttonsShowORHide(1);
                        speak.setTag("2");
                    }
                    return;
                }

                speak.setImageResource(R.drawable.playing);
                voice_status_show.setText(voice_status_show_text1);
                handler_countdown.postDelayed(r, 0);
                speak.setTag("1");


                mIat =  ListenHelper.setOnResultListenerWithNoDialog(AnswerQuestionActivity.this, new ListenHelper.OnResultListener() {

                    //成功后回调
                    @Override
                    public void onResult(String fileId, String result) {
                        ListenHelper.showTip(AnswerQuestionActivity.this, result);
                        ListenHelper.showTip(AnswerQuestionActivity.this, ListenHelper.getListenerPath(fileId));
//                        voice_length_show.setText(result);
                        filePath = ListenHelper.getListenerPath(fileId);
                    }

                    //失败后回调
                    @Override
                    public void onError(String errorMsg) {
                        ListenHelper.showTip(AnswerQuestionActivity.this, errorMsg + "哈哈哈 出错啦");
                        speak.setImageResource(R.drawable.microphone);
                    }
                });

            }
        });

        re_recorded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transToStatus0();
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
//        MediaHelper.stop();
        state = STATE.IDLE;
        speak.setTag("0");
        speak.setImageResource(R.drawable.microphone);
        recordLength = RECOND_LENGTH;
        voice_length_show.setText(voice_length_show_text0);
        voice_status_show.setText(voice_status_show_text0);
        buttonsShowORHide(0);
    }

}
