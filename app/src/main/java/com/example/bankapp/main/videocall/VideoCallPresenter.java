package com.example.bankapp.main.videocall;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.bankapp.base.config.Constant;
import com.example.bankapp.base.presenter.BasePresenter;
import com.example.bankapp.util.CallFailReason;
import com.example.bankapp.util.L;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.ecsdk.VideoRatio;
import com.yuntongxun.ecsdk.VoipMediaChangedInfo;
import com.yuntongxun.ecsdk.voip.video.ECOpenGlView;

/**
 * Created by dell on 2018/1/6.
 */

public class VideoCallPresenter extends BasePresenter<IVideoCallView> {
    //失败原因
    private int faild_reason = 0;
    private String mNumber = Constant.OTHERS_NAME;

    private Handler mHandler;

    public VideoCallPresenter(IVideoCallView mView) {
        super(mView);
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        initHandler();
        mHandler = getHandler();
        initListener();
        initData();
    }


    public void initData() {
        L.e("GG", mIncomingCall + "");
        if (mIncomingCall) {
            // 来电
            //获取当前的callid
            mCallId = ((Activity) mView).getIntent().getStringExtra(ECDevice.CALLID);
            mCallNumber = ((Activity) mView).getIntent().getStringExtra(ECDevice.CALLER);
            mView.setTopText(mCallNumber + "向您发起视频");
            ECDevice.getECVoIPCallManager().acceptCall(mCallId);
        }

    }

    void ReleaseCall() {
        ECDevice.setAudioMode(1);
        mView.setTopText("通话结束");
        ECDevice.getECVoIPCallManager().releaseCall(mCallId);
        exit();
    }

    /**
     * 初始化界面
     * 如果视频呼叫，则在接受呼叫之前，需要先设置视频通话显示的view
     * localView本地显示视频的view
     * view 显示远端视频的surfaceview
     */
    public void attachGlView(ECOpenGlView localView, ECOpenGlView remoteView) {
        ECVoIPSetupManager setupManager = ECDevice.getECVoIPSetupManager();
        if (setupManager == null) {
            return;
        }
        setupManager.setGlDisplayWindow(localView, remoteView);
    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 1000:
                Log.e("GG", "mHandle 1000");
                mView.setTopText("正在呼叫。。。");
                break;
            case 1001:
                Log.e("GG", "mHandle 1001");
                mView.setTopText("等待对方接听");
                break;
            case 1002:
                Log.e("GG", "mHandle 1002");
                mView.setRemoteVisiable(true);
                ECDevice.getECVoIPSetupManager().enableLoudSpeaker(true);
                if (mIncomingCall) {
                    mView.setTopText("正在和" + mCallNumber + "语音通话");
                } else {
                    mView.setTopText("正在和" + mNumber + "语音通话");
                }
                break;
            case 1003:
                Log.e("GG", "mHandle 1003");
                mView.setTopText(CallFailReason.getCallFailReason(faild_reason) + "");
                break;
            case 1004:
                Log.e("GG", "mHandle 1004");
                ReleaseCall();
//                ECDevice.setAudioMode(1);
//                mView.setTopText("通话结束");
//                ECDevice.getECVoIPCallManager().releaseCall(mCallId);
//                mView.exit();
                break;
        }
    }

    private void initListener() {
        final ECVoIPCallManager callInterface = ECDevice.getECVoIPCallManager();
        if (callInterface != null) {
            callInterface.setOnVoIPCallListener(new ECVoIPCallManager.OnVoIPListener() {
                @Override
                public void onVideoRatioChanged(VideoRatio videoRatio) {
                    /**
                     * 远端视频分辨率到达，标识收到视频图像
                     *
                     * @param videoRatio 视频分辨率信息
                     */
                    L.e("key", "onVideoRatioChanged");
                    if (videoRatio == null) {
                        return;
                    }
                    int width = videoRatio.getWidth();
                    int height = videoRatio.getHeight();
                    if (width == 0 || height == 0) {
                        L.e("key", "invalid video width(" + width + ") or height(" + height + ")");
                        return;
                    }
                    if (width > height) {
                        DisplayMetrics dm = new DisplayMetrics();
                        ((Activity) mView).getWindowManager().getDefaultDisplay().getMetrics(dm);
                        int mSurfaceViewWidth = dm.widthPixels;
                        int mSurfaceViewHeight = dm.heightPixels;
                        int w = mSurfaceViewWidth * height / width;
                    }


                }

                @Override
                public void onSwitchCallMediaTypeRequest(String s, ECVoIPCallManager.CallType callType) {
                    L.e("key", "onSwitchCallMediaTypeRequest");
                }

                @Override
                public void onSwitchCallMediaTypeResponse(String s, ECVoIPCallManager.CallType callType) {
                    L.e("key", "onSwitchCallMediaTypeResponse");
                }

                @Override
                public void onDtmfReceived(String s, char c) {
                    L.e("key", "onDtmfReceived");
                }

                @Override
                public void onCallEvents(ECVoIPCallManager.VoIPCall voipCall) {
                    L.e("GG", "onCallEvents");
                    // 处理呼叫事件回调
                    if (voipCall == null) {
                        L.e("GG", "handle call event error , voipCall null");
                        return;
                    }
                    // 根据不同的事件通知类型来处理不同的业务
                    ECVoIPCallManager.ECCallState callState = voipCall.callState;
                    switch (callState) {
                        case ECCALL_PROCEEDING:
                            Log.e("GG", "正在连接服务器处理呼叫请求，callid：" + voipCall.callId);
                            ECDevice.getECVoIPSetupManager().enableLoudSpeaker(true);
                            mHandler.sendEmptyMessage(1000);
                            break;
                        case ECCALL_ALERTING:
                            Log.e("GG", "呼叫到达对方，正在振铃，callid：" + voipCall.callId);
                            mHandler.sendEmptyMessageDelayed(1001, 200);
                            break;
                        case ECCALL_ANSWERED:
                            mHandler.sendEmptyMessageDelayed(1002, 200);
                            Log.e("GG", "对方接听本次呼叫,callid：" + voipCall.callId);
                            break;
                        case ECCALL_FAILED:
                            // 本次呼叫失败，根据失败原因进行业务处理或跳转
                            Log.e("key", "called:" + voipCall.callId + ",reason:" + voipCall.reason);
                            faild_reason = voipCall.reason;
                            mHandler.sendEmptyMessageDelayed(1003, 200);
                            break;
                        case ECCALL_RELEASED:
                            Log.e("GG", "通话释放[完成一次呼叫]");
                            mHandler.sendEmptyMessageDelayed(1004, 200);
                            // 通话释放[完成一次呼叫]
                            break;
                        default:
                            Log.e("key", "handle call event error , callState " + callState);
                            break;
                    }
                }

                @Override
                public void onMediaDestinationChanged(VoipMediaChangedInfo voipMediaChangedInfo) {

                }
            });
        }
    }

    @Override
    public void onLocalError() {

    }

    @Override
    public void onInsufficient() {

    }
}
