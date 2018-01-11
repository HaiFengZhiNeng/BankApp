package com.example.bankapp.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bankapp.R;
import com.example.bankapp.adapter.ChatAdapter;
import com.example.bankapp.adapter.MainRollViewAdapter;
import com.example.bankapp.addLocalData.AddDataView;
import com.example.bankapp.animator.SlideInOutBottomItemAnimator;
import com.example.bankapp.asr.MySpeech;
import com.example.bankapp.base.handler.BaseHandler;
import com.example.bankapp.base.presenter.BasePresenter;
import com.example.bankapp.base.view.PresenterActivity;
import com.example.bankapp.business.BusinessView;
import com.example.bankapp.common.Constants;
import com.example.bankapp.common.enums.ComType;
import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.common.instance.SpeakTts;
import com.example.bankapp.database.manager.IntroduceManager;
import com.example.bankapp.distinguish.faceDistinguish.FaceDistinguishView;
import com.example.bankapp.modle.Chat;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.radio.Radio;
import com.example.bankapp.moneyService.MoneyServiceView;
import com.example.bankapp.register.RegisterView;
import com.example.bankapp.service.UDPAcceptReceiver;
import com.example.bankapp.service.UdpService;
import com.example.bankapp.splash.SingleLogin;
import com.example.bankapp.splash.SplashView;
import com.example.bankapp.traffic.TrafficView;
import com.example.bankapp.util.BitmapUtils;
import com.example.bankapp.util.DanceUtils;
import com.example.bankapp.util.FileUtil;
import com.example.bankapp.util.GsonUtil;
import com.example.bankapp.util.L;
import com.example.bankapp.util.PermissionsChecker;
import com.example.bankapp.util.PreferencesUtils;
import com.example.bankapp.util.ReceiveMessage;
import com.example.bankapp.view.ChatLinearLayoutManager;
import com.example.bankapp.view.FindQRCodeDialog;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.bankapp.base.presenter.BasePresenter.isSport;
import static com.example.bankapp.base.presenter.BasePresenter.isTalking;
import static com.example.bankapp.base.presenter.BasePresenter.isVoice;

public class MainView extends PresenterActivity<MainPresenter> implements UDPAcceptReceiver.UDPAcceptInterface, IMainView {

    @BindView(R.id.tv_registerService)
    TextView tvRegisterService;//挂号服务
    @BindView(R.id.tv_illegalrService)
    TextView tvIllegalrService;//违章查询
    @BindView(R.id.tv_costService)
    TextView tvCostService;//e缴费
    @BindView(R.id.tv_businessService)
    TextView tvBusinessService;//业务查询
    @BindView(R.id.tv_identityService)
    TextView tvIdentityService;//身份认证
    @BindView(R.id.tv_moneyService)
    TextView tvMoneyService;//理财产品
    @BindView(R.id.rv_main_service)
    RecyclerView rvMainService;//对话框
    @BindView(R.id.roll_view)
    RollPagerView rollView;    //轮播图
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;

    //二维码
    private FindQRCodeDialog findQRCodeDialog;

    //对话框
    private ChatAdapter mChatAdapter;
    private List<Chat> mChatList = new ArrayList<>();

    //本地语音Manager
    private IntroduceManager introduceManager;
    //udp
    private UDPAcceptReceiver mUdpAcceptReceiver;
    private LocalBroadcastManager mLbmManager;

    //第一次进入Main
    private boolean isFirst = true;

    @Override
    protected int getContentViewResource() {
        return (R.layout.activity_main);
    }


    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter(this);
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

    // 初始化云通讯完成
    @Override
    public void initECSuccess(boolean isSuccess) {
        if (isSuccess) {
//            showToast("控制端连接成功");
        } else {
//            showToast("控制端连接失败");
        }
    }

