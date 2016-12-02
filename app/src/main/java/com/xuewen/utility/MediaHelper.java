package com.xuewen.utility;

import android.media.MediaPlayer;

/**
 * Created by Administrator on 2016/12/02.
 */

public class MediaHelper {

    public interface OnResultListener {
        public void onResult();
    }
    private OnResultListener onResultListener;
    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    private MediaPlayer mediaPlayer = new MediaPlayer();

    public MediaHelper() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (onResultListener != null) {
                    onResultListener.onResult();
                }
            }
        });
    }

    public  boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public int currentMillis() {
        return mediaPlayer.getCurrentPosition();
    }

    public int maxMillis() {
        return mediaPlayer.getDuration();
    }

    public void play(String filePath) {
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        mediaPlayer.pause();
    }
    public void resume() {
        mediaPlayer.start();
    }
    public void stop() {
        mediaPlayer.stop();
        mediaPlayer.reset();
    }
}
