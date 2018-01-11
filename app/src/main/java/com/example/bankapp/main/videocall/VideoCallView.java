package com.example.bankapp.main.videocall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bankapp.R;
import com.example.bankapp.asr.MySpeech;
import com.example.bankapp.base.view.PresenterActivity;
import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.radio.Radio;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.ecsdk.voip.video.ECCaptureView;
import com.yuntongxun.ecsdk.voip.video.ECOpenGlView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 视频呼叫页面
 *
 * @author Guanluocang
 *         created at 2018/1/6 15:42
 */
public class VideoCallView extends PresenterActivity<VideoCallPresenter> implements IVideoCallView {
    /**
     * 当前呼叫类型对应的布局
     */
    @BindView(R.id.callRoot_fm)
    RelativeLayout callRootFm;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.remote_video_view)
    ECOpenGlView remoteVideoView;
    @BindView(R.id.local_video_view)
    ECOpenGlView localVideoView;
    @BindView(R.id.tv_goBack)
    TextView tvGoBack;
    @BindView(R.id.glView_frameLayout)
    FrameLayout glViewFrameLayout;

    private ECCaptureView mCaptureView;


    @Override
    public VideoCallPresenter createPresenter() {
        return new VideoCallPresenter(this);
    }

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_video_call_view;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        initView();
    }

    private void initView() {
        remoteVideoView.setGlType(ECOpenGlView.RenderType.RENDER_REMOTE);
        remoteVideoView.setAspectMode(ECOpenGlView.AspectMode.CROP);

        localVideoView.setGlType(ECOpenGlView.RenderType.RENDER_PREVIEW);
        localVideoView.setAspectMode(ECOpenGlView.AspectMode.CROP);

        mPresenter.attachGlView(localVideoView, remoteVideoView);
        mCaptureView = new ECCaptureView(this);

        setCaptureView(mCaptureView);
    }

    @Override
    public void onResumeVoice() {
        mPresenter.setMySpeech(MySpeech.SPEECH_NULL);
        mPresenter.stopRecognizerListener();
        mPresenter.stopTts();
    }

    @Override
    public void onPauseReceiver() {

    }

    /**
     * 添加预览到视频通话界面上
     *
     * @param captureView 预览界面
     */
    private void addCaptureView(ECCaptureView captureView) {
        if (callRootFm != null && captureView != null) {
            callRootFm.removeView(mCaptureView);
            mCaptureView = null;
            mCaptureView = captureView;
            callRootFm.addView(captureView, new RelativeLayout.LayoutParams(1, 1));
            mCaptureView.setVisibility(View.VISIBLE);
        }
    }


    public void setCaptureView(ECCaptureView captureView) {
        ECVoIPSetupManager setUpMgr = ECDevice.getECVoIPSetupManager();
        if (setUpMgr != null) {
            setUpMgr.setCaptureView(captureView);
        }
        addCaptureView(captureView);
    }

    @Override
    public void setTopText(String text) {
        Message message = new Message();
        message.what = 200;
        message.obj = text;
        mHandler.sendMessage(message);
    }

    @Override
    public void setRemoteVisiable(boolean visiable) {
        remoteVideoView.setVisibility(visiable ? View.VISIBLE : View.GONE);
    }

    @Override
    public void exit() {
        mPresenter.ReleaseCall();
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    tvTip.setText(msg.obj + "");
                    break;
            }
        }
    };

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
    public void special(String result, SpecialType type) {

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


    @OnClick(R.id.tv_goBack)
    public void onViewClicked() {
        mPresenter.ReleaseCall();
    }
}
