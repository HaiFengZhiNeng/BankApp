package com.example.bankapp.common.instance;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.bankapp.BankApplication;
import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhangyuanyuan on 2017/11/30.
 */

public class SpeakAiui {

    private Context mContext;

    private AIUIAgent mAIUIAgent;

    private volatile static SpeakAiui mSpeakAiui;

    public static SpeakAiui getInstance() {
        if (mSpeakAiui == null) {
            synchronized (SpeakAiui.class) {
                if (mSpeakAiui == null) {
                    mSpeakAiui = new SpeakAiui();
                }
            }
        }
        return mSpeakAiui;
    }

    private SpeakAiui() {
        this.mContext = BankApplication.getInstance().getApplicationContext();
    }

    public void initAiui(AIUIListener listener) {
        String params = "";
        AssetManager assetManager = mContext.getResources().getAssets();
        try {
            InputStream ins = assetManager.open("cfg/aiui_phone.cfg");
            byte[] buffer = new byte[ins.available()];
            ins.read(buffer);
            ins.close();
            params = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mAIUIAgent = AIUIAgent.createAgent(mContext, params, listener);
        AIUIMessage startMsg = new AIUIMessage(AIUIConstant.CMD_START, 0, 0, null, null);
        mAIUIAgent.sendMessage(startMsg);
    }

        public void startAiuiListener() {
        AIUIMessage aiuiMessage1 = new AIUIMessage(AIUIConstant.CMD_START, 0, 0, null, null);
        mAIUIAgent.sendMessage(aiuiMessage1);

        AIUIMessage aiuiMessage = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, null, null);
        mAIUIAgent.sendMessage(aiuiMessage);

        String paramss = "sample_rate=16000,data_type=audio";
        AIUIMessage writeMsg = new AIUIMessage(AIUIConstant.CMD_START_RECORD, 0, 0, paramss, null);
        mAIUIAgent.sendMessage(writeMsg);
    }

    public void stopAiuiListener() {
        String paramss = "sample_rate=16000,data_type=audio";
        AIUIMessage writeMsg = new AIUIMessage(AIUIConstant.CMD_STOP_RECORD, 0, 0, paramss, null);
        mAIUIAgent.sendMessage(writeMsg);
        AIUIMessage aiuiMessage = new AIUIMessage(AIUIConstant.CMD_STOP, 0, 0, null, null);
        mAIUIAgent.sendMessage(aiuiMessage);
    }

    public void aiuiWriteText(String text) {
        if (TextUtils.isEmpty(text)) {
            Log.e("gg", "aiuiWriteText null");
            text = "";
        }
        String params = "data_type=text";
        AIUIMessage msg = new AIUIMessage(AIUIConstant.CMD_WRITE, 0, 0, params, text.getBytes());
        mAIUIAgent.sendMessage(msg);
    }
}
