package com.example.bankapp.addLocalData.video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bankapp.R;
import com.example.bankapp.asr.MySpeech;
import com.example.bankapp.base.view.PresenterActivity;
import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.database.manager.IntroduceFinanceManager;
import com.example.bankapp.modle.LocalMoneyVideo;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.radio.Radio;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加视频
 *
 * @author Guanluocang
 *         created at 2017/12/21 10:36
 */
public class AddVideoView extends PresenterActivity<AddVideoPresenter> implements IAddVideoView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ed_question)
    EditText edQuestion;
    @BindView(R.id.tv_video_answer)
    TextView tvVideoAnswer;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.iv_goback)
    ImageView ivGoback;
    @BindView(R.id.iv_addData)
    ImageView ivAddData;

    private IntroduceFinanceManager introduceFinanceManager;

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_add_video_view;
    }

    @Override
    public AddVideoPresenter createPresenter() {
        return new AddVideoPresenter(this);
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
        introduceFinanceManager = new IntroduceFinanceManager();
        ivAddData.setVisibility(View.GONE);
    }

    /**
     * 选择视频
     */
    public void pickLoacalVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");   //打开文件类型
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    @OnClick({R.id.iv_goback, R.id.tv_add, R.id.tv_video_answer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_goback:
                finish();
                break;
            case R.id.tv_add:
                addLoaclVideo();
                break;
            case R.id.tv_video_answer:
                pickLoacalVideo();
                break;
        }
    }

    /**
     * 添加数据
     */
    private void addLoaclVideo() {
        String name = edQuestion.getText().toString().trim();
        String address = tvVideoAnswer.getText().toString().trim();
        if (!"".equals(name) && !"".equals(address)) {
            introduceFinanceManager.insert(new LocalMoneyVideo(name, address));
            finish();
        } else {
            showToast("输入不能为空");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
                switch (requestCode) {
                    case 1:
                        try {
                            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
                            String[] proj = {MediaStore.Video.Media.DATA};
                            Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
                            Cursor cursor1 = getContentResolver().query(uri, proj, null, null, null);
                            int actual_image_column_index = cursor1.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                            actualimagecursor.moveToFirst();
                            String img_path = actualimagecursor.getString(actual_image_column_index);
//                            File file = new File(img_path);
//                            Uri videoPaht = FileProvider.getUriForFile(AddVideoActivity.this, "com.example.fangfangcustom", file);
                            tvVideoAnswer.setText(img_path.toString());
                        } catch (Exception e) {
                            showToast("请选择正确的视频路径，不要在最近中选择视频文件！");
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;

                }
            }
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

    @Override
    public void doDance() {

    }


}
