package com.example.bankapp.youtu.thread;

import android.graphics.Bitmap;
import android.os.Handler;


import com.example.bankapp.distinguish.faceDistinguish.face.person.YtAddperson;
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
 * Created by zhangyuanyuan on 2017/9/5.
 */

public class AddFaceThread extends ReqestThread<YtAddperson> {

    private String personId;
    private List<Bitmap> bitmaps;

    public AddFaceThread(Handler mHandler, String personId, List<Bitmap> bitmaps, SimpleCallback<YtAddperson> simpleCallback) {
        super(mHandler, simpleCallback);
        this.personId = personId;
        this.bitmaps = bitmaps;
    }

    @Override
    protected YtAddperson initData() throws KeyManagementException, NoSuchAlgorithmException, JSONException, IOException {

        JSONObject jsonObject = YoutuManager.getInstence().AddFace(personId, bitmaps);
        YtAddperson ytAddperson = GsonUtil.GsonToBean(jsonObject.toString(), YtAddperson.class);

        return ytAddperson;
    }
}
