package com.xuewen.utility;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ym on 16-11-24.
 */

public class ToastMsg {
    public static final String NETWORK_ERROR = "网络请求失败";
    public static final String SERVER_ERROR = "服务器错误";
    public static final String APPLICATION_ERROR = "应用程序错误";
    public static final String FILE_OPERATION_ERROR = "文件操作错误";

    public static final String ARG_INVALID_EMPTY = "输入字段不能为空";
    public static final String MODIFY_SUCCESS = "修改成功";

    public static void showTips(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
