package com.example.bankapp.distinguish.faceDistinguish;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.ArrayMap;
import android.view.SurfaceHolder;

import com.example.bankapp.base.presenter.BasePresenter;
import com.example.bankapp.common.Constants;
import com.example.bankapp.distinguish.faceDistinguish.face.person.face.IdentifyItem;
import com.example.bankapp.distinguish.faceDistinguish.face.person.face.YtDetectFace;
import com.example.bankapp.distinguish.faceDistinguish.face.person.face.YtFaceIdentify;
import com.example.bankapp.util.BitmapUtils;
import com.example.bankapp.util.CameraUtils;
import com.example.bankapp.util.PreferencesUtils;
import com.example.bankapp.youtu.PersonManager;
import com.example.bankapp.youtu.callback.SimpleCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/12/14.
 */

public class FaceDistinguishPresenter extends BasePresenter<IFaceDistinguishView> implements Camera.PreviewCallback, Camera.PictureCallback,
        Camera.ShutterCallback, Camera.FaceDetectionListener {

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private int previewWidth = 640;
    private int previewHeight = 480;
    private boolean isPreviewing = false;
    private Matrix mScaleMatrix = new Matrix();
    private int orientionOfCamera;
    private int orientionOfPhoto;
    private int orientionOfFace;
    private boolean isFirstCamera;
    private boolean isFaceFirst;

    public static final String PICTURETAKEN = "pictureTaken";

    private long curTime;
    private long detectTime;

    private boolean isIdentify;
    private boolean isDetecting;
    public FaceDistinguishPresenter(IFaceDistinguishView mView) {
        super(mView);
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        curTime = System.currentTimeMillis();
        detectTime = System.currentTimeMillis();
        isIdentify = false;
        isDetecting = false;
    }

    //初始化holder
    void initHolder(SurfaceHolder surfaceHolder) {
        mHolder = surfaceHolder;
    }

    public void faceIdentifyFace(Handler handler, Bitmap baseBitmap) {
        if(!isIdentify) {
            if (System.currentTimeMillis() - curTime < 1000) {
                return;
            }
            Bitmap copyBitmap = bitmapSaturation(baseBitmap);
//            BitmapUtils.saveBitmapToFile(copyBitmap, "111", "copyBitmap.jpg");
            PersonManager.faceIdentify(handler, copyBitmap, new SimpleCallback<YtFaceIdentify>((Activity) mView.getContext()) {
                @Override
                public void onBefore() {
                    isIdentify = true;
                }
                @Override
                public void onSuccess(YtFaceIdentify ytFaceIdentify) {
                    mView.verificationSuccess(ytFaceIdentify);
                    isIdentify = false;
                }
                @Override
                public void onFail(int code, String msg) {
                    mView.verificationFail(code, msg);
                    isIdentify = false;
                }
                @Override
                public void onEnd() {
                    curTime = System.currentTimeMillis();
                    isIdentify = false;
                }
                @Override
                public void onError(Exception e) {
                    isIdentify = false;
                }
            });
        }
//        PersonManager.faceVerify(handler, mAuthId, copyBitmap, new SimpleCallback<YtVerifyperson>((Activity) mFaceverifView.getContext()) {
//            @Override
//            public void onBefore() {
//                isVerifying = true;
//            }
//
//            @Override
//            public void onSuccess(YtVerifyperson ytVerifyperson) {
//                mFaceverifView.verificationSuccerr(ytVerifyperson);
//                mFaceverifView.setVerifyingFalse();
//            }
//
//            @Override
//            public void onFail(int code, String msg) {
//                mFaceverifView.verificationFail(code, msg);
//                mFaceverifView.setVerifyingFalse();
//            }
//
//            @Override
//            public void onError(Exception e) {
//                super.onError(e);
//                mFaceverifView.setVerifyingFalse();
//            }
//
//            @Override
//            public void onEnd() {
//                mFaceverifView.setVerifyingFalse();
//            }
//        });
    }

    private Bitmap bitmapSaturation(Bitmap baseBitmap) {
        Bitmap copyBitmap = Bitmap.createBitmap(baseBitmap.getWidth(), baseBitmap.getHeight(), baseBitmap.getConfig());
        ColorMatrix mImageViewMatrix = new ColorMatrix();
        ColorMatrix mBaoheMatrix = new ColorMatrix();
        float sat = (float) 0.0;
        mBaoheMatrix.setSaturation(sat);
        mImageViewMatrix.postConcat(mBaoheMatrix);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(mImageViewMatrix);//再把该mImageViewMatrix作为参数传入来实例化ColorMatrixColorFilter
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);//并把该过滤器设置给画笔
        Canvas canvas = new Canvas(copyBitmap);//将画纸固定在画布上
        canvas.drawBitmap(baseBitmap, new Matrix(), paint);//传如baseBitmap表示按照原图样式开始绘制，将得到是复制后的图片
        canvas.drawBitmap(baseBitmap, new Matrix(), paint);//传如baseBitmap表示按照原图样式开始绘制，将得到是复制后的图片
        return copyBitmap;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void compareFace(YtFaceIdentify ytFaceIdentify) {
        ArrayMap<IdentifyItem, Integer> countMap = new ArrayMap<IdentifyItem, Integer>();

        ArrayList<IdentifyItem> identifyItems = ytFaceIdentify.getCandidates();
        if (identifyItems != null && identifyItems.size() > 0) {
            for (int i = 0; i < identifyItems.size(); i++) {
                IdentifyItem identifyItem = identifyItems.get(i);

                if (countMap.containsKey(identifyItem)) {
                    countMap.put(identifyItem, countMap.get(identifyItem) + 1);
                } else {
                    countMap.put(identifyItem, 1);
                }
            }

            ArrayMap<Integer, List<IdentifyItem>> resultMap = new ArrayMap<Integer, List<IdentifyItem>>();
            List<Integer> tempList = new ArrayList<Integer>();

            Iterator iterator = countMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<IdentifyItem, Integer> entry = (Map.Entry<IdentifyItem, Integer>) iterator.next();

                IdentifyItem key = entry.getKey();
                int value = entry.getValue();

                if (resultMap.containsKey(value)) {
                    List list = (List) resultMap.get(value);
                    list.add(key);
                } else {
                    List<IdentifyItem> list = new ArrayList<IdentifyItem>();
                    list.add(key);
                    resultMap.put(value, list);
                    tempList.add(value);
                }
            }

            Collections.sort(tempList);

            int size = tempList.size();
            List<IdentifyItem> list = resultMap.get(tempList.get(size - 1));
            IdentifyItem identifyItem = list.get(0);

            if(identifyItem.getConfidence() >= 70) {
                String person = identifyItem.getPerson_id();
                String name = PreferencesUtils.getString(mView.getContext(), person);
                if (name != null) {

                    mView.identifyFace(name);
                } else {
                    mView.preferencesNoPerson(person);
                }
            }else{
                mView.confidenceLow();
            }
        } else {
            mView.identifyNoFace();
            isIdentify = false;
        }
    }

    public void distinguishFace(Handler handler, Bitmap bitmap) {
        if(!isDetecting) {
            if (System.currentTimeMillis() - detectTime < 2000) {
                return;
            }
            Bitmap copyBitmap = BitmapUtils.ImageCrop(bitmap, 4, 4, true);
            PersonManager.detectFace(handler, copyBitmap, 0, new SimpleCallback<YtDetectFace>((Activity) mView.getContext()) {
                @Override
                public void onBefore() {
                    isDetecting = true;
                }

                @Override
                public void onSuccess(YtDetectFace ytDetectFace) {
                    mView.distinguishFaceSuccess(ytDetectFace);
                    isDetecting = false;
                }

                @Override
                public void onFail(int code, String msg) {
                    mView.distinguishFail(code, msg);
                }

                @Override
                public void onError(Exception e) {
                    super.onError(e);
                    isIdentify = false;
                }

                @Override
                public void onEnd() {
                    curTime = System.currentTimeMillis();
                    isIdentify = false;
                }
            });
        }
    }

    /**
     * Camera
     */
    public void closeCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.stopFaceDetection();
            isPreviewing = false;
            mCamera.release();
            mCamera = null;
        }
    }

    public void openCamera() {
        if (null != mCamera) {
            return;
        }
        if (Camera.getNumberOfCameras() == 1) {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        try {
            mCamera = Camera.open(mCameraId);
            mCamera.setFaceDetectionListener(this);
        } catch (Exception e) {
            e.printStackTrace();
            closeCamera();
            return;
        }
    }

    public void doStartPreview() {
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPictureFormat(PixelFormat.JPEG);
                List<Camera.Size> pictures = parameters.getSupportedPictureSizes();
                Camera.Size size = pictures.get(pictures.size() - 1);
//                parameters.setPictureSize(size.width, size.height);
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes.contains("continuous-video")) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }
                parameters.setPreviewFormat(ImageFormat.NV21);
                parameters.setPreviewSize(previewWidth, previewHeight);
                mCamera.setParameters(parameters);

                if (Constants.unusual) {
                    orientionOfCamera = 90;
                } else {
                    // 设置显示的偏转角度，大部分机器是顺时针90度，某些机器需要按情况设置
                    orientionOfCamera = CameraUtils.setCameraDisplayOrientation((Activity) mView.getContext(), mCameraId);
                }

                mCamera.setDisplayOrientation(orientionOfCamera);
                mCamera.startPreview();
                mCamera.setPreviewCallback(this);
