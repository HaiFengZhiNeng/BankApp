package com.example.bankapp.main;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.bankapp.BankApplication;
import com.example.bankapp.R;
import com.example.bankapp.base.config.Constant;
import com.example.bankapp.base.presenter.BasePresenter;
import com.example.bankapp.common.enums.ComType;
import com.example.bankapp.database.manager.IntroduceManager;
import com.example.bankapp.main.videocall.VideoCallView;
import com.example.bankapp.modle.LocalMoneyService;
import com.example.bankapp.service.UDPAcceptReceiver;
import com.example.bankapp.splash.SingleLogin;
import com.example.bankapp.util.BitmapUtils;
import com.example.bankapp.util.FileUtil;
import com.example.bankapp.util.GsonUtil;
import com.example.bankapp.util.L;
import com.example.bankapp.util.PreferencesUtils;
import com.example.bankapp.util.ReceiveMessage;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.ecsdk.OnChatReceiveListener;
import com.yuntongxun.ecsdk.OnMeetingListener;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.VideoRatio;
import com.yuntongxun.ecsdk.im.ECMessageNotify;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;
import com.yuntongxun.ecsdk.meeting.intercom.ECInterPhoneMeetingMsg;
import com.yuntongxun.ecsdk.meeting.video.ECVideoMeetingMsg;
import com.yuntongxun.ecsdk.meeting.voice.ECVoiceMeetingMsg;

import java.util.List;

import static com.example.bankapp.splash.SingleLogin.isInitSuccess;

/**
 * Created by dell on 2017/12/6.
 */

public class MainPresenter extends BasePresenter<IMainView> {

    private String username = Constant.LOGIN_NAME;
    private String appKey = "8a216da85ea31fdd015ea399cb4b0075";
    private String token = "c3f9bc4ed6f62a1b2888f4dc4d6604ab";
    private SingleLogin mLogin;

    public MainPresenter(IMainView mView) {
        super(mView);

    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        mLogin = SingleLogin.getInstance(getContext(), "");
        mLogin.initInitialized();
        mLogin.setOnInitListener(initListener);
    }

    //    添加本地数据
    void addLocalData(IntroduceManager introduceManager) {
        boolean isSaveCountry = PreferencesUtils.getBoolean(mView.getContext(), "localIntroduce", false);
        if (!isSaveCountry) {
            PreferencesUtils.putBoolean(mView.getContext(), "localIntroduce", true);
            String[] introduceQuestion = resArray(R.array.local_introduce_question);
            String[] introduceAnswer = resArray(R.array.local_introduce_answer);
            for (int i = 0; i < introduceQuestion.length; i++) {
                introduceManager.insert(new LocalMoneyService("本地语音", introduceQuestion[i], introduceAnswer[i], "双臂向左欢迎", "A50C8001AA", "微笑", "A5028001AA"));

            }
        }
        //上传热词
        uploadUserwords();
    }

    //获取资源文件
    private String[] resArray(int resId) {
        return mView.getContext().getResources().getStringArray(resId);
    }

    SingleLogin.OnInitListener initListener = new SingleLogin.OnInitListener() {
        @Override
        public void onSuccess() {
            L.e("key", "loginSuccess");
//            DoLogin();
        }

        @Override
        public void onError(Exception exception) {
            L.e("key", "loginError");
        }

        @Override
        public void onInitFinish() {
            if (isInitSuccess) {
                loginMain();
            } else {
                L.e("GG", "初始化失败");
            }
        }
    };


    public void DoLogin() {
        mLogin.initInitialized();
        mLogin.setOnInitListener(initListener);
    }


