package com.example.bankapp.youtu.thread;

import android.graphics.Bitmap;
import android.os.Handler;

import com.example.bankapp.distinguish.faceDistinguish.face.person.face.YtDetectFace;
import com.example.bankapp.util.GsonUtil;
import com.example.bankapp.youtu.YoutuManager;
import com.example.bankapp.youtu.callback.SimpleCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhangyuanyuan on 2017/9/6.
 */

public class DetectFaceThread extends ReqestThread<YtDetectFace> {

    private Bitmap bitmap;
    private int mode;

    public DetectFaceThread(Handler mHandler, Bitmap bitmap, int mode, SimpleCallback<YtDetectFace> simpleCallback) {
        super(mHandler, simpleCallback);
        this.bitmap = bitmap;
        this.mode = mode;
    }

    @Override
    protected YtDetectFace initData() throws KeyManagementException, NoSuchAlgorithmException, JSONException, IOException {
        JSONObject jsonObject = YoutuManager.getInstence().detectFace(bitmap, mode);
        YtDetectFace ytDetectFace = GsonUtil.GsonToBean(jsonObject.toString(), YtDetectFace.class);
        new Thread(){

        }.start();
        return ytDetectFace;
    }
}
