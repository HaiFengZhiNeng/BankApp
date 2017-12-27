package com.example.bankapp.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by zhangyuanyuan on 2017/11/16.
 */

public class DanceUtils {

    private static DanceUtils mDanceManager;

    private MediaPlayer mediaPlayer;

    private AssetFileDescriptor fileDescriptor;
    private AudioManager mAudioManager;

    public static synchronized DanceUtils getInstance() {
        if (mDanceManager == null) {
            synchronized (DanceUtils.class) {
                if (mDanceManager == null) {
                    mDanceManager = new DanceUtils();
                }
            }
        }
        return mDanceManager;
    }


    private boolean initMedia(Context context) {
        /**
         * 把音乐音量强制设置为最大音量
         */
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前音乐音量
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获取最大声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0); // 设置为最大声音，可通过SeekBar更改音量大小
        try {
            fileDescriptor = context.getAssets().openFd("wd.mp3");

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void initMediaplayer() {
        try {
            mediaPlayer = new MediaPlayer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startDance(Context context, final OnMusicCompletionListener onCompletionListener) {
        if (mediaPlayer == null) {
            initMediaplayer();
        } else {
            mediaPlayer.reset();
        }
        if (initMedia(context)) {

            if (!mediaPlayer.isPlaying()) {
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        //根据需要添加自己的代码。。。
                        onCompletionListener.onCompletion(true);
                    }
                });
            }
        }
    }

    public void stopDance() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }
    }

    public interface OnMusicCompletionListener {
        void onCompletion(boolean isPlaySuccess);
    }

}
