package com.xuewen.customview;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.xuewen.xuewen.R;

import java.io.IOException;

/**
 * Created by huangyuming on 17-4-3.
 */

public class AudioPlayerView extends LinearLayout {

    private Context context;
    private SeekBar seekBar;
    private Button listenButton;
    private String url;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean firstLock = true;
    private boolean userCtrling = false;
    private ProgressDialog progressDialog;

    private OnCompleteListener onCompleteListener;

    Handler progressHandler = new Handler();
    Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();
            if (!userCtrling) {
                seekBar.setProgress(seekBar.getMax() * position / duration);
            }
            listenButton.setText((duration-position)/1000+"''");
            progressHandler.postDelayed(progressRunnable, 50);
        }
    };

    public AudioPlayerView(Context context) {
        super(context);
        init(context);
    }

    public AudioPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AudioPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

//    public AudioPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init(context);
//    }

    public void prepare(String url, int audioSecs) {
        this.url = url;
        listenButton.setText(audioSecs+"''");
    }
    public void release() {
        progressHandler.removeCallbacks(progressRunnable);
        mediaPlayer.release();
    }
    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    private void init(final Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_audio_player, this);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        listenButton = (Button) findViewById(R.id.listenButton);

        listenButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstLock) {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.setDataSource(url);
                        mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
                        firstLock = false;
                        progressDialog = ProgressDialog.show(context, "", "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    } else {
                        mediaPlayer.pause();
                    }
                }
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.e("TAG", "percent:  " + percent);
                seekBar.setSecondaryProgress(seekBar.getMax() * percent / 100);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("TAG", "onCompletion is called!");
                if (onCompleteListener != null) {
                    onCompleteListener.onComplete();
                }
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("TAG", "onError === what:  " + what + " extra: " + extra);
                return false;
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.e("TAG", "onPrepared");
                mediaPlayer.start();
                seekBar.setVisibility(VISIBLE);
                progressHandler.post(progressRunnable);
                progressDialog.dismiss();
            }
        });
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                Log.e("TAG", "onSeekComplete");
            }
        });
//        mediaPlayer.setOn
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                Log.e("TAG", "onInfo   what:  " + what + " extra: " + extra);
                return false;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    userCtrling = true;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("TAG", "start tracking . progress "+seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("TAG", "stop tracking");
                userCtrling = false;
            }
        });
    }

    public interface OnCompleteListener {
        void onComplete();
    }

}
