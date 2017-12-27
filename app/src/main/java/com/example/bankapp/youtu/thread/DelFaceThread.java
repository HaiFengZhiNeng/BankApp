package com.example.bankapp.youtu.thread;

import android.os.Handler;


import com.example.bankapp.distinguish.faceDistinguish.face.person.face.YtDelface;
import com.example.bankapp.util.GsonUtil;
import com.example.bankapp.youtu.YoutuManager;
import com.example.bankapp.youtu.callback.SimpleCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/6.
 */

public class DelFaceThread extends ReqestThread<YtDelface> {

    private String personId;
    private List<String> face_id_arr;

    public DelFaceThread(Handler mHandler, String personId, List<String> face_id_arr, SimpleCallback<YtDelface> simpleCallback) {
        super(mHandler, simpleCallback);
        this.personId = personId;
        this.face_id_arr = face_id_arr;
    }

    @Override
    protected YtDelface initData() throws KeyManagementException, NoSuchAlgorithmException, JSONException, IOException {
        JSONObject jsonObject = YoutuManager.getInstence().DelFace(personId, face_id_arr);

        YtDelface ytDelface = GsonUtil.GsonToBean(jsonObject.toString(), YtDelface.class);
        return ytDelface;
    }
}
