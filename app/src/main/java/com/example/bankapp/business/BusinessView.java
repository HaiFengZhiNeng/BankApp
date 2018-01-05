package com.example.bankapp.business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bankapp.R;
import com.example.bankapp.asr.MySpeech;
import com.example.bankapp.base.presenter.BasePresenter;
import com.example.bankapp.base.view.PresenterActivity;
import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.radio.Radio;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 业务查询
 *
 * @author Guanluocang
 *         created at 2017/12/13 11:04
 */
public class BusinessView extends PresenterActivity<BusinessPresenter> implements IBusinessView {


    @BindView(R.id.tv_goBack)
    TextView tvGoBack;
    @BindView(R.id.ll_pay)
    LinearLayout llPay;//自助缴费
    @BindView(R.id.ll_window)
    LinearLayout llWindow;//柜台窗口
    @BindView(R.id.ll_money)
    LinearLayout llMoney;//理财专区
    @BindView(R.id.ll_service)
    LinearLayout llService;//大堂服务

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_business_view;
    }


    @Override
    public Context getContext() {
        return this;
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
    public BusinessPresenter createPresenter() {
        return new BusinessPresenter(this);
    }

    @Override
    public void onResumeVoice() {

        mPresenter.setMySpeech(MySpeech.SPEECH_EXIT);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BasePresenter.ACTION_OTHER_FINISH);
        registerReceiver(businessReceiver, filter);

    }

    @Override
    public void onPauseReceiver() {
        unregisterReceiver(businessReceiver);
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

    private BroadcastReceiver businessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BasePresenter.ACTION_OTHER_FINISH)) {
                onExit();
            }
        }
    };

    @OnClick({R.id.tv_goBack, R.id.ll_pay, R.id.ll_window, R.id.ll_money, R.id.ll_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_goBack:
                finish();
                break;
            case R.id.ll_pay://自助缴费
                showToast("正在带领您前往自助缴费区域");
                break;
            case R.id.ll_window://柜台窗口
                showToast("正在带领您前往柜台窗口区域");
                break;
            case R.id.ll_money://理财专区
                showToast("正在带领您前往理财专区");
                break;
            case R.id.ll_service://大堂服务
                showToast("正在带领您前往大堂服务区域");
                break;
        }
    }
}
