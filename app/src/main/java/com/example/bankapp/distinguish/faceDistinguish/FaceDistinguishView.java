package com.example.bankapp.distinguish.faceDistinguish;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bankapp.R;
import com.example.bankapp.asr.MySpeech;
import com.example.bankapp.base.view.PresenterActivity;
import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.distinguish.addPhoto.AddPhotoView;
import com.example.bankapp.distinguish.faceDistinguish.face.person.face.Face;
import com.example.bankapp.distinguish.faceDistinguish.face.person.face.YtDetectFace;
import com.example.bankapp.distinguish.faceDistinguish.face.person.face.YtFaceIdentify;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.radio.Radio;
import com.example.bankapp.view.takephoto.DetectOpenFaceView;
import com.example.bankapp.view.takephoto.DrawSurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.facedetect.DetectionBasedTracker;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 人脸识别
 *
 * @author Guanluocang
 *         created at 2017/12/14 9:45
 */
public class FaceDistinguishView extends PresenterActivity<FaceDistinguishPresenter> implements IFaceDistinguishView, SurfaceHolder.Callback {


    @BindView(R.id.opengl_surfaceview)
    SurfaceView openglSurfaceview;
    @BindView(R.id.draw_sufaceView)
    DrawSurfaceView drawSufaceView;
    @BindView(R.id.opencv_face_view)
    DetectOpenFaceView opencvFaceView;
    @BindView(R.id.camera_layout)
    RelativeLayout cameraLayout;
    @BindView(R.id.tv_addPhoto)
    TextView tvAddPhoto;
    @BindView(R.id.tv_goBack)
    TextView tvGoBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;


    //opencv
    private Mat mRgba;
    private Mat mGray;

    private File mCascadeFile;
    private CascadeClassifier mJavaDetector;
    private DetectionBasedTracker mNativeDetector;

