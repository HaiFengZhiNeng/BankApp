package com.example.bankapp.main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.bankapp.R;
import com.example.bankapp.adapter.ChatAdapter;
import com.example.bankapp.adapter.MainRollViewAdapter;
import com.example.bankapp.addLocalData.AddDataView;
import com.example.bankapp.animator.SlideInOutBottomItemAnimator;
import com.example.bankapp.asr.MySpeech;
import com.example.bankapp.base.handler.BaseHandler;
import com.example.bankapp.base.view.PresenterActivity;
import com.example.bankapp.business.BusinessView;
import com.example.bankapp.common.enums.SpecialType;
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
import com.example.bankapp.util.DanceUtils;
import com.example.bankapp.util.MediaPlayerUtil;
import com.example.bankapp.util.PermissionsChecker;
import com.example.bankapp.view.ChatLinearLayoutManager;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class MainView extends PresenterActivity<MainPresenter> implements IMainView, BaseHandler.HandleMessage {

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

    //对话框
    private ChatAdapter mChatAdapter;
    private List<Chat> mChatList = new ArrayList<>();

    //权限列表
    static final String[] PERMISSIONS = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA
    };
    //权限
    private PermissionsChecker mChecker;
    private static final int PERMISSION_REQUEST_CODE = 0; // 系统权限管理页面的参数
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案

    //handler
    private static final int REFRESH_COMPLETE = 0X153;
    private Handler handler = new BaseHandler<>(MainView.this);

    //本地语音Manager
    private IntroduceManager introduceManager;

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
        initPermission();
    }

    private void initPermission() {
        mChecker = new PermissionsChecker(this);
        if (mChecker.lacksPermissions(PERMISSIONS)) {

            if (mChecker.lacksPermissions(PERMISSIONS)) {
                //请求权限
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);

            } else {
                initView();// 全部权限都已获取
            }
        } else {
            initView();
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
    }

    private void initData() {
        mPresenter.addLocalData(introduceManager);
        mChatAdapter.addItem(new Chat("您好，很高兴为您服务，请问有什么可以帮您？", 0));
        mPresenter.doAnswer("您好，很高兴为您服务，请问有什么可以帮您？");
        doInfoScroll();
    }

    @OnClick({R.id.tv_registerService, R.id.tv_illegalrService, R.id.tv_costService, R.id.tv_businessService, R.id.tv_identityService, R.id.tv_moneyService})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_registerService://挂号服务
                startActivity(RegisterView.class);
                break;
            case R.id.tv_illegalrService://违章查询
                showToast("暂未开通此功能");
                mPresenter.doAnswer("暂未开通此功能");
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
        }
    }


    //请求权限结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            initView();
        } else {
            handler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);
        }
    }


    //含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case REFRESH_COMPLETE:
                finish();
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
                SpecialType specialType = (SpecialType) intent.getSerializableExtra("specialType");

                onMainHandle(specialType);
//                mPresenter.receiveMotion(ComType.A, myTalk());//进入政策和本地语音等界面做的动作
            } else if (intent.getAction().equals(mPresenter.ACTION_MAIN_SHOWTEXT)) {
//                String quetion = intent.getStringExtra("question");
//                if (!"".equals(quetion)) {
//                    setQuestionText(quetion);
//                }
//                String text = intent.getStringExtra("showText");
//                setAnwerText(text);
            }
        }
    };

    // 判断语音输入类型
    @Override
    public void onResumeVoice() {
        /**
         * UDP
         * */
//        mUdpAcceptReceiver = new UDPAcceptReceiver(this);
//        IntentFilter intentFilter = new IntentFilter(Constants.UDP_ACCEPT_ACTION);
//        mLbmManager.registerReceiver(mUdpAcceptReceiver, intentFilter);
        mPresenter.setMySpeech(MySpeech.SPEECH_AIUI);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(mPresenter.ACTION_MAIN_SPECIATYPE);
        intentFilter.addAction(mPresenter.ACTION_MAIN_SHOWTEXT);
        intentFilter.addAction(mPresenter.ACTION_AIUI_EXIT);
        registerReceiver(handleReceiver, intentFilter);
    }

    @Override
    public void onPauseReceiver() {
        unregisterReceiver(handleReceiver);
    }

    //答案 显示
    public void setAnwerText(String text) {
        mChatAdapter.addItem(new Chat(text, 0));
        doInfoScroll();
    }

    // 问题显示
    public void setQuestionText(String text) {
        mChatAdapter.addItem(new Chat(text, 1));
        doInfoScroll();
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
//        toAdapter.addItem(getChatMessage(ChatRecyclerAdapter.TO_USER_MSG, result));
//        smoothScroll();
//        mSoundPresenter.onCompleted();
//        switch (type) {
//            case Forward:
//                mPresenter.receiveMotion(ComType.A, "A5038002AA");
//                break;
//            case Backoff:
//                mPresenter.receiveMotion(ComType.A, "A5038008AA");
//                break;
//            case Turnleft:
//                mPresenter.receiveMotion(ComType.A, "A5038004AA");
//                ;
//                break;
//            case Turnright:
//                mPresenter.receiveMotion(ComType.A, "A5038006AA");
//
//                break;
//        }
    }

    // aiui 回答
    @Override
    public void doAiuiAnwer(String anwer) {
        addSpeakAnswer(anwer);
    }

    // 播放答案
    protected void addSpeakAnswer(String messageContent) {
        mPresenter.doAnswer(messageContent);
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

    //普通问答
    @Override
    public void refHomePage(String question, String finalText) {
        if (question != null) {
            mChatAdapter.addItem(new Chat(question, 1));
            mChatAdapter.addItem(new Chat(finalText, 0));
            doInfoScroll();
        }
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
                showToast("暂未开通此功能");
                mPresenter.doAnswer("暂未开通此功能");
                mPresenter.startRecognizerListener();
                mPresenter.setMySpeech(MySpeech.SPEECH_AIUI);
                break;
            case CostService://e缴费
                showToast("暂未开通此功能");
                mPresenter.doAnswer("暂未开通此功能");
                mPresenter.startRecognizerListener();
                mPresenter.setMySpeech(MySpeech.SPEECH_AIUI);
                break;
            case IdentityService://身份认证
                startActivity(FaceDistinguishView.class);
                break;
            case MoneyService://理财产品
                startActivity(MoneyServiceView.class);
                break;

        }
    }


}
