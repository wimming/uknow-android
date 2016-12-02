package com.xuewen.xuewen;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iflytek.cloud.SpeechRecognizer;
import com.xuewen.utility.ListenHelper;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnswerQuestionActivity extends AppCompatActivity {

    @BindView(R.id.speak)
    ImageView speak;
    @BindView(R.id.voice_length_show)
    TextView voice_length_show;
    @BindView(R.id.voice_status_show)
    TextView voice_status_show;

    String filePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);
        ButterKnife.bind(this);

        //初始为0
        speak.setTag("0");

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //tag状态
                // 可以播放状态
                if (speak.getTag().equals("2")) {
                    if (filePath != null) {
                        ListenHelper.playListener(filePath);
                        return;
                    }
                }


                speak.setImageResource(R.drawable.playing);

                final SpeechRecognizer mIat =  ListenHelper.setOnResultListenerWithNoDialog(AnswerQuestionActivity.this, new ListenHelper.OnResultListener() {
                    @Override
                    public void onResult(String fileId, String result) {
                        ListenHelper.showTip(AnswerQuestionActivity.this, ListenHelper.getListenerPath(fileId));
                        voice_length_show.setText(result);
                        filePath = ListenHelper.getListenerPath(fileId);
                    }

                    @Override
                    public void onError(String errorMsg) {
                        ListenHelper.showTip(AnswerQuestionActivity.this, errorMsg + "哈哈哈 出错啦");
                        speak.setImageResource(R.drawable.microphone);
                    }
                });


                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        //停止
                        mIat.stopListening();
                        ListenHelper.showTip(AnswerQuestionActivity.this,  "stop 这里");
                        speak.setTag("2");
                        speak.setImageResource(R.drawable.stop);
                    }
                };

                handler.postDelayed(runnable, 3000);


            }
        });


    }
}
