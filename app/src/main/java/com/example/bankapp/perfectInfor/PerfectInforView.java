package com.example.bankapp.perfectInfor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 完善信息
 *
 * @author Guanluocang
 *         created at 2017/12/12 18:11
 */
public class PerfectInforView extends PresenterActivity<PerfectInforPresenter> implements IPerfectInforView {

    @BindView(R.id.tv_goBack)
    TextView tvGoBack;
    @BindView(R.id.et_name)
    EditText etName;//姓名
    @BindView(R.id.et_idcard)
    EditText etIdcard;//身份证
    @BindView(R.id.et_telphone)
    EditText etTelphone;//电话
    @BindView(R.id.tv_complete)
    TextView tvComplete;//完成

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_perfect_infor_view;
    }

    @Override
    public PerfectInforPresenter createPresenter() {
        return new PerfectInforPresenter(this);
    }

    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public void onResumeVoice() {

        mPresenter.setMySpeech(MySpeech.SPEECH_AIUI);
        IntentFilter filter = new IntentFilter();
        filter.addAction(mPresenter.ACTION_AIUI_EXIT);
        registerReceiver(businessReceiver, filter);

    }

    @Override
    public void onPauseReceiver() {
        unregisterReceiver(businessReceiver);
    }

    private BroadcastReceiver businessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mPresenter.ACTION_AIUI_EXIT)) {
                onExit();
            }
        }
    };

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

    @Override
    public void doDance() {

    }

    @OnClick({R.id.tv_goBack, R.id.tv_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_goBack:
                finish();
                break;
            case R.id.tv_complete:
                showToast("完成");
                break;
        }
    }
}
