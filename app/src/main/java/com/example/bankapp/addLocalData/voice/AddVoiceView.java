package com.example.bankapp.addLocalData.voice;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.example.bankapp.util.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.iv_goback)
    ImageView ivGoback;
    @BindView(R.id.iv_addData)
    ImageView ivAddData;
    @BindView(R.id.tv_expression)
    TextView tvExpression;
    @BindView(R.id.tv_action)
    TextView tvAction;
    @BindView(R.id.tv_datatype)
    TextView tvDatatype;

    private IntroduceManager introduceManager;

    //语音
    public static final int LOCAL_SPEECH = 0;
    //导航
    public static final int LOCAL_NAVIGATION = 1;

    //添加的类型
    private int addSoundType;

    //添加的是哪个Item
    private int addExpressionItem;
    private int addActionItem;

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
        initView();
    }

    private void initView() {
        //初始化 数据库Manager
        introduceManager = new IntroduceManager();
        //添加按钮 隐藏
        ivAddData.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_goback, R.id.tv_add, R.id.tv_expression, R.id.tv_action, R.id.tv_datatype})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_goback:
                finish();
                break;
            case R.id.tv_add:
                addLoaclVoice();
                break;
            case R.id.tv_expression:
                DialogUtils.showLongListDialog(AddVoiceView.this, "面部表情", R.array.expression, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        addExpressionItem = position;
                        tvExpression.setText(text);
                    }
                });
                break;
            case R.id.tv_action:
                int res = R.array.action;
                if (addSoundType == LOCAL_SPEECH) {
                    res = R.array.action;
                } else if (addSoundType == LOCAL_NAVIGATION) {
                    res = R.array.navigation;
                }
                DialogUtils.showLongListDialog(AddVoiceView.this, "执行动作", res, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        addActionItem = position;
                        tvAction.setText(text);
                    }
                });
                break;
            case R.id.tv_datatype:
                DialogUtils.showLongListDialog(AddVoiceView.this, "类型", R.array.dataType, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
//                        curSoundType = position;
                        tvDatatype.setText(text);
                        if (position == 0) {
                            addSoundType = LOCAL_SPEECH;
                            tvAction.setText(getResources().getStringArray(R.array.action)[0]);
                        } else {
                            addSoundType = LOCAL_NAVIGATION;
                            tvAction.setText(getResources().getStringArray(R.array.navigation)[0]);
                        }
                    }
                });
                break;
        }
    }

    /**
     * 添加数据
     */
    private void addLoaclVoice() {
        String question = edQuestion.getText().toString().trim();
        String answer = edAnswer.getText().toString().trim();
        String action = tvAction.getText().toString().trim();
        String expression = tvExpression.getText().toString().trim();
        String type = tvDatatype.getText().toString();
        if (isEmpty(edQuestion) || isEmpty(edAnswer) || isEmpty(tvExpression) || isEmpty(tvAction)) {
            showToast("输入不能为空！");
            return;
        }
        //判断是否有数据 有 则更新 无 则 插入
        //TODO 优化添加数据 以及点击事件
        introduceManager.insert(new LocalMoneyService(type, question, answer, action, resArray(R.array.action)[addActionItem], expression, resArray(R.array.expression)[addExpressionItem]));
        finish();
        showToast("添加成功");
    }

    //判断是否为空
    private boolean isEmpty(TextView textView) {
        return textView.getText().toString().trim().equals("") || textView.getText().toString().trim().equals("");
    }

    //获取资源文件
    private String[] resArray(int resId) {
        return getResources().getStringArray(resId);
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

    @Override
    public void doDance() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
