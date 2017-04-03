package com.xuewen.xuewen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xuewen.utility.ToastMsg;

public class QuestionAnswerSuccessActivity extends AppCompatActivity {

    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_success);

        id = getIntent().getIntExtra("id", -1);
        if (id == -1) {
            Toast.makeText(QuestionAnswerSuccessActivity.this, ToastMsg.APPLICATION_ERROR, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        TextView checkQuestionDetail = (TextView) findViewById(R.id.checkQuestionDetail);
        checkQuestionDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(QuestionAnswerSuccessActivity.this, ProfileActivity.class);
//                intent.putExtra("id", id);
//                startActivity(intent);
                finish();
            }
        });
    }
}
