package com.example.bankapp.youtu.thread;

import android.os.Handler;


import com.example.bankapp.distinguish.faceDistinguish.face.person.face.YtFaceInfoResult;
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

public class GetFaceInfoThread extends ReqestThread<YtFaceInfoResult> {

    private String faceId;

    public GetFaceInfoThread(Handler mHandler, String faceId, SimpleCallback<YtFaceInfoResult> simpleCallback) {
        super(mHandler, simpleCallback);
        this.faceId = faceId;
    }

    @Override
    protected YtFaceInfoResult initData() throws KeyManagementException, NoSuchAlgorithmException, JSONException, IOException {

        JSONObject jsonObject = YoutuManager.getInstence().GetFaceInfo(faceId);

        YtFaceInfoResult ytFaceInfoResult = GsonUtil.GsonToBean(jsonObject.toString(), YtFaceInfoResult.class);

        return ytFaceInfoResult;
    }
}