    //初始化视图
    private void initView() {

        //设置轮播图
        rollView.setPlayDelay(3000);
        rollView.setAnimationDurtion(1000);
        rollView.setAdapter(new MainRollViewAdapter());
        rollView.setHintView(new ColorPointHintView(this, Color.YELLOW, Color.WHITE));

        // 对话recyclever
        mChatAdapter = new ChatAdapter(this, mChatList);
        rvMainService.setLayoutManager(new ChatLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvMainService.setItemAnimator(new SlideInOutBottomItemAnimator(rvMainService));
        rvMainService.setAdapter(mChatAdapter);

        //本地语音管理
        introduceManager = new IntroduceManager();
        //初始化数据
        initData();
        // 初始化UDP
        initUdp();
    }


    private void initData() {
        mPresenter.addLocalData(introduceManager);
        mChatAdapter.addItem(new Chat("您好，很高兴为您服务，请问有什么可以帮您？", 0));
        mPresenter.doAnswer("您好，很高兴为您服务，请问有什么可以帮您？");

        doInfoScroll();
    }

    private void initUdp() {
        //开启UDP Service
        startService(new Intent(MainView.this, UdpService.class));
        mLbmManager = LocalBroadcastManager.getInstance(this);
        startService(new Intent(this, UdpService.class));

        mUdpAcceptReceiver = new UDPAcceptReceiver(this);
        IntentFilter udp_intent = new IntentFilter(Constants.UDP_ACCEPT_ACTION);
        mLbmManager.registerReceiver(mUdpAcceptReceiver, udp_intent);
    }

    @OnClick({R.id.tv_registerService, R.id.tv_illegalrService, R.id.tv_costService, R.id.tv_businessService, R.id.tv_identityService, R.id.tv_moneyService, R.id.iv_qrcode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_registerService://挂号服务
                startActivity(RegisterView.class);
                break;
            case R.id.tv_illegalrService://违章查询
                startActivity(TrafficView.class);
                break;
            case R.id.tv_costService://e缴费
                startActivity(AddDataView.class);
//                showToast("暂未开通此功能");
//                mPresenter.doAnswer("暂未开通此功能");
                break;
            case R.id.tv_businessService://业务查询
                startActivity(BusinessView.class);
                break;
            case R.id.tv_identityService://身份认证
                startActivity(FaceDistinguishView.class);
                break;
            case R.id.tv_moneyService://理财产品
                startActivity(MoneyServiceView.class);
                break;
            case R.id.iv_qrcode://二维码
                showQRCode();
                break;
        }
    }

    //设置消息的滚动
    public void doInfoScroll() {
        if (mChatList != null && mChatList.size() > 0) {
            rvMainService.smoothScrollToPosition(mChatList.size() - 1);
        }
    }

    private BroadcastReceiver handleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mPresenter.ACTION_MAIN_SPECIATYPE)) {
                // 首页根据语音跳转其他页面 exercise: 取号服务
                SpecialType specialType = (SpecialType) intent.getSerializableExtra("specialType");
                onMainHandle(specialType);
            }
        }
    };

    // 判断语音输入类型
    @Override
    public void onResumeVoice() {
        /**
         * UDP
         * */
        mPresenter.setMySpeech(MySpeech.SPEECH_AIUI);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BasePresenter.ACTION_MAIN_SPECIATYPE);
        intentFilter.addAction(BasePresenter.ACTION_MAIN_SHOWTEXT);
        intentFilter.addAction(BasePresenter.ACTION_AIUI_EXIT);
        registerReceiver(handleReceiver, intentFilter);
        if (isFirst) {
            isFirst = false;
        } else {
            //开启语音监听
            mBaseHandler.sendEmptyMessageDelayed(100, 500);
        }
    }

    @Override
    public void onPauseReceiver() {
        unregisterReceiver(handleReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mLbmManager.unregisterReceiver(mUdpAcceptReceiver);
        stopService(new Intent(MainView.this, UdpService.class));
    }

    // 讲故事 笑话
    @Override
    public void special(String result, SpecialType type) {
        if (mPresenter.getMySpeech().equals(MySpeech.SPEECH_RECOGNIZER_VOICE)) {

        } else if (mPresenter.getMySpeech().equals(MySpeech.SPEECH_AIUI)) {
//            mChatAdapter.addItem(new Chat(result, 0));
//            doInfoScroll();
        }
        switch (type) {
            case Story:
                String[] arrStory = getResources().getStringArray(R.array.local_story);
                String finalStory = arrStory[new Random().nextInt(arrStory.length)];
                mChatAdapter.addItem(new Chat(result, 1));
                mChatAdapter.addItem(new Chat(finalStory, 0));
                doInfoScroll();
                addSpeakAnswer(finalStory);
                break;
            case Music:
//                mPresenter.playMusic();
                addSpeakAnswer("暂无此功能");
                break;
            case Joke:
                String[] arrJoke = getResources().getStringArray(R.array.local_joke);
                String finalJoke = arrJoke[new Random().nextInt(arrJoke.length)];
                mChatAdapter.addItem(new Chat(result, 1));
                mChatAdapter.addItem(new Chat(finalJoke, 0));
                doInfoScroll();
                addSpeakAnswer(finalJoke);
                break;
        }
    }

    @Override
    public void spakeMove(SpecialType specialType, String result) {
        mPresenter.onCompleted();
        switch (specialType) {
            case Forward:
                mSerialPresenter.receiveMotion(ComType.A, "A5038002AA");
                break;
            case Backoff:
                mSerialPresenter.receiveMotion(ComType.A, "A5038008AA");
                break;
            case Turnleft:
                mSerialPresenter.receiveMotion(ComType.A, "A5038004AA");
                ;
                break;
            case Turnright:
                mSerialPresenter.receiveMotion(ComType.A, "A5038006AA");
                break;
        }
    }


    // 播放答案
    protected void addSpeakAnswer(String messageContent) {
        mPresenter.doAnswer(messageContent);
        // 根据长度做出对应的动作
        if (messageContent.length() <= 13) {
            mSerialPresenter.receiveMotion(ComType.A, "A50C8001AA");
        } else if (messageContent.length() > 13 && messageContent.length() <= 40) {
            mSerialPresenter.receiveMotion(ComType.A, "A50C8003AA");
        } else {
            mSerialPresenter.receiveMotion(ComType.A, "A50C8021AA");
        }
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

    // aiui 回答
    @Override
    public void doAiuiAnwer(String anwer) {
        addSpeakAnswer(anwer);
    }

    //普通问答
    @Override
    public void refHomePage(String question, String finalText) {
        if (!"".equals(question)) {
            mChatAdapter.addItem(new Chat(question, 1));
            doInfoScroll();
        }
        mChatAdapter.addItem(new Chat(finalText, 0));
        doInfoScroll();
    }

    //新闻
    @Override
    public void refHomePage(String question, News news) {
        if (question != null) {
            mChatAdapter.addItem(new Chat(question, 1));
//            mChatAdapter.addItem(new Chat(news.getContent(), 0));
            doInfoScroll();
        }
    }

    //无线收音机
    @Override
    public void refHomePage(String question, Radio radio) {
        if (question != null) {
            mChatAdapter.addItem(new Chat(question, 1));
            mChatAdapter.addItem(new Chat(radio.getName(), 0));
            doInfoScroll();
        }
    }


    //古诗
    @Override
    public void refHomePage(String question, Poetry poetry) {
        if (question != null) {
            mChatAdapter.addItem(new Chat(question, 1));
            mChatAdapter.addItem(new Chat(poetry.getContent(), 0));
            doInfoScroll();
        }
    }

    //菜谱
    @Override
    public void refHomePage(String question, Cookbook cookbook) {
        if (question != null) {
            mChatAdapter.addItem(new Chat(question, 1));
            mChatAdapter.addItem(new Chat(cookbook.getSteps(), 0));
            doInfoScroll();
        }
    }

    //每日英语
    @Override
    public void refHomePage(String question, EnglishEveryday englishEveryday) {
        if (question != null) {
            mChatAdapter.addItem(new Chat(question, 1));
            mChatAdapter.addItem(new Chat(englishEveryday.getContent(), 0));
            doInfoScroll();
        }
    }

    //打电话
    @Override
    public void doCallPhone(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 开始跳舞 A50C80E1AA
     * 停止跳舞 A50C80E2AA
     */
    @Override
    public void doDance() {
        DanceUtils.getInstance().startDance(MainView.this);//唱歌
        mSerialPresenter.receiveMotion(ComType.A, "A50C80E1AA");
    }

    // 首页 语音控制跳转
    public void onMainHandle(SpecialType specialType) {
        switch (specialType) {
            case BusinessService://业务查询
                startActivity(BusinessView.class);
                break;
            case RegisterService://挂号服务
                startActivity(RegisterView.class);
                break;
            case IllegalrService://违章查询
                startActivity(TrafficView.class);
                break;
            case CostService://e缴费
//                showToast("暂未开通此功能");
//                mPresenter.doAnswer("暂未开通此功能");
//                mPresenter.startRecognizerListener();
//                mPresenter.setMySpeech(MySpeech.SPEECH_AIUI);
                break;
            case IdentityService://身份认证
                startActivity(FaceDistinguishView.class);
                break;
            case MoneyService://理财产品
                startActivity(MoneyServiceView.class);
                break;

        }
    }

    /**
     * 二维码
     */
    public void showQRCode() {
        if (findQRCodeDialog == null) {
            findQRCodeDialog = new FindQRCodeDialog(MainView.this);
        }
        findQRCodeDialog.show();
    }

    @Override
    public void UDPinitFinsih(String content) {
        showToast(content);
    }

    @Override
    public void UDPAcceptMessage(String content) {
        if (isSport) {
//            showToast(content);
            ReceiveMotion(ComType.A, content);
        }
    }

}
