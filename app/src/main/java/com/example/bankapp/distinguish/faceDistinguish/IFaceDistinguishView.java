package com.example.bankapp.distinguish.faceDistinguish;

import android.graphics.Bitmap;
import android.hardware.Camera;

import com.example.bankapp.base.view.UiView;
import com.example.bankapp.distinguish.faceDistinguish.face.person.face.YtDetectFace;
import com.example.bankapp.distinguish.faceDistinguish.face.person.face.YtFaceIdentify;

/**
 * Created by dell on 2017/12/14.
 */

public interface IFaceDistinguishView extends UiView {
    void previewFinish();

    void noFace();

    void tranBitmap(Bitmap bitmap, int num);

    void pictureTakenSuccess(String savePath);

    void pictureTakenFail();

    void setCameraFaces(Camera.Face[] faces);

    void distinguishFaceSuccess(YtDetectFace ytDetectFace);

    void distinguishFail(int code, String msg);

    void identifyFace(String name);

    void confidenceLow();

    void preferencesNoPerson(String person);

    void identifyNoFace();

    void verificationSuccess(YtFaceIdentify ytFaceIdentify);

    void verificationFail(int code, String msg);
}
