package com.example.bankapp.traffic.record;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.bankapp.R;
import com.example.bankapp.adapter.MoneyCurrentApdater;
import com.example.bankapp.adapter.MoneyIntroduceFinanceAdapter;
import com.example.bankapp.adapter.MoneyIntroduceShowAdapter;
import com.example.bankapp.adapter.TrafficRecordAdapter;
import com.example.bankapp.animator.SlideInOutBottomItemAnimator;
import com.example.bankapp.asr.MySpeech;
import com.example.bankapp.base.presenter.BasePresenter;
import com.example.bankapp.base.view.PresenterActivity;
import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.modle.TrafficRecord;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.radio.Radio;
import com.iflytek.cloud.thirdparty.V;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 违章查询记录
 *
 * @author Guanluocang
 *         created at 2017/12/28 20:14
 */
public class TrafficRecordView extends PresenterActivity<TrafficRecordPresenter> implements ITrafficRecordView {


    @BindView(R.id.tv_goBack)
    TextView tvGoBack;
    @BindView(R.id.tv_confim)
    TextView tvConfim;
    @BindView(R.id.rv_trafficRecord)
    RecyclerView rvTrafficRecord;
    @BindView(R.id.tv_noInfo)
    TextView tvNoInfo;

    private TrafficRecordAdapter trafficRecordAdapter;
    private List<TrafficRecord> trafficRecordsl = new ArrayList<>();

    @Override
    public TrafficRecordPresenter createPresenter() {
        return new TrafficRecordPresenter(this);
    }

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_traffic_record_view;
    }

    @Override
    public Context getContext() {
        return this;
    }

    // 判断语音输入类型
    @Override
    public void onResumeVoice() {
        mPresenter.setMySpeech(MySpeech.SPEECH_EXIT);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BasePresenter.ACTION_OTHER_FINISH);
        registerReceiver(trafficRecordReceiver, filter);
    }

    @Override
    public void onPauseReceiver() {
        unregisterReceiver(trafficRecordReceiver);
    }

    private BroadcastReceiver trafficRecordReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BasePresenter.ACTION_OTHER_FINISH)) {
                onExit();
            }
        }
    };

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        initDate();
//        mPresenter.doAnswer("您当前没有违章记录，请点击确定按钮返回到首页");
    }

    private void initDate() {


        trafficRecordsl.add(new TrafficRecord("京Q123321", "任意停靠", "100元", "3分"));
        trafficRecordsl.add(new TrafficRecord("京N567892", "闯红灯", "300元", "6分"));
        trafficRecordsl.add(new TrafficRecord("京A233445", "未礼让行人", "50元", "3分"));
        trafficRecordsl.add(new TrafficRecord("京Q764234", "酒驾", "1000元", "12分"));
        if (trafficRecordsl.size() > 0) {
            tvNoInfo.setVisibility(View.GONE);
            rvTrafficRecord.setVisibility(View.VISIBLE);
        } else {
            tvNoInfo.setVisibility(View.VISIBLE);
            rvTrafficRecord.setVisibility(View.GONE);
        }
        //理财介绍
        trafficRecordAdapter = new TrafficRecordAdapter(this, trafficRecordsl);
        rvTrafficRecord.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //设置动画
        rvTrafficRecord.setItemAnimator(new SlideInOutBottomItemAnimator(rvTrafficRecord));
        //添加分割线
        rvTrafficRecord.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvTrafficRecord.setAdapter(trafficRecordAdapter);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_goBack, R.id.tv_confim})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_goBack:
                finish();
                break;
            case R.id.tv_confim:
                finish();
                break;
        }
    }
}
