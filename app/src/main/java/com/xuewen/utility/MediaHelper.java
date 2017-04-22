package com.xuewen.utility;

import android.media.MediaPlayer;

/**
 * Created by Administrator on 2016/12/02.
 */

public class MediaHelper {

    public interface OnResultListener {
        void onResult();
    }
    private OnResultListener onResultListener;
    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    private MediaPlayer mediaPlayer = new MediaPlayer();

    public int maxMillis() {
        return mediaPlayer.getDuration();
    }
    public int currentMillis() {
        return mediaPlayer.getCurrentPosition();
    }

    public void reset() {
        mediaPlayer.reset();
    }
    public void pause() {
        mediaPlayer.pause();
    }
    public void start() {
        mediaPlayer.start();
    }
    public void release() {
        mediaPlayer.release();
    }

    public void playOnce(String filePath, final OnResultListener onResultListener ) {
        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    onResultListener.onResult();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