    private void loginMain() {
        if (!isInitSuccess)
            return;
        L.e("key", "初始化SDK及登陆代码完成");


        ECInitParams params = ECInitParams.createParams();
        params.setUserid(username);
        params.setAppKey(appKey);
        params.setToken(token);
        //设置登陆验证模式：自定义登录方式
        params.setAuthType(ECInitParams.LoginAuthType.NORMAL_AUTH);
        //DloginMode（强制上线：FORCE_DlogIN  默认登录：AUTO。使用方式详见注意事项）
        params.setMode(ECInitParams.LoginMode.FORCE_LOGIN);


        /**
         * 登录回调
         */
        ECDevice.setOnDeviceConnectListener(onECDeviceConnectListener);

        /**
         * 设置接收VoIP来电事件通知Intent
         * 呼入界面activity、开发者需修改该类
         * */
        Intent intent = new Intent(getContext(), VideoCallView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ECDevice.setPendingIntent(pendingIntent);
        /**
         * 验证参数是否正确
         * */
        if (params.validate()) {
            // 登录函数
            ECDevice.login(params);
        }
        // 设置VOIP 自定义铃声路径
        ECVoIPSetupManager setupManager = ECDevice.getECVoIPSetupManager();
        if (setupManager != null) {
            // 目前支持下面三种路径查找方式
            // 1、如果是assets目录则设置为前缀[assets://]
            setupManager.setInComingRingUrl(true, "assets://phonering.mp3");
            setupManager.setOutGoingRingUrl(true, "assets://phonering.mp3");
            setupManager.setBusyRingTone(true, "assets://played.mp3");
            // 2、如果是raw目录则设置为前缀[raw://]
            // 3、如果是SDCard目录则设置为前缀[file://]
        }
    }

    /**
     * 设置通知回调监听包含登录状态监听，接收消息监听，呼叫事件回调监听和
     * 设置接收来电事件通知Intent等
     */
    private ECDevice.OnECDeviceConnectListener onECDeviceConnectListener = new ECDevice.OnECDeviceConnectListener() {
        public void onConnect() {
            //兼容旧版本的方法，不必处理
        }

        @Override
        public void onDisconnect(ECError error) {
            //兼容旧版本的方法，不必处理
        }

        @Override
        public void onConnectState(ECDevice.ECConnectState state, ECError error) {
            if (state == ECDevice.ECConnectState.CONNECT_FAILED) {
                if (error.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
                    L.e("key", "==帐号异地登陆");
                } else {
                    L.e("key", "==其他登录失败,错误码：" + error.errorMsg + error.errorCode);
                    L.e("GG", "正在重新登录。。。");
//                    DoLogin();
                    mView.initECSuccess(false);
                }
                return;
            } else if (state == ECDevice.ECConnectState.CONNECT_SUCCESS) {
                L.e("key", "==登陆成功" + error.toString());
                mView.initECSuccess(true);
//                startActivity(MainView.class);
            }
        }
    };
    /**
     * IM接收消息监听，使用IM功能的开发者需要设置。
     */
    private OnChatReceiveListener onChatReceiveListener = new OnChatReceiveListener() {
        @Override
        public void OnReceivedMessage(ECMessage ecMessage) {

        }

        @Override
        public void onReceiveMessageNotify(ECMessageNotify ecMessageNotify) {

        }

        @Override
        public void OnReceiveGroupNoticeMessage(ECGroupNoticeMessage ecGroupNoticeMessage) {

        }

        @Override
        public void onOfflineMessageCount(int i) {

        }

        @Override
        public int onGetOfflineMessage() {
            return 0;
        }

        @Override
        public void onReceiveOfflineMessage(List<ECMessage> list) {

        }

        @Override
        public void onReceiveOfflineMessageCompletion() {

        }

        @Override
        public void onServicePersonVersion(int i) {

        }

        @Override
        public void onReceiveDeskMessage(ECMessage ecMessage) {

        }

        @Override
        public void onSoftVersion(String s, int i) {

        }
    };

    /**
     * 音视频会议回调监听，使用音视频会议功能的开发者需要设置。
     */
    public void onMeetingListener() {
        if (ECDevice.getECMeetingManager() != null) {
            ECDevice.getECMeetingManager().setOnMeetingListener(new OnMeetingListener() {
                @Override
                public void onVideoRatioChanged(VideoRatio videoRatio) {
                    L.e("key", "1初始化SDK成功");
                }

                @Override
                public void onReceiveInterPhoneMeetingMsg(ECInterPhoneMeetingMsg msg) {
                    // 处理实时对讲消息Push
                    L.e("key", "2初始化SDK成功");
                }

                @Override
                public void onReceiveVoiceMeetingMsg(ECVoiceMeetingMsg msg) {
                    // 处理语音会议消息push
                    L.e("key", "3初始化SDK成功");
                }

                @Override
                public void onReceiveVideoMeetingMsg(ECVideoMeetingMsg msg) {
                    // 处理视频会议消息Push（暂未提供）
                    L.e("key", "4初始化SDK成功");
                }

                @Override
                public void onMeetingPermission(String s) {
                    L.e("key", "5初始化SDK成功");
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
