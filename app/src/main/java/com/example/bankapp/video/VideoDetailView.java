package com.example.bankapp.video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bankapp.R;
import com.example.bankapp.asr.MySpeech;
import com.example.bankapp.base.view.PresenterActivity;
import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.radio.Radio;
import com.example.bankapp.view.FullScreenVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 视频详情
 *
 * @author Guanluocang
 *         created at 2017/12/22 9:36
 */
public class VideoDetailView extends PresenterActivity<VideoDetailPresenter> implements IVideoDetailView {

    @BindView(R.id.videoView)
    FullScreenVideoView videoView;
    @BindView(R.id.tv_goBack)
    TextView tvGoBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_addData)
    ImageView ivAddData;
    private String upfile;//视频地址

    public static final String ACTION_VIDEO_CLOSE = "com.example.closeVideoDetailActivity";

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_video_detail_view;
    }

    @Override
    public VideoDetailPresenter createPresenter() {
        return new VideoDetailPresenter(this);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        initView();
    }

    private void initView() {
        upfile = getIntent().getStringExtra("url");
        videoView.setVideoPath(upfile);

        //设置视频控制器
        videoView.setMediaController(new MediaController(VideoDetailView.this));

        //播放完成回调
        videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());
        // 开始播放视频
        videoView.start();
    }

    @Override
    public void onResumeVoice() {
        mPresenter.setMySpeech(MySpeech.SPEECH_VOICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(mPresenter.ACTION_OTHER_EXIT);
        registerReceiver(videoDetailReceiver, filter);
    }

    @Override
    public void onPauseReceiver() {
        unregisterReceiver(videoDetailReceiver);
    }

    @Override
    public Context getContext() {
        return this;
    }
    // 创建广播
    private BroadcastReceiver videoDetailReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mPresenter.ACTION_OTHER_EXIT)) {
                onExit();
            }
        }
    };

    @OnClick(R.id.tv_goBack)
    public void onViewClicked() {
        finish();
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(VideoDetailView.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 复写的方法
     * */
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
