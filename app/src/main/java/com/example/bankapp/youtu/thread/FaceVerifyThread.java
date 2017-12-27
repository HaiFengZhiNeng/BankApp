package com.example.bankapp.youtu.thread;


import android.graphics.Bitmap;
import android.os.Handler;

import com.example.bankapp.distinguish.faceDistinguish.face.person.YtVerifyperson;
import com.example.bankapp.util.GsonUtil;
import com.example.bankapp.youtu.YoutuManager;
import com.example.bankapp.youtu.callback.SimpleCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhangyuanyuan on 2017/9/5.
 */

public class FaceVerifyThread extends ReqestThread<YtVerifyperson> {

    private String personId;
    private Bitmap bitmap;

    public FaceVerifyThread(Handler mHandler, String personId, Bitmap bitmap, SimpleCallback<YtVerifyperson> simpleCallback) {
        super(mHandler, simpleCallback);
        this.personId = personId;
        this.bitmap = bitmap;
    }

    @Override
    protected YtVerifyperson initData() throws KeyManagementException, NoSuchAlgorithmException, JSONException, IOException {

        JSONObject jsonObject = YoutuManager.getInstence().FaceVerify(bitmap, personId);

        YtVerifyperson ytVerifyperson = GsonUtil.GsonToBean(jsonObject.toString(), YtVerifyperson.class);

        return ytVerifyperson;
    }
}
