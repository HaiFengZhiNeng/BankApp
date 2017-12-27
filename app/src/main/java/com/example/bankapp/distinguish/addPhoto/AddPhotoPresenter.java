package com.example.bankapp.distinguish.addPhoto;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.bankapp.base.presenter.BasePresenter;
import com.example.bankapp.common.Constants;
import com.example.bankapp.distinguish.faceDistinguish.face.person.YtAddperson;
import com.example.bankapp.distinguish.faceDistinguish.face.person.YtNewperson;
import com.example.bankapp.distinguish.faceDistinguish.face.person.face.YtDetectFace;
import com.example.bankapp.listener.OnConfimListener;
import com.example.bankapp.util.BitmapUtils;
import com.example.bankapp.util.CameraUtils;
import com.example.bankapp.util.PreferencesUtils;
import com.example.bankapp.view.AddInfoDialog;
import com.example.bankapp.youtu.PersonManager;
import com.example.bankapp.youtu.callback.SimpleCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/12/13.
 */

public class AddPhotoPresenter extends BasePresenter<IAddphotoView> implements OnConfimListener, Camera.PreviewCallback, Camera.PictureCallback,
        Camera.ShutterCallback, Camera.FaceDetectionListener {


    private String mAuthId;
    private String mCurrentTimeStr;

    private AddInfoDialog addInfoDialog;

    private int curCount = 0;
    private List<String> mPaths;
    private long curTime;
    private boolean isnewPerson;
    private boolean isFirstVerifica;
    private boolean isDetecting;

    private int cutRatio;

    //Camera
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

    public AddPhotoPresenter(IAddphotoView mView) {
        super(mView);
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        curTime = System.currentTimeMillis();
        mPaths = new ArrayList<>();
        isFirstVerifica = true;
        showDialog();
        cutRatio = 4;
    }

    void initHolder(SurfaceHolder surfaceHolder) {
        mHolder = surfaceHolder;
    }

    void saveFace(final Bitmap bitmap) {
        if (mAuthId == null || mAuthId.equals("")) {
            return;
        }
        if (System.currentTimeMillis() - curTime > 2000) {

            if (curCount == 0) {
                if (!isnewPerson) {
                    boolean save = BitmapUtils.saveBitmapToFile(bitmap, mAuthId, curCount + ".jpg");
                    if (save) {
                        mView.newPerson(bitmap);
                    } else {
                        mView.saveFirstFail();
                    }
                }
            }
            if (!isFirstVerifica) {
                if (curCount < 10) {
                    boolean save = BitmapUtils.saveBitmapToFile(bitmap, mAuthId, curCount + ".jpg");
                    if (save) {
                        mPaths.add(Constants.PROJECT_PATH + mAuthId + File.separator + curCount + ".jpg");
                        mView.saveCount(curCount, "");
                        curCount++;
                    } else {
                        mView.saveFirstFail();
                    }
                } else if (curCount == 10) {
                    if (mPaths != null && mPaths.size() > 0) {
                        mView.addFace(mPaths);
                    }
                    curCount++;
                    mView.saveFinish();
                }
            }
            curTime = System.currentTimeMillis();

        }
    }

    //添加信息dialog
    void showDialog() {
        if (addInfoDialog == null) {
            addInfoDialog = new AddInfoDialog(mView.getContext(), this);
        }
        addInfoDialog.show();
    }

    //确认添加
    @Override
    public void onConfim(String content) {
        if (content.contains(" ")) {
            return;
        }
        mAuthId = content;
        curCount = 0;
        mPaths.clear();
        isFirstVerifica = true;
    }

    void foundPerson(Handler handler, final Bitmap bitmap) {
//        BitmapUtils.saveBitmapToFile(bitmap, "111", "bitmap.jpg");
        Bitmap replicaBitmap = Bitmap.createBitmap(bitmap);
        Bitmap copyBitmap = BitmapUtils.ImageCrop(replicaBitmap, cutRatio, cutRatio, true);
//        Bitmap copyBitmap = bitmapSaturation(bitmap);
//        BitmapUtils.saveBitmapToFile(copyBitmap, "111", "copyBitmap.jpg");
        final String currentTimeStr = String.valueOf(System.currentTimeMillis());
        PersonManager.newperson(handler, currentTimeStr, copyBitmap, new SimpleCallback<YtNewperson>((Activity) mView.getContext()) {
            @Override
            public void onBefore() {
                super.onBefore();
                isnewPerson = true;
            }

            @Override
            public void onSuccess(YtNewperson ytNewperson) {
                PreferencesUtils.putString(mView.getContext(), currentTimeStr, mAuthId);

                mCurrentTimeStr = ytNewperson.getPerson_id();
                isFirstVerifica = false;
                curCount++;
                mView.newpersonSuccess(mCurrentTimeStr);
            }

            @Override
            public void onFail(int code, String msg) {
                mView.newpersonFail(code, msg);
                isnewPerson = false;
            }

            @Override
            public void onEnd() {
                super.onEnd();
                isnewPerson = false;
            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
                mView.newpersonFail(-1, "foundPerson错误");
                isnewPerson = false;
            }
        });
    }

    void uploadFaceBitmap(Handler handler, List<String> paths) {

        PersonManager.addFacefoPath(handler, mCurrentTimeStr, paths, new SimpleCallback<YtAddperson>((Activity) mView.getContext()) {
            @Override
            public void onSuccess(YtAddperson ytAddperson) {
                List<Integer> integerList = ytAddperson.getRet_codes();
                int c = 0;
                for (int i = 0; i < integerList.size(); i++) {
                    if (integerList.get(i) == 0) {

                        c++;
                    }
                }
                mView.uploadBitmapFinish(c);
            }

            @Override
            public void onFail(int code, String msg) {
                mView.uploadBitmapFail(code, msg);
            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
                mView.uploadBitmapFail(-1, "错误addFacefoPath");
            }
        });
    }

    void distinguishFace(Handler handler, Bitmap bitmap) {
        if (!isDetecting) {
            Bitmap replicaBitmap = Bitmap.createBitmap(bitmap);
            Bitmap copyBitmap = BitmapUtils.ImageCrop(replicaBitmap, cutRatio, cutRatio, true);
//        BitmapUtils.saveBitmapToFile(copyBitmap, "copyBitmap", "copyBitmap"+ ".jpg");
            PersonManager.detectFace(handler, copyBitmap, 0, new SimpleCallback<YtDetectFace>((Activity) mView.getContext()) {
                @Override
                public void onBefore() {
                    isDetecting = true;
                }

                @Override
                public void onSuccess(YtDetectFace ytDetectFace) {
                    mView.distinguishFaceSuccess(ytDetectFace);

                }

                @Override
                public void onFail(int code, String msg) {
                    mView.distinguishFail(code, msg);
                }

                @Override
                public void onError(Exception e) {
                    super.onError(e);
                    mView.distinguishError();
                }

                @Override
                public void onEnd() {
                    mView.distinguishEnd();
                }
            });
        }
    }

    void setDetecting(boolean isDetecting) {
        this.isDetecting = isDetecting;
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
