package com.xuewen.xuewen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.utility.GlobalUtil;

/**
 * Created by ym on 16-10-22.
 */

public class QuestionDetailActivity extends AppCompatActivity {

    private ImageView header_avatar;
    private ImageView avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        avatar2 = (ImageView) findViewById(R.id.avatar2);
//        header_avatar = (ImageView) findViewById(R.id.header_avatar);

//        ImageLoader.getInstance().displayImage("drawable://" +  R.drawable.avatar2, avatar2, GlobalUtil.getInstance().circleBitmapOptions);
//        ImageLoader.getInstance().displayImage("drawable://" +  R.drawable.avatar2, header_avatar, GlobalUtil.getInstance().circleBitmapOptions);

//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(android.R.drawable.star_on);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
