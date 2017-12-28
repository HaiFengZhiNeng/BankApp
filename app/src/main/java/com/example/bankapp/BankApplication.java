package com.example.bankapp;

import android.app.Activity;
import android.app.Application;

import com.example.bankapp.database.base.BaseManager;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.youdao.sdk.app.YouDaoApplication;

import java.util.ArrayList;
import java.util.List;

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

    // 保存所有的Activity
    private List<Activity> activityList;

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

    /**
     * 添加activity到activityList集合中
     *
     * @param activity 每一個activity
     */

    public void addActivity(Activity activity) {
        if (activityList == null) {
            activityList = new ArrayList<Activity>();
        }
        activityList.add(activity);
    }

    /**
     * 清空列表，取消引用
     */
    public void clearActivity() {
        activityList.clear();
    }

    /**
     * app退出
     */
    public void exit() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing() && activity != null) {
                activity.finish();
            }
        }
//        clearActivity();
        System.exit(0);
    }
}
