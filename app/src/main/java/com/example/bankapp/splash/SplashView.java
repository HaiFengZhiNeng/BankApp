package com.example.bankapp.splash;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.example.bankapp.R;
import com.example.bankapp.asr.MySpeech;
import com.example.bankapp.base.handler.BaseHandler;
import com.example.bankapp.base.view.PresenterActivity;
import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.main.MainView;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.radio.Radio;
import com.example.bankapp.service.UdpService;
import com.example.bankapp.util.PermissionsChecker;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashView extends AppCompatActivity implements BaseHandler.HandleMessage, ILoginView {

    @BindView(R.id.iv_splash)
    ImageView ivSplash;

    //权限列表
    static final String[] PERMISSIONS = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA
    };

    //权限
    private PermissionsChecker mChecker;
    private static final int PERMISSION_REQUEST_CODE = 0; // 系统权限管理页面的参数
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案

    //handler
    private static final int REFRESH_COMPLETE = 0X153;
    private Handler outHandler = new BaseHandler<>(SplashView.this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_view);
        initView();
        initPermission();
    }


    private void initView() {
        (ivSplash) = findViewById(R.id.iv_splash);
        mChecker = new PermissionsChecker(this);
    }

    private void initPermission() {

        if (mChecker.lacksPermissions(PERMISSIONS)) {
            //请求权限
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
        } else {// 全部权限都已获取
            startAnim();
//                doJump();
        }
    }

    /**
     * 启动动画
     */
    private void startAnim() {
        AlphaAnimation alpha = getAlphaAnimation();
        ivSplash.startAnimation(alpha);
    }

    @NonNull
    private AlphaAnimation getAlphaAnimation() {
        AnimationSet set = new AnimationSet(false); //设置动画集合；
        //缩放动画；
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(2000);
        scale.setFillAfter(true);

        //淡入淡出动画；
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(2000);
        alpha.setFillAfter(true);

        set.addAnimation(scale);
        set.addAnimation(alpha);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashView.this, MainView.class));
                finish();
            }
        });
        return alpha;
    }

    //请求权限结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            startAnim();
        } else {
            outHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);
        }
    }


    //含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case REFRESH_COMPLETE:
                finish();
                break;
        }
    }

    @Override
    public Context getContext() {
        return this;
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

//    public void doJump() {
//
//        Intent intent = new Intent();
//        intent.setClass(SplashView.this, MainView.class);
//        this.startActivity(intent);
//    }


}


