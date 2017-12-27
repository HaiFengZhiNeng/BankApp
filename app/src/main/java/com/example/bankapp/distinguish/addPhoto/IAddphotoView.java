package com.example.bankapp.distinguish.addPhoto;

import android.graphics.Bitmap;
import android.hardware.Camera;

import com.example.bankapp.base.view.UiView;
import com.example.bankapp.distinguish.faceDistinguish.face.person.face.YtDetectFace;

import java.util.List;

/**
 * Created by dell on 2017/12/13.
 */

public interface IAddphotoView extends UiView {

    void newPerson(Bitmap bitmap);

    void saveCount(int count, String path);

    void addFace(List<String> paths);

    void saveFinish();

    void saveFirstFail();

    void newpersonFail(int code, String msg);

    void newpersonSuccess(String personId);

    void uploadBitmapFinish(int c);

    void uploadBitmapFail(int code, String msg);

    void distinguishFaceSuccess(YtDetectFace ytDetectFace);

    void distinguishFail(int code, String msg);

    void distinguishError();

    void distinguishEnd();

    void previewFinish();

    void pictureTakenSuccess(String savePath);

    void pictureTakenFail();

    void noFace();

    void tranBitmap(Bitmap bitmap, int num);

    void setCameraFaces(Camera.Face[] faces);
}
