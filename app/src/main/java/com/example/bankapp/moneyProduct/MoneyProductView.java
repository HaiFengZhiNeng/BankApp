package com.example.bankapp.moneyProduct;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
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
import com.example.bankapp.perfectInfor.PerfectInforView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 理财产品 转入
 *
 * @author Guanluocang
 *         created at 2017/12/12 15:21
 */
public class MoneyProductView extends PresenterActivity<MoneyProducePresenter> implements IMoneyProductView {

    @BindView(R.id.tv_goBack)
    TextView tvGoBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_productTitle)
    TextView tvProductTitle;//顶部标题
    @BindView(R.id.tv_productPercentage)
    TextView tvProductPercentage;//顶部收益率
    @BindView(R.id.tv_productPercentageTwo)
    TextView tvProductPercentageTwo;//顶部收益率下方
    @BindView(R.id.tv_productToday)
    TextView tvProductToday;//当日计息
    @BindView(R.id.tv_productAccess)
    TextView tvProductAccess;//灵活收取
    @BindView(R.id.tv_productWin)
    TextView tvProductWin;//收益更高
    @BindView(R.id.tv_productInto)
    TextView tvProductInto;//立即转入

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_money_product_view;
    }

    @Override
    public MoneyProducePresenter createPresenter() {
        return new MoneyProducePresenter(this);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_goBack, R.id.tv_productTitle, R.id.tv_productPercentage, R.id.tv_productPercentageTwo, R.id.tv_productToday, R.id.tv_productAccess, R.id.tv_productWin, R.id.tv_productInto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_goBack:
                finish();
                break;
            case R.id.tv_productTitle://顶部标题
                break;
            case R.id.tv_productPercentage://顶部收益率
                break;
            case R.id.tv_productPercentageTwo://顶部收益率下方
                break;
            case R.id.tv_productToday://当日计息
                showToast("当日计息");
                break;
            case R.id.tv_productAccess://灵活收取
                showToast("灵活收取");
                break;
            case R.id.tv_productWin://收益更高
                showToast("收益更高");
                break;
            case R.id.tv_productInto://立即转入
                startActivity(PerfectInforView.class);
                break;
        }
    }
}
