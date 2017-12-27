package com.example.bankapp;

import android.app.Application;

import com.example.bankapp.database.base.BaseManager;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.youdao.sdk.app.YouDaoApplication;

/**
 * @author Guanluocang
 *         created at 2017/12/6 15:06
 */
public class BankApplication extends Application {
    private static BankApplication instance;

    public static BankApplication getInstance() {
        return instance;
    }

    private boolean isAutoAction;

    private String mEngineType;//TYPE_LOCAL

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initXunFei();
        initYouDao();

        BaseManager.initOpenHelper(this);
    }

    /**
     * 讯飞
     */
    public void initXunFei() {
        /**
         * 讯飞初始化
         * 原始app_id = 5a27877d
         */
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=59b8fefd" + "," + SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
    }

    /**
     * 有道翻译
     */
    public void initYouDao() {
        YouDaoApplication.init(this, "4dc7ab44c14b8014");//创建应用，每个应用都会有一个Appid，绑定对应的翻译服务实例，即可使用
    }

    public String getEngineType() {
        return mEngineType;
    }

    public void setEngineType(String engineType) {
        this.mEngineType = engineType;
    }

    public boolean isAutoAction() {
        return isAutoAction;
    }

    public void setAutoAction(boolean autoAction) {
        isAutoAction = autoAction;
    }
}
