package com.example.bankapp.base.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.example.bankapp.BankApplication;
import com.example.bankapp.R;
import com.example.bankapp.base.config.Constant;
import com.example.bankapp.base.handler.BaseHandler;
import com.example.bankapp.base.presenter.BasePresenter;
import com.example.bankapp.common.Constants;
import com.example.bankapp.common.enums.ComType;
import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.common.instance.SpeakTts;
import com.example.bankapp.database.LocalMoneyServiceDao;
import com.example.bankapp.database.manager.IntroduceManager;
import com.example.bankapp.modle.LocalMoneyService;
import com.example.bankapp.serialport.ISerialPortView;
import com.example.bankapp.serialport.SerialPresenter;
import com.example.bankapp.service.UDPAcceptReceiver;
import com.example.bankapp.service.UsbService;
import com.example.bankapp.splash.SingleLogin;
import com.example.bankapp.util.BitmapUtils;
import com.example.bankapp.util.DanceUtils;
import com.example.bankapp.util.FileUtil;
import com.example.bankapp.util.GsonUtil;
import com.example.bankapp.util.L;
import com.example.bankapp.util.MediaPlayerUtil;
import com.example.bankapp.util.PhoneUtil;
import com.example.bankapp.util.PreferencesUtils;
import com.example.bankapp.util.ReceiveMessage;
import com.example.bankapp.util.SendToControll;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import static com.example.bankapp.base.presenter.BasePresenter.isSport;
import static com.example.bankapp.base.presenter.BasePresenter.isTalking;
import static com.example.bankapp.base.presenter.BasePresenter.isVoice;


public abstract class PresenterActivity<T extends BasePresenter> extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener, ISerialPortView.ISerialView, BaseHandler.HandleMessage,
        ReceiveMessage {
    protected T mPresenter;
    private static final String TAG = PresenterActivity.class.getName();
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    // 串口
    public SerialPresenter mSerialPresenter;

    public static final int MSG_MOVE = 108;
    public static final int MSG_JUDGE = 109;
    private boolean suspend;

    private Handler outHandler = new BaseHandler<>(PresenterActivity.this);
    private long OUT_TIME = 500;

    //本地语音数据库
    private IntroduceManager introduceManager;
    public static Handler mBaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPresenter = createPresenter();
        super.onCreate(savedInstanceState);
        getTakePhoto().onCreate(savedInstanceState);
        mPresenter.onCreate(savedInstanceState);
        mSerialPresenter = new SerialPresenter(this);
        Constant.IP = PhoneUtil.getWifiIP(this);
        initDataBase();
        SingleLogin.getInstance(this, "").setReceive(this);
    }

    public abstract T createPresenter();

    @Override
    protected void onViewCreateBefore() {
        mPresenter.onViewCreateBefore();
    }


    @Override
    protected void onViewCreated() {
        mPresenter.onViewCreated();
    }

    //初始化数据库
    public void initDataBase() {
        introduceManager = new IntroduceManager();
        mBaseHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 100:
                        mPresenter.doVoiceSwitch(true);
                        mPresenter.onResume();
                        break;
                }
            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
        onResumeVoice();

//        IntentFilter filter = new IntentFilter();
//        filter.addAction(UsbService.ACTION_USB_RECEIVER);
//        this.registerReceiver(this.usbReceiver, filter);
        //开启语音
