package com.example.bankapp.addLocalData;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.bankapp.R;
import com.example.bankapp.addLocalData.video.AddVideoView;
import com.example.bankapp.addLocalData.voice.AddVoiceView;
import com.example.bankapp.asr.MySpeech;
import com.example.bankapp.base.view.PresenterActivity;
import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.fragment.IntroduceFinanceFragment;
import com.example.bankapp.fragment.IntroduceFragment;
import com.example.bankapp.modle.AddData;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.radio.Radio;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加 本地数据
 *
 * @author Guanluocang
 *         created at 2017/12/21 14:08
 */
public class AddDataView extends PresenterActivity<AddDataPresenter> implements IAddDataView {


    @BindView(android.R.id.tabcontent)
    FrameLayout tabcontent;
    @BindView(android.R.id.tabhost)
    FragmentTabHost tabhost;
    @BindView(R.id.realtabcontent)
    FrameLayout realtabcontent;
    @BindView(R.id.iv_goback)
    ImageView ivGoback;
    @BindView(R.id.iv_addData)
    ImageView ivAddData;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private List<AddData> mTabs = new ArrayList<>(5);
    private LayoutInflater mInflater;

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_add_data_view;
    }

    @Override
    public AddDataPresenter createPresenter() {
        return new AddDataPresenter(this);
    }

    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public void onResumeVoice() {
        mPresenter.setMySpeech(MySpeech.SPEECH_NULL);
    }

    @Override
    public void onPauseReceiver() {

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
    protected void onViewCreated() {
        super.onViewCreated();
        initTab();
    }

    //初始化添加对象
    private void initTab() {

        AddData tab_policy = new AddData("理财介绍", IntroduceFragment.class);
        AddData tab_voice = new AddData("理财与财经", IntroduceFinanceFragment.class);


        mTabs.add(tab_policy);
        mTabs.add(tab_voice);

        mInflater = LayoutInflater.from(this);
        tabhost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        tabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        for (AddData tab : mTabs) {

            TabHost.TabSpec tabSpec = tabhost.newTabSpec((tab.getName()));

            tabSpec.setIndicator(buildIndicator(tab));

            tabhost.addTab(tabSpec, tab.getFragment(), null);

        }

        tabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        tabhost.setCurrentTab(0);


    }

    private View buildIndicator(AddData tab) {


        View view = mInflater.inflate(R.layout.tab_indicator, null);

        TextView text = (TextView) view.findViewById(R.id.txt_indicator);

        text.setText(tab.getName());

        return view;
    }

    @OnClick({R.id.iv_goback, R.id.iv_addData})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_goback:
                finish();
                break;
            case R.id.iv_addData:
                if (0 == tabhost.getCurrentTab()) {
                    startActivity(AddVoiceView.class);
                } else {
                    startActivity(AddVideoView.class);
                }
                break;
        }
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
