package com.xuewen.utility;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/12/07.
 */

public class MyTextWatcher implements TextWatcher{

    private Context context;
    private int length = 0;
    private TextView textView;

    //textView 为动态监听的textview lenght 最大长度
    public MyTextWatcher(Context context, int length, TextView textView) {
        this.context = context;
        this.length = length;
        this.textView = textView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.length() == length) {
            Toast.makeText(context, "最多输入" + length + "个字", Toast.LENGTH_LONG).show();
        }
        textView.setText(s.length() + "/" + length);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
