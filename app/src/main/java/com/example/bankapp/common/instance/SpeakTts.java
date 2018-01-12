package com.example.bankapp.common.instance;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.bankapp.BankApplication;
import com.example.bankapp.common.Constants;
import com.example.bankapp.util.PreferencesUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;

/**
 * 语音合成
 *
 * @author Guanluocang
 *         created at 2017/12/25 13:31
 */

public class SpeakTts {

    private Context mContext;

    private SpeechSynthesizer mTts;

    private String mSpokesman;

    private String TTS_LANGUAGE = "en_us";

    private String TTS_SPEED = "70";
    private String TTS_VOLUME = "100";
    private String TTS_PITCH = "50";
    private String TTS_STREAM_TYPE = "3";
    private String TTS_KEY_REQUEST_FOCUS = "true";
    private String TTS_AUDIO_FORMAT = "wav";
    private String TTS_TTS_AUDIO_PATH = Environment.getExternalStorageDirectory() + "/msc/tts.wav";
    private String TTS_KEY_SPEECH_TIMEOUT = "100";

    private volatile static SpeakTts mSpeakTts;

    public static SpeakTts getInstance() {
        if (mSpeakTts == null) {
            synchronized (SpeakTts.class) {
                if (mSpeakTts == null) {
                    mSpeakTts = new SpeakTts();
                }
            }
        }
        return mSpeakTts;
    }

    private SpeakTts() {
        this.mContext = BankApplication.getInstance().getApplicationContext();
        mSpokesman = "xiaoyan";//vinn
    }


    public void initTts() {
        mTts = SpeechSynthesizer.createSynthesizer(mContext, new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    Log.e("gg", "初始化失败,错误码：" + code);
                }
            }
        });

        String engineType = BankApplication.getInstance().getEngineType();

        mTts.setParameter(SpeechConstant.PARAMS, null);
        if (engineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            mTts.setParameter(SpeechConstant.VOICE_NAME, mSpokesman);
        }
//        else if (engineType.equals(SpeechConstant.TYPE_LOCAL)) {
//            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
//            mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResTtsPath());
        //发言人
//            mTts.setParameter(SpeechConstant.VOICE_NAME, mSpokesman);
//        }
        //说话速度
        mTts.setParameter(SpeechConstant.SPEED, TTS_SPEED);
        //说话音量
        mTts.setParameter(SpeechConstant.VOLUME, TTS_VOLUME);
        //说话语调
        mTts.setParameter(SpeechConstant.PITCH, TTS_PITCH);
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, TTS_STREAM_TYPE);
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, TTS_KEY_REQUEST_FOCUS);
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
        mTts.setParameter(SpeechConstant.LANGUAGE, TTS_LANGUAGE);
        //开启VAD
        mTts.setParameter(SpeechConstant.VAD_ENABLE, "1");
        //会话最长时间
        mTts.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, TTS_KEY_SPEECH_TIMEOUT);
        mTts.setParameter(SpeechConstant.MIXED_THRESHOLD, "30");
    }

    public void stopTts() {
        mTts.stopSpeaking();
    }

    public void doAnswer(String answer, SynthesizerListener listener) {
        mTts.startSpeaking(answer, listener);
    }

    //设置发音人
    public void setSpokesman(String spokesman) {
        this.mSpokesman = spokesman;
        mTts.setParameter(SpeechConstant.VOICE_NAME, mSpokesman);
    }

    private String getResTtsPath() {
        StringBuffer tempBuffer = new StringBuffer();
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "tts/" + mSpokesman + ".jet"));
        return tempBuffer.toString();
    }
}
