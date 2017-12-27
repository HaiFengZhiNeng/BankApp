package com.example.bankapp.youtu.thread;

import android.graphics.Bitmap;
import android.os.Handler;

import com.example.bankapp.distinguish.faceDistinguish.face.person.YtNewperson;
import com.example.bankapp.util.GsonUtil;
import com.example.bankapp.youtu.YoutuManager;
import com.example.bankapp.youtu.callback.SimpleCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/5.
 */

public class NewPersonThread extends ReqestThread<YtNewperson> {

    private String personId;
    private Bitmap bitmap;

    public NewPersonThread(Handler mHandler, String personId, Bitmap bitmap, SimpleCallback<YtNewperson> simpleCallback) {
        super(mHandler, simpleCallback);
        this.personId = personId;
        this.bitmap = bitmap;
    }

    @Override
    protected YtNewperson initData() throws KeyManagementException, NoSuchAlgorithmException, JSONException, IOException {

        List<String> group_ids = new ArrayList<>();
        group_ids.add(YoutuManager.getGroupId());
        JSONObject jsonObject = YoutuManager.getInstence().newPerson(bitmap, personId, group_ids);

        YtNewperson ytNewperson = GsonUtil.GsonToBean(jsonObject.toString(), YtNewperson.class);

        return ytNewperson;
    }
}
