package com.example.bankapp.distinguish.addPhoto;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bankapp.R;
import com.example.bankapp.asr.MySpeech;
import com.example.bankapp.base.handler.BaseHandler;
import com.example.bankapp.base.view.PresenterActivity;
import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.distinguish.faceDistinguish.face.person.face.YtDetectFace;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.radio.Radio;
import com.example.bankapp.view.takephoto.DetectOpenFaceView;
import com.example.bankapp.view.takephoto.DetectionFaceView;
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
 * 添加人脸信息
 *
 * @author Guanluocang
 *         created at 2017/12/14 9:41
 */
public class AddPhotoView extends PresenterActivity<AddPhotoPresenter> implements IAddphotoView, SurfaceHolder.Callback, BaseHandler.HandleMessage {

    @BindView(R.id.opengl_layout_surfaceview)
    SurfaceView openglLayoutSurfaceview;
    @BindView(R.id.draw_sufaceView)
    DrawSurfaceView drawSufaceView;
    @BindView(R.id.detection_face_view)
    DetectionFaceView detectionFaceView;
    @BindView(R.id.opencv_face_view)
    DetectOpenFaceView opencvFaceView;
    @BindView(R.id.tv_add_info)
    TextView tvAddInfo;//添加人员信息
    @BindView(R.id.tv_goBack)
    TextView tvGoBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.surface_layout)
    RelativeLayout surfaceLayout;


    private boolean isHasFace;

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

    private Handler mHandler = new BaseHandler<>(AddPhotoView.this);

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_add_photo_view;
    }

    @Override
    public AddPhotoPresenter createPresenter() {
        return new AddPhotoPresenter(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    // 加载opencv
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

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        initView();
        initData();
    }

    //初始化OpenCV
    static {
        if (!OpenCVLoader.initDebug()) {
            System.out.println("opencv 初始化失败！");
        } else {
            System.loadLibrary("detection_based_tracker");
        }
    }

    private void initView() {
        tvTitle.setText(R.string.string_face_add);
        SurfaceHolder holder = openglLayoutSurfaceview.getHolder(); // 获得SurfaceHolder对象
        holder.addCallback(this); // 为SurfaceView添加状态监听
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mRgba = new Mat();
        mGray = new Mat();
        mPresenter.initHolder(holder);
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
    }

    private void initData() {
        mPresenter.showDialog();
    }

    @Override
    public void onResumeVoice() {
        mPresenter.setMySpeech(MySpeech.SPEECH_NULL);
    }

    @Override
    public void onPauseReceiver() {
        mPresenter.closeCamera();

    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                mPresenter.setDetecting(false);
                break;
        }
    }

    @Override
    public void noFace() {
        drawSufaceView.clear();
        opencvFaceView.clear();
    }

    @Override
    public void tranBitmap(Bitmap bitmap, int faceNumber) {
//        if (isHasFace) {
        mPresenter.saveFace(bitmap);
        mPresenter.distinguishFace(mHandler, bitmap);
//        }

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
    }

    @Override
    public void setCameraFaces(Camera.Face[] faces) {
        if (!isHasFace) {
            isHasFace = true;
        }
        detectionFaceView.setFaces(faces, mPresenter.getOrientionOfCamera());
    }

    @Override
    public void saveCount(int count, String path) {
        showToast("保存第" + count + "图片");
    }

    @Override
    public void saveFinish() {
        showToast("保存完成");
        finish();
    }

    @Override
    public void saveFirstFail() {

    }

    @Override
    public void newPerson(Bitmap bitmap) {
        mPresenter.foundPerson(mHandler, bitmap);
    }

    @Override
    public void addFace(List<String> paths) {
        mPresenter.uploadFaceBitmap(mHandler, paths);
    }

    @Override
    public void newpersonSuccess(String personId) {
        showToast("添加人脸成功");
    }

    @Override
    public void newpersonFail(int code, String msg) {
        if (code == -1101) {
            showToast("未拍摄到人脸");
        } else if (code == -1313) {
            showToast("请正对摄像头");
        } else if (code == -1302) {
            showToast("个体已存在");
        } else if (code == -1310) {
            showToast("个体个数超过限制");
        } else {
            showToast(msg);
        }
    }

    @Override
    public void uploadBitmapFinish(int c) {
        showToast("成功添加了" + c + "张人脸");
    }

    @Override
    public void uploadBitmapFail(int code, String msg) {
        showToast(msg);
    }

    @Override
    public void distinguishFaceSuccess(YtDetectFace ytDetectFace) {
        //TODO SS
//        boolean frontCamera = mCameraPresenter.getCameraId() == Camera.CameraInfo.CAMERA_FACING_FRONT ? true : false;
//        drawSufaceView.setYtDetectFace(ytDetectFace, frontCamera);
        mHandler.sendEmptyMessageDelayed(1, 2000);
    }

    @Override
    public void distinguishFail(int code, String msg) {
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    public void distinguishError() {
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    public void distinguishEnd() {
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    public void previewFinish() {

    }

    @Override
    public void pictureTakenSuccess(String savePath) {

    }

    @Override
    public void pictureTakenFail() {

    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mPresenter.openCamera();
        mPresenter.doStartPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mPresenter.setMatrix(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mPresenter.closeCamera();
    }


    @OnClick({R.id.tv_goBack, R.id.tv_add_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_goBack:
                finish();
                break;
            case R.id.tv_add_info:
                mPresenter.showDialog();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void special(String result, SpecialType type) {

    }

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
    public void doDance() {

    }
}
