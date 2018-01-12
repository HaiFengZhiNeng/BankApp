package com.example.bankapp.asr;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.bankapp.BankApplication;
import com.example.bankapp.modle.local.Asr;
import com.example.bankapp.modle.local.Cw;
import com.example.bankapp.modle.local.Ws;
import com.example.bankapp.util.GsonUtil;
import com.example.bankapp.util.voice.JsonParser;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;

import java.util.List;

/**
 * Created by dell on 2017/9/20.
 */

public class MyRecognizerListener implements RecognizerListener {
    public MyRecognizerListener(RecognListener recognListener) {
        this.recognListener = recognListener;
    }

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
    public void onResult(RecognizerResult recognizerResult, boolean b) {
        final String engineType = BankApplication.getInstance().getEngineType();
//        if (engineType.equals(SpeechConstant.TYPE_LOCAL)) {
//            if (null != recognizerResult && !TextUtils.isEmpty(recognizerResult.getResultString())) {
////                String text = JsonParser.parseGrammarResult(recognizerResult.getResultString(), engineType);
//                Asr local = GsonUtil.GsonToBean(recognizerResult.getResultString(), Asr.class);
//
//                if (local.getSc() > 30) {
//                    StringBuffer sb = new StringBuffer();
//                    List<Ws> wsList = local.getWs();
//                    for (int i = 0; i < wsList.size(); i++) {
//                        Ws ws = wsList.get(i);
//                        List<Cw> cwList = ws.getCw();
//                        for (int j = 0; j < cwList.size(); j++) {
//                            Cw cw = cwList.get(j);
//                            sb.append(cw.getW());
//                        }
//                    }
//                    Log.e("GG", "置信度" + local.getSc());
//                    recognListener.onResult(sb.toString().trim());
//                } else {
//                    recognListener.onResult("置信度太小");
//                }
//            } else {
//                Log.e("GG", "recognizer result : null");
//                recognListener.onRecognDown();
//            }
//
//        } else
        if (engineType.equals(SpeechConstant.TYPE_CLOUD)) {
            if (!b) {
//                String text = JsonParser.parseIatResult(recognizerResult.getResultString());
                StringBuffer sb = new StringBuffer();
                Asr line = GsonUtil.GsonToBean(recognizerResult.getResultString(), Asr.class);
                List<Ws> wsList = line.getWs();
                for (int i = 0; i < wsList.size(); i++) {
                    Ws ws = wsList.get(i);
                    List<Cw> cwList = ws.getCw();
                    for (int j = 0; j < cwList.size(); j++) {
                        Cw cw = cwList.get(j);
                        sb.append(cw.getW());
                    }
                }
                if (recognListener != null) {
                    if (!TextUtils.isEmpty(sb.toString())) {
                        recognListener.onResult(sb.toString().trim());
                    } else {
                        recognListener.onRecognDown();
                        Log.e("GG", "onRecognDown ： null");
                    }
                } else {
                    recognListener.onRecognDown();
                }
            }
        }

//        if (!b) {
//            String text = JsonParser.parseIatResult(recognizerResult.getResultString());
//            if(recognListener != null) {
//                recognListener.onResult(text);
//            }
//        }
    }

    @Override
    public void onError(SpeechError speechError) {
        Log.e("SpeechError", speechError.getErrorCode() + "");
        Log.e("SpeechError", speechError.toString() + "");
        if (10118 == speechError.getErrorCode()) {
            if (null != recognListener) {
                recognListener.onRecognDown();
            }
        } else if (20006 == speechError.getErrorCode()) {
            if (null != recognListener) {
                recognListener.onErrInfo();
            }
        } else if (10114 == speechError.getErrorCode()) {
            recognListener.onNetwork();
        } else if (10108 == speechError.getErrorCode()) {
            recognListener.onNetwork();
        } else if (10107 == speechError.getErrorCode()) {
//            recognListener.onNetwork();
        }
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {

    }

    private RecognListener recognListener;

    public interface RecognListener {
        void onResult(String result);

        void onErrInfo();

        void onRecognDown();

        void onNetwork();

        void onLocalError();

        void onInsufficient();
    }
}
