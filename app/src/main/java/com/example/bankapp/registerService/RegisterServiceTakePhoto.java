package com.example.bankapp.registerService;

import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.example.bankapp.R;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;

/**
 * Created by dell on 2017/12/13.
 */

public class RegisterServiceTakePhoto {
    private View rootView;

    public static RegisterServiceTakePhoto of(View rootView) {
        return new RegisterServiceTakePhoto(rootView);
    }

    private RegisterServiceTakePhoto(View rootView) {
        this.rootView = rootView;
    }


    public void onClick(View view, TakePhoto takePhoto) {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);

        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
        switch (view.getId()) {
            case R.id.btn_pick_photo:
                //从相册
                takePhoto.onPickFromGallery();
                break;
            //拍照
            case R.id.btn_take_photo:
                takePhoto.onPickFromCapture(imageUri);
                break;
            default:
                break;
        }
    }

    //纠正拍照的照片旋转角度
    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        //纠正拍照的照片旋转角度：
        builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());
    }

    //压缩
    private void configCompress(TakePhoto takePhoto) {
        CompressConfig config;
        config = new CompressConfig.Builder()
                //压缩的大小 B
                .setMaxSize(153600)
                //宽 高
                .setMaxPixel(1000)
                .enableReserveRaw(false)
                .create();

        //显示压缩进度条
        takePhoto.onEnableCompress(config, true);
    }
}