    private int mAbsoluteFaceSize = 0;
    private float mRelativeFaceSize = 0.2f;
    private int mDetectorType = JAVA_DETECTOR;
    public static final int JAVA_DETECTOR = 0;
    public static final int NATIVE_DETECTOR = 1;

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_face_distinguish_view;
    }

    @Override
    public FaceDistinguishPresenter createPresenter() {
        return new FaceDistinguishPresenter(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void special(String result, SpecialType type) {

    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        initView();
    }

    // 判断语音输入类型
    @Override
    public void onResumeVoice() {
        /**
         * UDP
         * */
//        mUdpAcceptReceiver = new UDPAcceptReceiver(this);
//        IntentFilter intentFilter = new IntentFilter(Constants.UDP_ACCEPT_ACTION);
//        mLbmManager.registerReceiver(mUdpAcceptReceiver, intentFilter);
        mPresenter.setMySpeech(MySpeech.SPEECH_NULL);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(mPresenter.ACTION_MAIN_SPECIATYPE);
        intentFilter.addAction(mPresenter.ACTION_MAIN_SHOWTEXT);
        intentFilter.addAction(mPresenter.ACTION_AIUI_EXIT);
        registerReceiver(handleReceiver, intentFilter);
    }

    @Override
    public void onPauseReceiver() {
        mPresenter.closeCamera();
        unregisterReceiver(handleReceiver);
    }

    public final static int CAMERA_FLAG = 1;

    private Handler mCameraHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CAMERA_FLAG:
                    showToast("识别失败");
                    break;
            }
        }
    };
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    System.loadLibrary("detection_based_tracker");

                    try {
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (mJavaDetector.empty()) {
                            mJavaDetector = null;
                        } else

                            mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);

                        cascadeDir.delete();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    static {
        if (!OpenCVLoader.initDebug()) {
            System.out.println("opencv 初始化失败！");
        } else {
            System.loadLibrary("detection_based_tracker");
        }
    }

    private void initView() {

        tvTitle.setText(R.string.string_face_distinguish);
        SurfaceHolder holder = openglSurfaceview.getHolder(); // 获得SurfaceHolder对象
        holder.addCallback(this); // 为SurfaceView添加状态监听
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mRgba = new Mat();
        mGray = new Mat();
        mPresenter.initHolder(holder);

        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mPresenter.openCamera();
        mPresenter.doStartPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        mPresenter.setMatrix(i1, i2);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        mPresenter.closeCamera();
    }

    @Override
    public void previewFinish() {

    }

    @Override
    public void noFace() {
        drawSufaceView.clear();
        opencvFaceView.clear();
    }

    @Override
    public void tranBitmap(Bitmap bitmap, int num) {
        Utils.bitmapToMat(bitmap, mRgba);
        Mat mat1 = new Mat();
        Utils.bitmapToMat(bitmap, mat1);
        Imgproc.cvtColor(mat1, mGray, Imgproc.COLOR_BGR2GRAY);
        if (mAbsoluteFaceSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
            mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
        }
        MatOfRect faces = new MatOfRect();
        if (mDetectorType == JAVA_DETECTOR) {
            if (mJavaDetector != null)

                mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        } else if (mDetectorType == NATIVE_DETECTOR) {
            if (mNativeDetector != null)
                mNativeDetector.detect(mGray, faces);
        }
        Rect[] facesArray = faces.toArray();
        if (facesArray.length > 0) {
            opencvFaceView.setFaces(facesArray, mPresenter.getOrientionOfCamera());
        }

        mPresenter.faceIdentifyFace(mHandler, bitmap);
        mPresenter.distinguishFace(mHandler, bitmap);
    }

    @Override
    public void pictureTakenSuccess(String savePath) {

    }

    @Override
    public void pictureTakenFail() {

    }

    @Override
    public void setCameraFaces(Camera.Face[] faces) {

    }

    @Override
    public void distinguishFaceSuccess(YtDetectFace ytDetectFace) {
        boolean frontCamera = mPresenter.getCameraId() == Camera.CameraInfo.CAMERA_FACING_FRONT ? true : false;
        drawSufaceView.setYtDetectFace(ytDetectFace, frontCamera);
        List<Face> faces = ytDetectFace.getFace();
        for (int i = 0; i < faces.size(); i++) {
            Face face = faces.get(i);
            String testString = "性别：" + face.getGender() + "; 年龄：" + face.getAge() + "; 微笑：" + face.getExpression();
            showToast(testString);
        }
    }

    @Override
    public void distinguishFail(int code, String msg) {
        if (code == -1101) {
            showToast("正对摄像头，保证画面清晰");
        } else {
            showToast(msg);
        }
    }

    @Override
    public void identifyFace(String name) {
        String msg = "没有检测到，您未注册";
        showToast(msg);
    }

    @Override
    public void confidenceLow() {

    }

    @Override
    public void preferencesNoPerson(String person) {
        String msg = "检测到您是：" + person;
        showToast(msg);
//        mCameraHandler.sendEmptyMessageDelayed(CAMERA_FLAG, 1000);
    }

    @Override
    public void identifyNoFace() {

        showToast("没有检测到，您未注册");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void verificationSuccess(YtFaceIdentify ytFaceIdentify) {

        mPresenter.compareFace(ytFaceIdentify);
    }

    @Override
    public void verificationFail(int code, String msg) {
        if (code == -1101) {
            showToast("正对摄像头，保证画面清晰");
        } else {
            showToast(msg);
        }
    }

    @OnClick({R.id.tv_goBack, R.id.tv_addPhoto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_goBack:
                finish();
                break;
            case R.id.tv_addPhoto:
                startActivity(AddPhotoView.class);
                break;
        }
    }

    private BroadcastReceiver handleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mPresenter.ACTION_AIUI_EXIT)) {
                onExit();
            }
        }
    };

    /**
     * 复写的方法
     */
    @Override
    protected void startMotion() {

    }

    @Override
    protected void stopMotion() {

    }

    @Override
    protected void onEventLR() {

    }

    @Override
    public void spakeMove(SpecialType specialType, String result) {

    }

    @Override
    public void doAiuiAnwer(String anwer) {

    }

    @Override
    public void refHomePage(String question, String finalText) {

    }

    @Override
    public void refHomePage(String question, News news) {

    }

    @Override
    public void refHomePage(String question, Radio radio) {

    }

    @Override
    public void refHomePage(String question, Poetry poetry) {

    }

    @Override
    public void refHomePage(String question, Cookbook cookbook) {

    }

    @Override
    public void refHomePage(String question, EnglishEveryday englishEveryday) {

    }

    @Override
    public void doCallPhone(String phoneNumber) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_title)
    public void onViewClicked() {
    }
}
