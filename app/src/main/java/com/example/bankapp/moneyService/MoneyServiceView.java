package com.example.bankapp.moneyService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bankapp.R;
import com.example.bankapp.adapter.ChatAdapter;
import com.example.bankapp.adapter.MoneyCurrentApdater;
import com.example.bankapp.adapter.MoneyIntroduceAdapter;
import com.example.bankapp.adapter.MoneyIntroduceFinanceAdapter;
import com.example.bankapp.animator.SlideInOutBottomItemAnimator;
import com.example.bankapp.asr.MySpeech;
import com.example.bankapp.base.adapter.BaseRecyclerAdapter;
import com.example.bankapp.base.view.PresenterActivity;
import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.database.manager.IntroduceFinanceManager;
import com.example.bankapp.database.manager.IntroduceManager;
import com.example.bankapp.modle.Chat;
import com.example.bankapp.modle.LocalMoneyService;
import com.example.bankapp.modle.LocalMoneyVideo;
import com.example.bankapp.modle.MoneyCurrent;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.radio.Radio;
import com.example.bankapp.moneyProduct.MoneyProductView;
import com.example.bankapp.util.DanceUtils;
import com.example.bankapp.video.VideoDetailView;
import com.example.bankapp.view.ChatLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 理财产品
 *
 * @author Guanluocang
 *         created at 2017/12/11 14:14
 */
public class MoneyServiceView extends PresenterActivity<MoneyServicePresenter> implements IMoneyServiceView {

    @BindView(R.id.tv_goBack)
    TextView tvGoBack;
    @BindView(R.id.iv_introduce)
    ImageView ivIntroduce;//理财介绍
    @BindView(R.id.rv_introduce)
    RecyclerView rvIntroduce;
    @BindView(R.id.iv_introduceFinance)
    ImageView ivIntroduceFinance;//理财与财经
    @BindView(R.id.rv_introduceFinance)
    RecyclerView rvIntroduceFinance;
    @BindView(R.id.rv_currentMoney)
    RecyclerView rvCurrentMoney;//活期理财
    @BindView(R.id.rv_money_service)
    RecyclerView rvMoneyService;//聊天
    @BindView(R.id.tv_title)
    TextView tvTitle;

    //理财介绍Adapter
    private MoneyIntroduceAdapter moneyIntroduceAdapter;
    private List<LocalMoneyService> moneyIntroduceList = new ArrayList<>();

    //理财与财经Adapter
    private MoneyIntroduceFinanceAdapter moneyIntroduceFinanceAdapter;
    private List<LocalMoneyVideo> moneyVideoList = new ArrayList<>();

    //对话框
    private ChatAdapter mChatAdapter;
    private List<Chat> mChatList = new ArrayList<>();

    // 活期理财
    private MoneyCurrentApdater moneyCurrentApdater;
    private List<MoneyCurrent> moneyCurrentList = new ArrayList<>();

