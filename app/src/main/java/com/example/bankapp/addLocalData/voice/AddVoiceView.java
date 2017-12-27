package com.example.bankapp.addLocalData.voice;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bankapp.R;
import com.example.bankapp.asr.MySpeech;
import com.example.bankapp.base.view.PresenterActivity;
import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.database.manager.IntroduceManager;
import com.example.bankapp.modle.LocalMoneyService;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.radio.Radio;
import com.example.bankapp.util.PreferencesUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加本地音频
 *
 * @author Guanluocang
 *         created at 2017/12/21 10:36
 */
public class AddVoiceView extends PresenterActivity<AddVoicePreSenter> implements IAddVoiceView {

    @BindView(R.id.ed_question)
    EditText edQuestion;
    @BindView(R.id.ed_answer)
    EditText edAnswer;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    private IntroduceManager introduceManager;

    //布局文件
    @Override
    protected int getContentViewResource() {
        return R.layout.activity_add_voice_view;
    }

    @Override
    public AddVoicePreSenter createPresenter() {
        return new AddVoicePreSenter(this);
    }

    @Override
    public void onResumeVoice() {
        mPresenter.setMySpeech(MySpeech.SPEECH_NULL);
    }

    @Override
    public void onPauseReceiver() {

    }

    @Override
    public Context getContext() {
        return this;
    }


    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        introduceManager = new IntroduceManager();
    }

    @OnClick({R.id.iv_goback, R.id.tv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_goback:
                finish();
                break;
            case R.id.tv_add:
                addLoaclVoice();
                break;
        }
    }

    /**
     * 添加数据
     */
    private void addLoaclVoice() {
        String question = edQuestion.getText().toString().trim();
        String answer = edAnswer.getText().toString().trim();
        if (!"".equals(question) || !"".equals(answer)) {
            introduceManager.insert(new LocalMoneyService(question, answer));
            finish();
        } else {
            showToast("输入不能为空");
        }
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