//                mCamera.setFaceDetectionListener(this);

                try {
                    mCamera.setPreviewDisplay(mHolder);
                    mCamera.startPreview();//开启预览
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (parameters.getMaxNumDetectedFaces() > 1) {
                    mCamera.startFaceDetection();
                }
                isPreviewing = true;
                mView.previewFinish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setMatrix(int width, int height) {
        mScaleMatrix.setScale(width / (float) previewHeight, height / (float) previewWidth);
    }

    public void cameraTakePicture() {
//        isPreviewing = false;
        if (isPreviewing && (mCamera != null)) {
            mCamera.takePicture(this, null, this);
        }
    }

    public int getCameraId() {
        return mCameraId;
    }

    public int getOrientionOfCamera() {
        if (!isFaceFirst) {
            orientionOfFace = orientionOfCamera;
            isFaceFirst = true;
        }
        return 180 - orientionOfFace;
    }

    public void pictureTakenFinsih() {
        mCamera.startPreview();
        isPreviewing = true;
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        Camera.Size size = camera.getParameters().getPreviewSize();
        YuvImage yuvImage = new YuvImage(bytes, ImageFormat.NV21, size.width, size.height, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, baos);
        byte[] byteArray = baos.toByteArray();

        Bitmap previewBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        int width = previewBitmap.getWidth();
        int height = previewBitmap.getHeight();

        Matrix matrix = new Matrix();

        FaceDetector detector = null;
        Bitmap faceBitmap = null;

        detector = new FaceDetector(previewBitmap.getWidth(), previewBitmap.getHeight(), 10);
        if (Constants.unusual) {
            // no thing
//            orientionOfCamera = 90;
        } else {
            orientionOfCamera = 360 - orientionOfCamera;
        }
        if (orientionOfCamera == 360) {
            orientionOfCamera = 0;
        }
        switch (orientionOfCamera) {
            case 0:
                detector = new FaceDetector(width, height, 10);
                matrix.postRotate(0.0f, width / 2, height / 2);
                faceBitmap = Bitmap.createBitmap(previewBitmap, 0, 0, width, height, matrix, true);
                break;
            case 90:
                detector = new FaceDetector(height, width, 1);
                matrix.postRotate(-270.0f, height / 2, width / 2);
                faceBitmap = Bitmap.createBitmap(previewBitmap, 0, 0, width, height, matrix, true);
                break;
            case 180:
                detector = new FaceDetector(width, height, 1);
                matrix.postRotate(-180.0f, width / 2, height / 2);
                faceBitmap = Bitmap.createBitmap(previewBitmap, 0, 0, width, height, matrix, true);
                break;
            case 270:
                detector = new FaceDetector(height, width, 1);
                matrix.postRotate(-90.0f, height / 2, width / 2);
                faceBitmap = Bitmap.createBitmap(previewBitmap, 0, 0, width, height, matrix, true);
                break;
        }

        Bitmap copyBitmap = faceBitmap.copy(Bitmap.Config.RGB_565, true);

        FaceDetector.Face[] faces = new FaceDetector.Face[10];
        int faceNumber = detector.findFaces(copyBitmap, faces);
//        Log.e("faceNumber", faceNumber+"");
        if (!isFirstCamera) {
            orientionOfPhoto = orientionOfCamera;
            isFirstCamera = true;
        }
        if (faceNumber > 0) {
            if (orientionOfPhoto == orientionOfCamera) {
                mView.tranBitmap(faceBitmap, faceNumber);
            }
        } else {
            mView.noFace();
        }

        copyBitmap.recycle();
        faceBitmap.recycle();
        previewBitmap.recycle();
    }


    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        Bitmap saveBitmap = null;
        if (null != bytes) {
            mCamera.stopPreview();
            isPreviewing = false;

            Bitmap previewBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Matrix matrix = new Matrix();
            matrix.postRotate(0.0f, previewBitmap.getWidth() / 2, previewBitmap.getHeight() / 2);
            if (Constants.unusual) {
                matrix.setRotate(orientionOfCamera);
            } else {
                matrix.setRotate(-orientionOfCamera);
            }
            saveBitmap = Bitmap.createBitmap(previewBitmap, 0, 0, previewBitmap.getWidth(), previewBitmap.getHeight(), matrix, true);

        }
        if (null != saveBitmap) {
            long saveTime = System.currentTimeMillis();
            boolean save = BitmapUtils.saveBitmapToFile(saveBitmap, PICTURETAKEN, saveTime + ".jpg");
            if (save) {
                mView.pictureTakenSuccess(Constants.PROJECT_PATH + PICTURETAKEN + File.separator + saveTime + ".jpg");
            } else {
                mView.pictureTakenFail();
            }

        }
    }

    @Override
    public void onShutter() {

    }

    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        if (!Constants.unusual) {
            mView.setCameraFaces(faces);
        }
    }

    @Override
    public void onLocalError() {

    }

    @Override
    public void onInsufficient() {

    }
}
