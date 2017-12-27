package com.example.bankapp.youtu.thread;

import android.graphics.Bitmap;
import android.os.Handler;


import com.example.bankapp.distinguish.faceDistinguish.face.person.face.YtFaceIdentify;
import com.example.bankapp.util.GsonUtil;
import com.example.bankapp.youtu.YoutuManager;
import com.example.bankapp.youtu.callback.SimpleCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhangyuanyuan on 2017/9/18.
 */

public class FaceIdentifyThread extends ReqestThread<YtFaceIdentify> {

    private Bitmap bitmap;

    public FaceIdentifyThread(Handler mHandler, Bitmap bitmap, SimpleCallback<YtFaceIdentify> simpleCallback) {
        super(mHandler, simpleCallback);
        this.bitmap = bitmap;
    }

    @Override
    protected YtFaceIdentify initData() throws KeyManagementException, NoSuchAlgorithmException, JSONException, IOException {

        JSONObject jsonObject = YoutuManager.getInstence().FaceIdentify(bitmap, YoutuManager.getGroupId());
        YtFaceIdentify ytFaceIdentify = GsonUtil.GsonToBean(jsonObject.toString(), YtFaceIdentify.class);

        return ytFaceIdentify;
    }
}
