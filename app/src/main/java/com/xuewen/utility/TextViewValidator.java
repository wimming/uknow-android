package com.xuewen.utility;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/12/07.
 */

public class TextViewValidator {

    //可以传入多个 edittext textview  存在为空 返回true  不存在 返回false
    public static boolean isExistEmpty(TextView... textViews) {

        for (TextView textView: textViews) {
            if (textView.getText().toString().equals("")) {
                return true;
            }
        }
        return false;
    }
}