    //理财介绍 Manager
    private IntroduceManager introduceManager;
    //理财与财经 Manager
    private IntroduceFinanceManager introduceFinanceManager;

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_money_service_view;
    }

    @Override
    public MoneyServicePresenter createPresenter() {
        return new MoneyServicePresenter(this);
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

    @Override
    public void onResumeVoice() {

        mPresenter.setMySpeech(MySpeech.SPEECH_VOICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(mPresenter.ACTION_OTHER_EXIT);
        filter.addAction(mPresenter.ACTION_OTHER_RESULT);
        registerReceiver(businessReceiver, filter);
        addSpeakAnswer(resFoFinal(R.array.manual_location_voice));

    }

    @Override
    public void onPauseReceiver() {
        unregisterReceiver(businessReceiver);
    }

    // 创建广播
    private BroadcastReceiver businessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mPresenter.ACTION_OTHER_EXIT)) {
                onExit();
            } else if (intent.getAction().equals(mPresenter.ACTION_OTHER_RESULT)) {
                String text = intent.getStringExtra("result");
                onVoiceResult(text);
            }
        }
    };

    private void initView() {
        tvTitle.setText("石景山支行理财产品");

        //初始化manager
        introduceManager = new IntroduceManager();
        introduceFinanceManager = new IntroduceFinanceManager();
        //理财介绍
        moneyIntroduceAdapter = new MoneyIntroduceAdapter(this, moneyIntroduceList);
        //理财与财经
        moneyIntroduceFinanceAdapter = new MoneyIntroduceFinanceAdapter(this, moneyVideoList);
        //活期理财
        moneyCurrentApdater = new MoneyCurrentApdater(this, moneyCurrentList);

        //理财介绍
        rvIntroduce.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //设置动画
        rvIntroduce.setItemAnimator(new SlideInOutBottomItemAnimator(rvIntroduce));
        //添加分割线
        rvIntroduce.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvIntroduce.setAdapter(moneyIntroduceAdapter);

        //理财与财经
        rvIntroduceFinance.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //设置动画
        rvIntroduceFinance.setItemAnimator(new SlideInOutBottomItemAnimator(rvIntroduce));
        //添加分割线
        rvIntroduceFinance.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvIntroduceFinance.setAdapter(moneyIntroduceFinanceAdapter);

        // 活期理财
        rvCurrentMoney.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //设置动画
        rvCurrentMoney.setItemAnimator(new SlideInOutBottomItemAnimator(rvCurrentMoney));
        //添加分割线
        rvCurrentMoney.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvCurrentMoney.setAdapter(moneyCurrentApdater);

        // 对话recyclever
        mChatAdapter = new ChatAdapter(this, mChatList);
        rvMoneyService.setLayoutManager(new ChatLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //设置动画
        rvMoneyService.setItemAnimator(new SlideInOutBottomItemAnimator(rvMoneyService));
        rvMoneyService.setAdapter(mChatAdapter);

        initData();
    }

    // 初始化数据
    private void initData() {
        //理财介绍
        moneyIntroduceList = introduceManager.loadAll();
        moneyIntroduceAdapter.refreshData(moneyIntroduceList);

        // 理财与财经
        moneyVideoList = introduceFinanceManager.loadAll();
        moneyIntroduceFinanceAdapter.refreshData(moneyVideoList);

        //活期理财
        moneyCurrentApdater.addData(new MoneyCurrent("天天盈", "4.500%", "昨日年化收益率", "灵活存取", "1000元"));
        moneyCurrentApdater.addData(new MoneyCurrent("定天盈", "4.125%", "五日年化收益率", "随存随取", "1560元"));
        moneyCurrentApdater.addData(new MoneyCurrent("年年盈", "5.236%", "预期年化收益率", "灵活存取", "6743元"));
        moneyCurrentApdater.addData(new MoneyCurrent("活期盈", "3.885%", "近日年化收益率", "随存随取", "135元"));

        // 聊天
        mChatAdapter.addItem(new Chat(resFoFinal(R.array.manual_location_voice), 0));//初始化  可以向我提问
        if (mChatList != null && mChatList.size() > 0) {
            rvMoneyService.smoothScrollToPosition(mChatList.size() - 1);
        }

        //理财介绍
        moneyIntroduceAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onVoiceResult(moneyIntroduceList.get(position).getIntroduceQuestion());
            }
        });
        //理财与财经
        moneyIntroduceFinanceAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("url", moneyVideoList.get(position).getVideoAddress());
                startActivity(VideoDetailView.class, bundle);// 本地视频
            }
        });
        //活期理财
        moneyCurrentApdater.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(MoneyProductView.class); //理财产品 转入
            }
        });

    }

    @OnClick({R.id.tv_goBack, R.id.iv_introduce, R.id.iv_introduceFinance})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_goBack:
                finish();
                break;
            case R.id.iv_introduce://理财介绍
                mPresenter.setIntroduceVisibility();
                break;
            case R.id.iv_introduceFinance://理财与财经
                mPresenter.setIntroduceFinanceVisibility();
                break;
        }
    }

    // 理财介绍 显示与隐藏
    @Override
    public void isIntroduceVisibility(boolean isVisibility) {
        rvIntroduce.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
        if (isVisibility) {
            ivIntroduce.setImageResource(R.mipmap.ic_icon_down);
        } else {
            ivIntroduce.setImageResource(R.mipmap.ic_icon_down_right);
        }
    }

    // 理财与财经 显示与隐藏
    @Override
    public void isIntroduceFinanceVisibility(boolean isVisibility) {
        rvIntroduceFinance.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
        if (isVisibility) {
            ivIntroduceFinance.setImageResource(R.mipmap.ic_icon_down);
        } else {
            ivIntroduceFinance.setImageResource(R.mipmap.ic_icon_down_right);
        }
    }

    public String text = "";

    //判断是否属于 理财 内容
    public void onVoiceResult(String result) {

        List<LocalMoneyService> localVoiceList = introduceManager.queryIntroduceByQuestion(result);//本地语言
        List<LocalMoneyVideo> localVideosList = introduceFinanceManager.queryIntroduceByQuestion(result);//本地视频
        if (localVoiceList != null && localVoiceList.size() > 0) {// 属于本地语音
            LocalMoneyService localVoice = localVoiceList.get(new Random().nextInt(localVoiceList.size()));
            if (localVoiceList.size() == 1) {
                text = localVoice.getIntroduceAnswer();
            } else {
                text = localVoice.getIntroduceQuestion() + "  \n" + localVoice.getIntroduceAnswer();
            }
            doLocalChatScroll(result, text);
            addSpeakAnswer(text);
        } else if (localVideosList != null && localVideosList.size() > 0) {// 属于本地视频
            LocalMoneyVideo localVideo = localVideosList.get(new Random().nextInt(localVideosList.size()));
            Bundle bundle = new Bundle();
            bundle.putString("url", localVideo.getVideoAddress());
            startActivity(VideoDetailView.class, bundle);
        } else {//没有听懂的结果
            text = resFoFinal(R.array.no_voice);
            doLocalChatScroll(result, text);
            addSpeakAnswer(text);
        }


    }

    //设置消息的滚动
    public void doLocalChatScroll(String question, String text) {
        mChatAdapter.addItem(new Chat(question, 1));
        mChatAdapter.addItem(new Chat(text, 0));
        if (mChatList != null && mChatList.size() > 0) {
            rvMoneyService.smoothScrollToPosition(mChatList.size() - 1);
        }
    }


    // 默认进入 播放内容
    public String resFoFinal(int id) {
        String[] arrResult = MoneyServiceView.this.getResources().getStringArray(id);
        return arrResult[new Random().nextInt(arrResult.length)];
    }

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
    public void special(String result, SpecialType type) {

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
}
