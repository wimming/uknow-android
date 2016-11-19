package com.xuewen.xuewen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.utility.GlobalUtil;

public class ModifyInfoActivity extends AppCompatActivity {
    ImageView avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        avatar = (ImageView) findViewById(R.id.avatar);
        ImageLoader.getInstance().displayImage("drawable://" +  R.drawable.avatar, avatar, GlobalUtil.getInstance().circleBitmapOptions);
    }
}
