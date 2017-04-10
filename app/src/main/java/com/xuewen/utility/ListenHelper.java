package com.xuewen.utility;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * Created by Administrator on 2016/11/24.
 */
public class ListenHelper {

    public static void Init(Context context) {
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=582b3f8f");
    }

    //这个函数为异步的
    public static void listen(final Context context) {
        RecognizerDialog mDialog = new RecognizerDialog(context, null);
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");

        mDialog.setListener(new RecognizerDialogListener() {

            private String finalResults = "";
            @Override
            public void onResult(RecognizerResult results, boolean isLast) {

                //语音听写的结果 需要拼接
                finalResults += parseResult(results);
                if (isLast) {
                    Toast.makeText(context, finalResults, Toast.LENGTH_SHORT).show();
                    finalResults = "";
                }
            }

            @Override
            public void onError(SpeechError error) {
                Toast.makeText(context, error.getPlainDescription(true), Toast.LENGTH_SHORT).show();
            }
        });

        mDialog.show();
    }

    // 因为listen接收数据异步，所以定义一个监听回调形式接口
    public interface OnResultListener {
        void onResult(String fileId, String result);
        void onError(String errorMsg);
    }

    public static void listenWithCallback(final Context context, final OnResultListener onResultListener) {

        RecognizerDialog mDialog = new RecognizerDialog(context, null);
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mDialog.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        final UUID id = UUID.randomUUID();
        mDialog.setParameter(SpeechConstant.ASR_AUDIO_PATH,  getListenersDirectory() + id.toString() + ".wav");

        mDialog.setListener(new RecognizerDialogListener() {
            private String finalResults = "";
            @Override
            public void onResult(RecognizerResult results, boolean isLast) {

                //语音听写的结果 需要拼接
                finalResults += parseResult(results);
                if (isLast) {
                    onResultListener.onResult(id.toString(), finalResults);
                }
            }

            @Override
            public void onError(SpeechError error) {
                onResultListener.onError(error.getPlainDescription(true));
            }
        });

        mDialog.show();
    }

    // 没有系统对话框的录音
    // 返回穿件的录音对象 用于给用户控制是否关闭录音 和释放资源
    public static SpeechRecognizer listenWithoutDialogWithCallback(final Context context, final OnResultListener onResultListener) {
        //创建无系统动画对象
        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(context, null);

        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");

        // 设置语音前端点：静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "120000");
        // 设置语音后端点：后端点静音检测时间，即用户停止说话多长时间内即认为不再输入，自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "120000");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        final UUID id = UUID.randomUUID();
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, getListenersDirectory() + id.toString() + ".wav");

        mIat.startListening(new RecognizerListener() {

            private String finalResults = "";
            @Override
            public void onVolumeChanged(int i, byte[] bytes) {

            }

            @Override
            public void onBeginOfSpeech() {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                // 语音听写的结果 需要拼接
                finalResults += parseResult(recognizerResult);
                if (isLast) {
                    onResultListener.onResult(id.toString(), finalResults);
                }
            }

            @Override
            public void onError(SpeechError speechError) {
                onResultListener.onError(speechError.getPlainDescription(true));
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });

        return mIat;
    }

    //解析结果
    private static String parseResult(RecognizerResult results) {
        // 用HashMap存储听写结果
        HashMap<String, String> mIatResults = new LinkedHashMap<>();
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {

            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        return resultBuffer.toString();
    }

    private static String getListenersDirectory() {
        return Environment.getExternalStorageDirectory()+"/msc/";
    }

    public static String getListenerPath(String filenameId) {
        return getListenersDirectory() + filenameId + ".wav";
    }

}