//        mBaseHandler.sendEmptyMessageDelayed(100, 1000);
    }

    // 在onresume中判断语音输入类型
    public abstract void onResumeVoice();

    public abstract void onPauseReceiver();

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSerialPresenter.closeComPort();//关闭串口
        //关闭语音等其他操作
        mPresenter.stopTts();
        mPresenter.stopRecognizerListener();
        mPresenter.stopHandler();
        MediaPlayerUtil.getInstance().stopMusic();
        DanceUtils.getInstance().stopDance();
        onPauseReceiver();
        mPresenter.onPause();

    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        mSerialPresenter.closeComPort();
        //关闭语音等其他操作
        mPresenter.stopTts();
        mPresenter.stopRecognizerListener();
        mPresenter.stopHandler();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mPresenter.onBackPressed())
            super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void takeSuccess(TResult result) {
        Log.i(TAG, "takeSuccess：" + result.getImage().getCompressPath());
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.i(TAG, "takeFail:" + msg);
    }

    @Override
    public void takeCancel() {
        Log.i(TAG, getResources().getString(com.jph.takephoto.R.string.msg_operation_canceled));
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    //退出
    public void onExit() {
        finish();
    }

    // 进入页面时需要播放的内容
    protected void addSpeakAnswer(String messageContent) {
        mPresenter.doAnswer(messageContent);
        if (getClass().getSimpleName().equals("MainView")) {//如果是在首页，则根据说话的长度执行预支说话时长相匹配的动作
            if (messageContent.length() <= 13) {
                mSerialPresenter.receiveMotion(ComType.A, "A50C8001AA");
            } else if (13 < messageContent.length() && messageContent.length() <= 40) {
                mSerialPresenter.receiveMotion(ComType.A, "A50C8003AA");
            } else if (40 < messageContent.length()) {
                mSerialPresenter.receiveMotion(ComType.A, "A50C8021AA");
            }
        }
    }

    // 开始运动 结束运动
    protected abstract void startMotion();

    protected abstract void stopMotion();

    protected abstract void onEventLR();

    // 手柄按键 事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP://19
                if (!suspend) {
                    sendMsg(keyCode);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN://20
                if (!suspend) {
                    sendMsg(keyCode);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT://21
                if (!suspend) {
                    sendMsg(keyCode);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT://20
                if (!suspend) {
                    sendMsg(keyCode);
                }
                break;
            case KeyEvent.KEYCODE_BUTTON_L1:
                onEventLR();
                break;
            case KeyEvent.KEYCODE_BUTTON_R1:
                onEventLR();
                break;
            case KeyEvent.KEYCODE_BUTTON_B:
                mSerialPresenter.receiveMotion(ComType.A, "A50C80F3AA");
                //     mSerialPresenter.receiveMotion(ComType.A, "A5038002AA");
                break;
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
            case KeyEvent.KEYCODE_BUTTON_X:
                mSerialPresenter.receiveMotion(ComType.A, "A50C80F2AA");
                //       onKeyDown(KeyEvent.KEYCODE_BACK, event);
                break;
            case KeyEvent.KEYCODE_BUTTON_Y:
                sendAutoAction();
                break;
            case KeyEvent.KEYCODE_BUTTON_A:
                stopAutoAction();
                break;
//            case KeyEvent.KEYCODE_B:
//                mSerialPresenter.receiveMotion(ComType.A, "A50C80F2FF");
//                break;
        }
        return false;
    }

    private void sendMsg(int keyCode) {
        suspend = true;
        Message message = new Message();
        message.what = MSG_MOVE;
        message.obj = keyCode;
        outHandler.sendMessage(message);
    }

    // 自由运动 开启
    public void sendAutoAction() {
        startMotion();
        if (BankApplication.getInstance().isAutoAction()) {
            stopAutoAction();
        } else {
            BankApplication.getInstance().setAutoAction(true);
            mSerialPresenter.receiveMotion(ComType.A, "A503800AAA");
            Log.e("GG", "自由运动(开)");
            mHandler.postDelayed(runnable, 600);
        }
    }

    // 自由运动 开启
    public void stopAutoAction() {
        if (BankApplication.getInstance().isAutoAction()) {
            Log.e("GG", "自由运动(关)");
            mHandler.removeCallbacks(runnable);
            mSerialPresenter.receiveMotion(ComType.A, "A5038005AA");
            BankApplication.getInstance().setAutoAction(false);
        }
        stopMotion();
    }


    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (BankApplication.getInstance().isAutoAction()) {
                mSerialPresenter.receiveMotion(ComType.A, "A503800AAA");
                mHandler.postDelayed(runnable, 600);
                Log.e("GG", "自由运动(开)");
            }
        }
    };

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_MOVE:
                int keyCode = (int) msg.obj;
                move(keyCode);
                outHandler.sendEmptyMessageDelayed(MSG_JUDGE, OUT_TIME);
                break;
            case MSG_JUDGE:
                suspend = false;
                break;
        }
    }

    public void move(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP://19
                mSerialPresenter.receiveMotion(ComType.A, "A5038002AA");
                Log.e("GG", "up");
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN://20
                mSerialPresenter.receiveMotion(ComType.A, "A5038008AA");
                Log.e("GG", "down");
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT://21
                mSerialPresenter.receiveMotion(ComType.A, "A5038004AA");
                Log.e("GG", "left");
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT://20
                mSerialPresenter.receiveMotion(ComType.A, "A5038006AA");
                Log.e("GG", "right");
                break;

        }
    }


    @Override
    public void stopAll() {
        mPresenter.stopAll();
        MediaPlayerUtil.getInstance().stopMusic();
        DanceUtils.getInstance().stopDance();
        mSerialPresenter.receiveMotion(ComType.A, "A50C80E2AA");
        stopAutoAction();
    }

    @Override
    public void onMoveStop() {
        String text = resFoFinal(R.array.wake_up);
        addSpeakAnswer(text);
    }

    @Override
    public void sayHellow() {
        mPresenter.sayHellow();
    }

    public String resFoFinal(int id) {
        String[] arrResult = getResources().getStringArray(id);
        return arrResult[new Random().nextInt(arrResult.length)];
    }

    private BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(UsbService.ACTION_USB_RECEIVER)) {
                sayHellow();
            }
        }
    };

    //控制运动
    public void ReceiveMotion(ComType type, String motion) {
        mSerialPresenter.receiveMotion(type, motion);
    }

    //位移 运动
    @Override
    public void spakeMove(SpecialType specialType, String result) {
        switch (specialType) {
            case Forward:
                mSerialPresenter.receiveMotion(ComType.A, "A5038002AA");
                mPresenter.onCompleted();
                break;
            case Backoff:
                mSerialPresenter.receiveMotion(ComType.A, "A5038008AA");
                mPresenter.onCompleted();
                break;
            case Turnleft:
                mSerialPresenter.receiveMotion(ComType.A, "A5038004AA");
                mPresenter.onCompleted();
                break;
            case Turnright:
                mSerialPresenter.receiveMotion(ComType.A, "A5038006AA");
                mPresenter.onCompleted();
                break;
        }
    }

    @Override
    public void OnReceivedMessage(ECMessage msg) {
        L.e("basePresenter", "收到了" + msg.getBody() + "");
        /**
         *
         * 根据自定义的类型 去做相对应的操作
         * */
        ECTextMessageBody stateBody = (ECTextMessageBody) msg.getBody();
        if (msg.getUserData().equals("Motion")) {
            String motion = stateBody.getMessage();

            ReceiveMotion(ComType.A, motion);
            Log.i("WWDZ", "ReceiveMotion: " + msg.getBody().toString());
        } else if (msg.getUserData().equals("SmartChat")) {//发音人

            String state = stateBody.getMessage();
//            SharedPreferences.Editor editor = mSharedPreferences.edit();
            SpeakTts.getInstance().setSpokesman(state);
            SpeakTts.getInstance().initTts();
//            editor.putString("iat_language_talker", talker);
//            if (talker.equals("henry") || talker.equals("aiscatherine") || talker.equals("aisjohn") || talker.equals("vimary") || talker.equals("aistom")) {//听到英文翻译成中文
//                editor.putString("iat_language_preference", "en_us");
//            } else if (talker.equals("dalong") || talker.equals("xiaomei")) {//粤语
//                editor.putString("iat_language_preference", "cantonese");
//            } else {
//                editor.putString("iat_language_preference", "zh_cn");
//            }
//
//            editor.commit();
//            mPresenter.initIat();
//            mPresenter.initTts();

        } else if (msg.getUserData().equals("text")) {//文本
            ReceiveChat(msg);
        } else if (msg.getUserData().equals("AutoAction")) {//自由运动开关
            //自由运动

            ReceiveChat(msg);
        } else if (msg.getUserData().equals("VoiceSwitch")) {//语音开关
            //自由运动
            String state = stateBody.getMessage();
            if ("语音开".equals(state)) {
                isVoice = true;
                mPresenter.judgeState();
            } else {
                isVoice = false;
            }
        } else if (msg.getUserData().equals("version")) {
            int version = Integer.valueOf(stateBody.getMessage());// 控制端发过来的版本
            int currentVersion = PreferencesUtils.getInt(this, "LocalVoice");// 本地版本
            if (version < currentVersion) {// 控制端小于机器人端
                List<LocalMoneyService> userInfos = introduceManager.loadAll();
                if (userInfos != null && userInfos.size() > 0) {
                    String voiceBean = GsonUtil.GsonString(userInfos);
                    SendToControll sendToControll = new SendToControll();
                    sendToControll.SendControll(currentVersion + "", "version");
                    boolean isSave = FileUtil.saveFile(voiceBean, BitmapUtils.projectPath + "localVoice.txt");
                    if (isSave) {
                        sendToControll.SendLocal(BitmapUtils.projectPath + "localVoice.txt", "localVoiceFile");
                    }
                } else {
                    showToast("本地沒有語音");
                }
            }

        } else if (msg.getUserData().equals("IP")) {
            String ip = stateBody.getMessage();
            Log.e(TAG, "收到IP");
            ReceiveIP(ip);
        }
    }

    //接收消息
    private void ReceiveChat(ECMessage msg) {
        ECTextMessageBody stateBody = (ECTextMessageBody) msg.getBody();
        String state = stateBody.getMessage();
        L.e("GG", state + "");
        if (isTalking) {
            mPresenter.doAnswer(state);
            refHomePage("", state);
        }
    }

    //获取 IP
    public void ReceiveIP(String ip) {
        Log.e(TAG, "ReceiveMotion_ip: " + ip);
        Constant.CONNECT_IP = ip;

        if (Constant.IP != null && Constant.PORT > 0) {
            try {
                JSONObject object = new JSONObject();
                object.put("robotIp", Constant.IP);
                object.put("robotPort", Constant.PORT);
                SendToControll send = new SendToControll();
                send.SendControll(object.toString(), "IP");
//                    Print.e("发送: " + object.toString());
//                    mChatPresenter.sendCustomMessage(robotBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
