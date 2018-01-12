package com.example.bankapp.base.presenter;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.example.bankapp.BankApplication;
import com.example.bankapp.R;
import com.example.bankapp.asr.MyAiuiListener;
import com.example.bankapp.asr.MyRecognizerListener;
import com.example.bankapp.asr.MySpeech;
import com.example.bankapp.asr.MySynthesizerListener;
import com.example.bankapp.base.config.Constant;
import com.example.bankapp.base.view.UiView;
import com.example.bankapp.common.Constants;
import com.example.bankapp.common.enums.FollowType;
import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.common.enums.TeleType;
import com.example.bankapp.common.instance.SpeakAiui;
import com.example.bankapp.common.instance.SpeakTts;
import com.example.bankapp.modle.voice.Telephone;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.Flight;
import com.example.bankapp.modle.voice.Joke;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.cmd.Slots;
import com.example.bankapp.modle.voice.constellation.Constellation;
import com.example.bankapp.modle.voice.constellation.Fortune;
import com.example.bankapp.modle.voice.radio.Radio;
import com.example.bankapp.modle.voice.riddle.Riddle;
import com.example.bankapp.modle.voice.stock.Detail;
import com.example.bankapp.modle.voice.stock.Stock;
import com.example.bankapp.modle.voice.story.Story;
import com.example.bankapp.modle.voice.train.Train;
import com.example.bankapp.modle.voice.wordFinding.WordFinding;
import com.example.bankapp.util.AudioUtil;
import com.example.bankapp.util.FucUtil;
import com.example.bankapp.util.MediaPlayerUtil;
import com.example.bankapp.util.PreferencesUtils;
import com.example.bankapp.util.PromptManager;
import com.example.bankapp.util.ReceiveMessage;
import com.example.bankapp.util.SpecialUtils;
import com.example.bankapp.util.tele.TelNumMatch;
import com.example.bankapp.util.tele.TelePhoneUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.util.ResourceUtil;
import com.iflytek.sunflower.FlowerCollector;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.ECVoIPCallManager;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;

import static com.example.bankapp.asr.MySpeech.SPEECH_EXIT;
import static com.example.bankapp.common.Constants.mySpeechType;

/**
 * 控制器的父类
 *
 * @author Guanluocang
 *         created at 2017/12/6 15:14
 */
public abstract class BasePresenter<T extends UiView> implements MySynthesizerListener.SynListener,
        MyAiuiListener.AiListener, MyRecognizerListener.RecognListener {
    /**
     * 是否来电
     */
    protected boolean mIncomingCall = false;
    /**
     * 呼叫唯一标识号
     */
    protected String mCallId;
    /**
     * VoIP呼叫类型（音视频）
     */
    protected ECVoIPCallManager.CallType mCallType;
    /**
     * 通话昵称
     */
    protected String mCallName;
    /**
     * 通话号码
     */
    protected String mCallNumber;
    public AudioManager mAudioManager;

    public static final String ACTION_MAIN_SHOWTEXT = "com.example.bankapp.main.showtext";
    public static final String ACTION_MAIN_SPECIATYPE = "com.example.bankapp.main";
    public static final String ACTION_AIUI_EXIT = "com.example.bankapp.finish";
    public static final String ACTION_OTHER_EXIT = "com.example.bankapp.location.finish";
    public static final String ACTION_OTHER_RESULT = "com.example.bankapp.location";
    public static final String ACTION_OTHER_FINISH = "com.example.bankapp.location";

    //是否正在说话

    public static boolean isTalking = true;
    // 是否可以运动
    public static boolean isSport;

    //语音听写
    private SpeechRecognizer mIat;

    private String mLaguage;
    private boolean isSpeech;

    private MySynthesizerListener synthesizerListener;
    private MyAiuiListener aiuiListener;
    private MyRecognizerListener recognizerListener;


    private FollowType followType;
    private String mOtherText;

    private Handler mHandler = new Handler();

    private int ret = 0;
    private String phoneNumber;

    public static boolean isVoice = true;//是否正在说话

    private boolean isFirst = true;

    //拨打电话
    private TeleType teleType;
    public String TAG = this.getClass().getSimpleName();
    private BaseHandler mBaseHandler;
    protected T mView;

    public BasePresenter(T mView) {
        this.mView = mView;
    }

    /**
     * 创建ContentView之前调用
     */
    public void onViewCreateBefore() {
    }

    /**
     * ContentView创建之后调用
     */
    public void onViewCreated() {
        init();
    }

    /**
     * 同 Activity onCreate
     *
     * @param saveInstanceState
     */
    public void onCreate(Bundle saveInstanceState) {
        initVideo();
    }

    /**
     * 同 Activity onResume
     */
    public void onResume() {
        isSport = true;
    }

    /**
     * 同 Activity onPause
     */
    public void onPause() {

        isSport = false;
    }

    /**
     * 同 Activity onRestart
     */
    protected void onRestart() {
    }

    /**
     * 同 Activity onStart
     */
    protected void onStart() {
    }

    /**
     * 同 Activity onStop
     */
    public void onStop() {
    }

    /**
     * 同 Activity onDestroy
     */
    public void onDestroy() {
    }

    /**
     * 返回键处理
     *
     * @return true 程序将继续执行Activity里面的onBackPressed ，为false不执行
     */
    public boolean onBackPressed() {
        return true;
    }


    /**
     * 初始化一个Handler，如果需要使用Handler，先调用此方法，
     * 然后可以使用postRunnable(Runnable runnable)，
     * sendMessage在handleMessage（Message msg）中接收msg
     */
    public void initHandler() {
        mBaseHandler = new BaseHandler(this);
    }

    /**
     * 返回Handler，在此之前确定已经调用initHandler（）
     *
     * @return Handler
     */
    public Handler getHandler() {
        return mBaseHandler;
    }

    /**
     * 同Handler的postRunnable
     * 在此之前确定已经调用initHandler（）
     */
    protected void postRunnable(Runnable runnable) {
        postRunnableDelayed(runnable, 0);
    }

    /**
     * 同Handler的postRunnableDelayed
     * 在此之前确定已经调用initHandler（）
     */
    protected void postRunnableDelayed(Runnable runnable, long delayMillis) {
        if (mBaseHandler == null) initHandler();
        mBaseHandler.postDelayed(runnable, delayMillis);
    }

    /**
     * 得到Context
     *
     * @return
     */

    public Context getContext() {
        return mView.getContext();
    }


    /**
     * startActivityForResult的返回结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    /**
     * @param mIntent
     * @param resultCode
     */
    public void setResult(Intent mIntent, int resultCode) {
        ((Activity) getContext()).setResult(resultCode, mIntent);
    }

    /**
     * 开启一个服务
     *
     * @param mService
     */
    public void startService(Service mService) {
        startService(mService.getClass());
    }

    /**
     * 开启一个服务
     *
     * @param clazz 需要启动的Activity的class文件
     */
    public void startService(Class<? extends Service> clazz) {
        getContext().startService(new Intent(getContext(), clazz));
    }


    /**
     * 启动一个Activity
     *
     * @param clazz 需要启动的Activity的class文件
     */
    public void startActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(getContext(), clazz);
        getContext().startActivity(intent);
    }

    public void startActivity(Class<? extends Activity> clazz, Bundle mBundle) {
        Intent intent = new Intent(getContext(), clazz);
        intent.putExtras(mBundle);
        getContext().startActivity(intent);
    }

    /**
     * 启动一个Activity
     *
     * @param
     */
    public void startActivity(Intent mIntent) {
        getContext().startActivity(mIntent);
    }

    public void startActivityForResult(Intent mIntent, int requestCode) {
        ((Activity) getContext()).startActivityForResult(mIntent, requestCode);
    }

    /**
     * 显示Toast
     *
     * @param toastId
     */
    public void showToast(int toastId) {
        PromptManager.showToast(getContext(), toastId);
    }

    /**
     * 显示Toast
     *
     * @param text
     */
    public void showToast(String text) {
        PromptManager.showToast(getContext(), text);
    }

    /**
     * 显示Toast
     *
     * @param toastId
     */
    public void showToast(int toastId, int duration) {
        PromptManager.showToast(getContext(), toastId, duration);
    }

    /**
     * 显示Toast
     *
     * @param text
     */
    public void showToast(String text, int duration) {
        PromptManager.showToast(getContext(), text, duration);
    }


    /**
     * 同Handler 的 handleMessage，
     * getHandler.sendMessage,发送的Message在此接收
     * 在此之前确定已经调用initHandler（）
     *
     * @param msg
     */
    protected void handleMessage(Message msg) {
    }

    protected static class BaseHandler extends Handler {
        private final WeakReference<BasePresenter> mObjects;

        public BaseHandler(BasePresenter mPresenter) {
            mObjects = new WeakReference<BasePresenter>(mPresenter);
        }

        @Override
        public void handleMessage(Message msg) {
            BasePresenter mPresenter = mObjects.get();
            if (mPresenter != null)
                mPresenter.handleMessage(msg);
        }
    }

    public void initVideo() {
        mAudioManager = ((AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE));
        if (initVideoView()) {
            return;
        }
        if (mCallType == null) {
            mCallType = ECVoIPCallManager.CallType.VOICE;
        }
    }

    protected void exit() {
        if (mView != null)
            ((Activity) mView).finish();
    }


    public boolean initVideoView() {
        mIncomingCall = !((Activity) mView).getIntent().getBooleanExtra(Constant.ACTION_CALLBACKING, false);
        mCallType = (ECVoIPCallManager.CallType) ((Activity) mView).getIntent().getSerializableExtra(ECDevice.CALLTYPE);
        return false;
    }

    public void init() {

        synthesizerListener = new MySynthesizerListener(this);
        aiuiListener = new MyAiuiListener((Activity) mView.getContext(), this);
        recognizerListener = new MyRecognizerListener(this);
        if (isFirst) {
            setEngineType(SpeechConstant.TYPE_CLOUD);
            initTts();
            initAiui();
            initIat();
            buildIat();
            isFirst = false;
        }

        isSpeech = true;
        followType = FollowType.End;
    }

    //初始化
    public void initTts() {
        SpeakTts.getInstance().initTts();
    }

    //初始化AIUI
    public void initAiui() {
        SpeakAiui.getInstance().initAiui(aiuiListener);
    }

    //初始化 听写
    public void initIat() {
        mIat = SpeechRecognizer.createRecognizer(mView.getContext(), new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    showToast("初始化失败，错误码：" + code);
                }
            }
        });
    }


    private void setIatparameter() {
        String engineType = BankApplication.getInstance().getEngineType();
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置识别引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, engineType);
        if (engineType.equals(SpeechConstant.TYPE_CLOUD)) {
//            mIat.setParameter(SpeechConstant.CLOUD_GRAMMAR, grammarId);
//            String contents = FucUtil.readFile(mSoundView.getContext(), "userwords", "utf-8");
//            updateLocation("k0", contents, true);
        } else if (engineType.equals(SpeechConstant.TYPE_LOCAL)) {
            // 设置本地识别资源
//            mIat.setParameter(ResourceUtil.ASR_RES_PATH, getResAsrPath());
            // 设置语法构建路径
//            mIat.setParameter(ResourceUtil.GRM_BUILD_PATH, Constants.GRM_PATH);
            // 设置返回结果格式
            // 设置本地识别使用语法id
//            mIat.setParameter(SpeechConstant.LOCAL_GRAMMAR, "bank");
            // 设置识别的门限值
//            mIat.setParameter(SpeechConstant.MIXED_THRESHOLD, "30");
        }

        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        mLaguage = PreferencesUtils.getString(mView.getContext(), Constants.IAT_LANGUAGE_PREFERENCE, "mandarin");
        if (mLaguage.equals("en_us")) {
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);
        } else if (mLaguage.equals("cantonese")) {
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            mIat.setParameter(SpeechConstant.ACCENT, mLaguage);
        } else {
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            mIat.setParameter(SpeechConstant.ACCENT, mLaguage);
        }
        mIat.setParameter(SpeechConstant.VAD_BOS, "99000");
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
        FlowerCollector.onEvent(mView.getContext(), "iat_recognize");

        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    private void initIatFinish() {
        if (PreferencesUtils.getBoolean(mView.getContext(), Constants.IAT_LOCAL_BUILD, false) &&
                PreferencesUtils.getBoolean(mView.getContext(), Constants.IAT_CLOUD_BUILD, false &&
                        PreferencesUtils.getBoolean(mView.getContext(), Constants.IAT_LOCAL_UPDATELEXICON, false))) {
            startRecognizerListener();
        }
    }


    private String getResAsrPath() {
        StringBuffer tempBuffer = new StringBuffer();
        //识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(mView.getContext(), ResourceUtil.RESOURCE_TYPE.assets, "asr/common.jet"));
        //识别8k资源-使用8k的时候请解开注释
//		tempBuffer.append(";");
//		tempBuffer.append(ResourceUtil.generateResourcePath(this, RESOURCE_TYPE.assets, "asr/common_8k.jet"));
        return tempBuffer.toString();
    }

    public void setEngineType(String type) {
        BankApplication.getInstance().setEngineType(type);
    }

    public void updateLocation(String lexiconName, String lexiconContents) {
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置引擎类型
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        // 指定资源路径
        mIat.setParameter(ResourceUtil.ASR_RES_PATH, getResAsrPath());
        // 指定语法路径
        mIat.setParameter(ResourceUtil.GRM_BUILD_PATH, Constants.GRM_PATH);
        // 指定语法名字
        mIat.setParameter(SpeechConstant.GRAMMAR_LIST, "bank");
        // 设置文本编码格式
        mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        // lexiconName 为词典名字，lexiconContents 为词典内容，lexiconListener 为回调监听器
        int ret = mIat.updateLexicon(lexiconName, lexiconContents, new LexiconListener() {
            @Override
            public void onLexiconUpdated(String lexiconId, SpeechError error) {
                if (error == null) {
                    Log.e("GG", "词典更新成功");
                    PreferencesUtils.putBoolean(mView.getContext(), Constants.IAT_LOCAL_UPDATELEXICON, true);

                    BankApplication.getInstance().setEngineType(SpeechConstant.TYPE_CLOUD);
                    if (!PreferencesUtils.getBoolean(mView.getContext(), Constants.IAT_CLOUD_BUILD, false)) {
                        buildIat();
                    }
                    initIatFinish();
                } else {
                    Log.e("GG", "词典更新失败,错误码：" + error.getErrorCode());
                }
            }
        });
        if (ret != ErrorCode.SUCCESS) {
            Log.e("GG", "更新词典失败,错误码：" + ret);
        }

    }

    public void buildIat() {
        //先构建在线的，在线构建完成后构建本地的
        final String engineType = BankApplication.getInstance().getEngineType();

        mIat.setParameter(SpeechConstant.PARAMS, null);
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, engineType);
        mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        String content = null;
        String type = null;
        boolean isBuild = false;

        if (engineType.equals(SpeechConstant.TYPE_CLOUD)) {
            isBuild = PreferencesUtils.getBoolean(mView.getContext(), Constants.IAT_CLOUD_BUILD, false);
            if (isBuild) {
                isBuild = PreferencesUtils.getBoolean(mView.getContext(), Constants.IAT_LOCAL_BUILD, false);
            }
//            content = new String(FucUtil.readFile(mView.getContext(), "bank.abnf", "utf-8"));
//            type = "abnf";
        } else if (engineType.equals(SpeechConstant.TYPE_LOCAL)) {
            // 设置本地识别资源
//            mIat.setParameter(ResourceUtil.ASR_RES_PATH, getResAsrPath());
            // 设置语法构建路径
//            mIat.setParameter(ResourceUtil.GRM_BUILD_PATH, Constants.GRM_PATH);
            // 设置本地识别使用语法id
//            mIat.setParameter(SpeechConstant.LOCAL_GRAMMAR, "bank");
            // 设置识别的门限值
//            mIat.setParameter(SpeechConstant.MIXED_THRESHOLD, "30");

//            isBuild = PreferencesUtils.getBoolean(mView.getContext(), Constants.IAT_LOCAL_BUILD, false);
//            if (isBuild) {
//                isBuild = PreferencesUtils.getBoolean(mView.getContext(), Constants.IAT_CLOUD_BUILD, false);
//            }
//            content = new String(FucUtil.readFile(mView.getContext(), "bank.bnf", "utf-8"));
//            type = "bnf";
        }
        if (!isBuild) {
            ret = mIat.buildGrammar(type, content, new GrammarListener() {
                @Override
                public void onBuildFinish(String grammarId, SpeechError error) {
                    if (error == null) {
//                        if (engineType.equals(SpeechConstant.TYPE_LOCAL)) {
//                            Log.e("GG", "本地语法构建成功：" + grammarId);
//                            PreferencesUtils.putBoolean(mView.getContext(), Constants.IAT_LOCAL_BUILD, true);
//
//                            if (!PreferencesUtils.getBoolean(mView.getContext(), Constants.IAT_LOCAL_UPDATELEXICON, false)) {
////                                String contents = FucUtil.readFile(mSoundView.getContext(), "local_userwords", "utf-8").replace(" ", "");
//                                String[] arrResult = resArray(R.array.local_voice_question);
//                                StringBuffer sb = new StringBuffer();
//                                for (int i = 0; i < arrResult.length; i++) {
//                                    sb.append(arrResult[i] + "\n");
//                                }
//                                String[] arrStandard = resArray(R.array.local_standard);
//                                for (int i = 0; i < arrStandard.length; i++) {
//                                    sb.append(arrStandard[i] + "\n");
//                                }
//                                updateLocation("voice", sb.toString());
//                            } else {
//                                if (!PreferencesUtils.getBoolean(mView.getContext(), Constants.IAT_CLOUD_BUILD, false)) {
//                                    initIat();
//                                }
//                            }
//                        } else
                        if (engineType.equals(SpeechConstant.TYPE_CLOUD)) {
                            Log.e("GG", "在线语法构建成功：" + grammarId);
                            PreferencesUtils.putBoolean(mView.getContext(), Constants.IAT_CLOUD_BUILD, true);
                            boolean isBuild = PreferencesUtils.getBoolean(mView.getContext(), Constants.IAT_LOCAL_BUILD, false);
//                            BankApplication.getInstance().setEngineType(SpeechConstant.TYPE_LOCAL);
                            if (!isBuild) {
                                initIat();
                            }
                        }
                        initIatFinish();
                    } else {
                        Log.e("GG", "语法构建失败,错误码：" + error.getErrorCode());
                    }
                }
            });
            if (ret != ErrorCode.SUCCESS) {
                Log.e("GG", "语法构建失败,错误码：" + ret);
            }
        } else {
            startRecognizerListener();
        }

    }

    //开始语音监听
    public void judgeState() {
        isTalking = true;
        if (isVoice) {//为true 可以进行监听说话
            startRecognizerListener();
        }
    }

    //设置监听的类型
    public void setMySpeech(String speechAiui) {
        Constants.mySpeechType = speechAiui;
    }

    //停止语音合成
    public void stopTts() {
        SpeakTts.getInstance().stopTts();
    }


    //返回问题以及答案
    public void doAnswer(String answer) {
//        Intent intent = new Intent(ACTION_MAIN_SHOWTEXT);
//        if (!"".equals(question)) {
//            intent.putExtra("question", question);
//        }
//        intent.putExtra("showText", answer);
//        ((Activity) mView).sendBroadcast(intent);

        //播放答案
        SpeakTts.getInstance().doAnswer(answer, synthesizerListener);

    }

    //开启语音监听
    public void startRecognizerListener() {
        setIatparameter();
        mIat.startListening(recognizerListener);
    }

    //关闭语音监听
    public void stopRecognizerListener() {
        mIat.startListening(null);
        mIat.stopListening();
    }

    //是否开启语音监听
    public void doVoiceSwitch(boolean b) {
        isSpeech = b;
        if (isSpeech) {
            judgeState();
        } else {
            stopRecognizerListener();
        }
    }

    //AIUI 转义
    public void aiuiWriteText(String text) {
        SpeakAiui.getInstance().aiuiWriteText(text);
    }

    //设置发音人
    public void setSpokesman(String spokesman) {
        SpeakTts.getInstance().setSpokesman(spokesman);
    }

    //停止所有
    public void stopAll() {
        followType = FollowType.End;
        isTalking = false;
        stopTts();
        doAnswer(resFoFinal(R.array.wake_up));
    }

    public void stopAllForDance() {
        followType = FollowType.End;
        isTalking = false;
        stopTts();
        stopRecognizerListener();
    }

    public void stopHandler() {
        mHandler.removeCallbacks(runnable);
    }

    // 播放音频
    public void playVoice(String url) {
        if (TextUtils.isEmpty(url))
            return;
        Log.e(TAG, "开始播放视频... : " + url);
        MediaPlayerUtil.getInstance().playMusic(url, new MediaPlayerUtil.OnMusicCompletionListener() {
            @Override
            public void onCompletion(boolean isPlaySuccess) {
                onCompleted();
            }

            @Override
            public void onPrepare() {
                Log.e(TAG, "onPrepare music ... ");
            }
        });
    }

    /*********************************************************************************************/
    // 语音合成完毕
    @Override
    public void onCompleted() {

        Log.e(TAG, "onCompleted");
        mHandler.postDelayed(runnable, 1000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (followType == FollowType.Start) {
                followType = FollowType.Conduct;
                onDoAnswer(null, mOtherText);
            } else if (followType == FollowType.Conduct) {
                followType = FollowType.End;
                onResult(mOtherText);
                mOtherText = null;
            } else if (followType == FollowType.End) {
                isTalking = false;
                if (isSpeech) {
                    judgeState();
                }
            }
        }
    };

    @Override
    public void onSpeakBegin() {
        Log.e("GG", "onSpeakBegin");
        isTalking = false;
        stopRecognizerListener();
    }

    //普通问答
    @Override
    public void onDoAnswer(String question, String finalText) {
        if ("".equals(finalText) || finalText == null) {
            onCompleted();
        } else {
            mView.doAiuiAnwer(finalText);
            mView.refHomePage(question, finalText);
        }
    }

    //新闻
    @Override
    public void onDoAnswer(String question, String text, News news) {
//        mView.doAiuiAnwer(text + ", " + news.getContent());
//        mView.refHomePage(question, news);
        if (TextUtils.isEmpty(news.getUrl())) {
            mView.doAiuiAnwer(text + ", " + news.getContent());
            mView.refHomePage(question, news);
        } else {
            mView.refHomePage(question, news);
            playVoice(news.getUrl());
        }
    }

    //菜谱
    @Override
    public void onDoAnswer(String question, String text, Cookbook cookbook) {
        mView.doAiuiAnwer(text + ", " + cookbook.getSteps());
        mView.refHomePage(question, cookbook);
    }

    //古诗
    @Override
    public void onDoAnswer(String question, Poetry poetry) {
        mView.doAiuiAnwer(poetry.getContent());
        mView.refHomePage(question, poetry);
    }

    //讲笑话
    @Override
    public void onDoAnswer(String question, String finalText, Joke joke) {
//        mView.special(question, SpecialType.Joke);
//        playVoice(joke.getMp3Url());
        if (TextUtils.isEmpty(joke.getMp3Url())) {
            mView.doAiuiAnwer(joke.getTitle() + " : " + joke.getContent());
            mView.refHomePage(question, joke.getTitle() + " : " + joke.getContent());
        } else {
            mView.refHomePage(question, finalText);
            playVoice(joke.getMp3Url());
        }
    }

    //讲故事
    @Override
    public void onDoAnswer(String question, String finalText, Story story) {
//        playVoice(story.getPlayUrl());
        if (TextUtils.isEmpty(story.getPlayUrl())) {
            mView.doAiuiAnwer(story.getName() + " : " + story.getAuthor());
            mView.refHomePage(question, story.getName() + " : " + story.getSeries());
        } else {
            mView.refHomePage(question, finalText);
            playVoice(story.getPlayUrl());
        }
    }

    //火车
    @Override
    public void onDoAnswer(String question, String finalText, List<Train> trains, Train train0) {
        mView.doAiuiAnwer(finalText);
//        mView.refHomePage(question, finalText);
//        for (int i = 0; i < trains.size(); i++) {
        Train train = trains.get(0);
        mView.refHomePage(question, train.getEndtime_for_voice() + "的" + train.getTrainType() + " " + train.getTrainNo() + "" +
                " " + train.getOriginStation() + " -- " + train.getTerminalStation()
                + " , 运行时间：" + train.getRunTime());
//        }
    }

    //航班
    @Override
    public void onDoAnswer(String question, String finalText, List<Flight> flights, Flight flight0) {
//        int total;
//        if (flights.size() < 10) {
//            total = flights.size();
//        } else {
//            total = 10;
//        }
        Flight flight = flights.get(0);
        mView.refHomePage(question, flight.getStarttime_for_voice() + "从" + flight.getDepartCity() + "出发， "
                + flight.getEndtime_for_voice() + "到达" + flight.getArriveCity() + ", " +
                flight.getCabinInfo() + "价格是：" + flight.getPrice());
        mView.doAiuiAnwer(finalText);
//        mView.refHomePage(question, finalText);
    }

    //每日学英语
    @Override
    public void onDoAnswer(String question, String finalText, EnglishEveryday englishEveryday) {
        mView.doAiuiAnwer(englishEveryday.getTranslation() + englishEveryday.getContent());
        mView.refHomePage(question, englishEveryday.getContent() + englishEveryday.getTranslation());
    }

    //星座
    @Override
    public void onDoAnswer(String question, String finalText, Constellation constellation) {
        StringBuffer sb = new StringBuffer();
        List<Fortune> fortunes = constellation.getFortune();
//        sb.append(finalText);
        for (int i = 0; i < fortunes.size(); i++) {
            Fortune fortune = fortunes.get(i);
            sb.append(fortune.getName() + " : " + fortune.getDescription());
        }
        mView.doAiuiAnwer(sb.toString());
        mView.refHomePage(question, sb.toString());
    }

    //股票
    @Override
    public void onDoAnswer(String question, String finalText, Stock stock) {
        StringBuffer sb = new StringBuffer();
        sb.append(finalText);
        sb.append("\n截止到" + stock.getUpdateDateTime() + ", " + stock.getName() + " " + stock.getStockCode() +
                ", 当前价格为 ： " + stock.getOpeningPrice() + ", 上升率为 ： " + stock.getRiseRate() +
                " 详情请查看列表信息");
        mView.doAiuiAnwer(sb.toString());
        sb.append("\n最高价 ： " + stock.getHighPrice());
        sb.append("  最低价 ： " + stock.getLowPrice());
        List<Detail> details = stock.getDetail();
        for (int i = 0; i < details.size(); i++) {
            Detail detail = details.get(i);
            sb.append("\n" + detail.getCount() + " " + detail.getRole() + " " + detail.getPrice());
        }

        mView.refHomePage(question, sb.toString());
    }

    //谜语
    @Override
    public void onDoAnswer(String question, String finalText, Riddle riddle) {
        mView.doAiuiAnwer(riddle.getTitle() + "答案：" + riddle.getAnswer());
        mView.refHomePage(question, riddle.getTitle() + "\n答案：\n" + riddle.getAnswer() + "\n");
    }

    // 反义词
    @Override
    public void onDoAnswer(String question, String finalText, WordFinding wordFinding) {
        List<String> results;
        int count = 5;
        StringBuffer sb = new StringBuffer();
        if (finalText.contains("反义词")) {
            results = wordFinding.getAntonym();
        } else {
            results = wordFinding.getSynonym();
        }
        if (results.size() > count) {
            int random = new Random().nextInt(results.size() - count);
            for (int i = 0; i < count; i++) {
                sb.append("\n" + results.get(random + i));
            }
        } else {
            for (int i = 0; i < results.size(); i++) {
                sb.append("\n" + results.get(i));
            }
        }
        mView.doAiuiAnwer(sb.toString());
        mView.refHomePage(question, sb.toString());
    }

    //拨打电话
    @Override
    public void onDoDial(String question, String value) {
        if (TelNumMatch.matchNum(value) == 5 || TelNumMatch.matchNum(value) == 4) {
            List<Telephone> telephones = TelePhoneUtils.queryContacts(mView.getContext(), value);
            if (telephones != null && telephones.size() > 0) {
                if (telephones.size() == 1) {
                    teleType = TeleType.GetReadyPhone;
                    List<String> phones = telephones.get(0).getPhone();
                    if (phones != null && phones.size() > 0) {
                        if (phones.size() == 1) {
                            teleType = TeleType.TelePhone;
                            phoneNumber = phones.get(0);
                            mView.doAiuiAnwer("为您拨打 ： " + phoneNumber);
//                            mView.refHomePage(question, "为您拨打 ： " + phoneNumber);
                            mView.doCallPhone(phoneNumber);
                        } else {
                            mView.doAiuiAnwer("为您找到如下号码 ： ");
                            mView.refHomePage(question, "为您找到如下号码 ： ");
                            for (String phone : phones) {
                                mView.refHomePage(null, phone);
                            }
                        }
                    } else {
                        teleType = TeleType.TeleDefault;
                        mView.doAiuiAnwer("暂无此名字电话号码");
                        mView.refHomePage(question, "通讯录中暂无");
                    }
                } else {
                    teleType = TeleType.GetReadyName;
                    mView.doAiuiAnwer("为您匹配到如下姓名 ： ");
                    mView.refHomePage(question, "为您匹配到如下姓名 ： ");
                    for (Telephone telephone : telephones) {
                        mView.refHomePage(null, telephone.getName());
                    }
                }
            } else {
                teleType = TeleType.TeleDefault;
                mView.doAiuiAnwer("通讯录中暂无" + value);
                mView.refHomePage(question, "通讯录中暂无" + value);
            }
        } else {
            mView.doAiuiAnwer("为您拨打 ： " + phoneNumber);
            teleType = TeleType.TelePhone;
            phoneNumber = value;
            mView.doCallPhone(phoneNumber);
            mView.refHomePage(question, "为您拨打 ： " + phoneNumber);
        }
    }

    //没有结果
    @Override
    public void onNoAnswer(String question, String finalText, String otherText) {
        mOtherText = otherText;
        followType = FollowType.Start;
        onDoAnswer(question, finalText);
    }

    //无线收音机
    @Override
    public void onDoAnswer(String question, String finalText, Radio radio) {
        if (TextUtils.isEmpty(radio.getUrl())) {
            mView.refHomePage(question, radio);
            playVoice(radio.getUrl());
        } else {
            stopRecognizerListener();
            mView.special(question, SpecialType.Joke);
        }
    }

    //音乐
    @Override
    public void onMusic(String question, String finalText) {
        mOtherText = "音乐播放中...";
        followType = FollowType.Conduct;
        onDoAnswer(question, finalText);
    }

    //翻译
    @Override
    public void onTranslation(String question, String value) {
        onDoAnswer(question, value);
    }

    // 调节音量
    @Override
    public void onDoAnswer(String question, Slots slotsCmd) {
        int volume = AudioUtil.getInstance(mView.getContext()).getMediaVolume();
        int maxVolume = AudioUtil.getInstance(mView.getContext()).getMediaMaxVolume();
        int node = maxVolume / 5;
        String answer = "不支持此音量控制";
        if (slotsCmd.getName().equals("insType")) {
            if (slotsCmd.getValue().equals("volume_plus")) {
                if (volume == maxVolume) {
                    answer = "当前已是最大音量了";
                } else {
                    answer = "已增大音量";
                    volume = volume + node;
                    if (volume > maxVolume) {
                        volume = maxVolume;
                    }
                    AudioUtil.getInstance(mView.getContext()).setMediaVolume(volume);
                }
            } else if (slotsCmd.getValue().equals("volume_minus")) {
                if (volume == 0) {
                    answer = "当前已是最小音量了";
                } else {
                    answer = "已减小音量";
                    volume = volume - node;
                    if (volume < 0) {
                        volume = 0;
                    }
                    AudioUtil.getInstance(mView.getContext()).setMediaVolume(volume);
                }
            } else if (slotsCmd.getValue().equals("unmute")) {
                answer = "您可以说 “增大音量” 或 “减小音量” ，我会帮您改变的";
            }
        }

        onDoAnswer(question, answer);
    }


    @Override
    public void onError() {
        if (Constants.mySpeechType.equals(MySpeech.SPEECH_AIUI)) {
            initAiui();
        }
    }

    @Override
    public void onAIUIDowm() {
        Log.e("GG", "onaiuidown");
    }

    public String getMySpeech() {
        return mySpeechType;
    }

    //听写结果
    @Override
    public void onResult(String result) {
        Log.e("gg", "result " + result);
        if (followType == FollowType.Start) {
            if (Constants.mySpeechType.equals(MySpeech.SPEECH_VOICE)) {
                followType = FollowType.End;
            }
        }
        if (Constants.mySpeechType.equals(MySpeech.SPEECH_AIUI)) {
            SpecialType specialType = SpecialUtils.doesExist(((Activity) mView), result);
            if (specialType == SpecialType.NoSpecial) {
                aiuiWriteText(result);
            } else if (specialType == SpecialType.Music) {
                stopRecognizerListener();
                mView.special(result, SpecialType.Music);
            } else if (specialType == SpecialType.Story) {
                stopRecognizerListener();
                mView.special(result, SpecialType.Story);
            } else if (specialType == SpecialType.Joke) {
//                startTextNlp("笑话");
                stopRecognizerListener();
                mView.special(result, SpecialType.Joke);
            } else if (specialType == SpecialType.Dance) {
                mView.doDance();
            } else if (specialType == SpecialType.WakeUp) {
                doAnswer(resFoFinal(R.array.wake_up));
            } else if (specialType == SpecialType.Forward || specialType == SpecialType.Backoff ||
                    specialType == SpecialType.Turnleft || specialType == SpecialType.Turnright) {
                mView.spakeMove(specialType, result);
            } else if (specialType == SpecialType.BusinessService || specialType == SpecialType.CostService ||
                    specialType == SpecialType.RegisterService || specialType == SpecialType.IllegalrService ||
                    specialType == SpecialType.IdentityService || specialType == SpecialType.LocalVoice || specialType == SpecialType.MoneyService) {

                Intent intent = new Intent(ACTION_MAIN_SPECIATYPE);
                Bundle bundle = new Bundle();
                bundle.putSerializable("specialType", specialType);
                intent.putExtras(bundle);
                ((Activity) mView).sendBroadcast(intent);
            } else if (specialType == SpecialType.Exit) {
//                mView.onExit();
                Intent intent = new Intent(ACTION_AIUI_EXIT);
                ((Activity) mView).sendBroadcast(intent);
            }
        } else if (Constants.mySpeechType.equals(MySpeech.SPEECH_VOICE)) {
            Log.e(TAG, result);
            //result.
            String voice_result = result.replaceAll("[\\p{Punct}\\p{Space}]+", "");//去掉听写中的标点符号

            SpecialType specialType = SpecialUtils.doesExistVoice(((Activity) mView), voice_result.trim());
            if (specialType == SpecialType.Exit) {
//                mView.onExit();
                Intent intent = new Intent(ACTION_OTHER_EXIT);
                ((Activity) mView).sendBroadcast(intent);
            } else {
                Intent intent = new Intent(ACTION_OTHER_RESULT);
                intent.putExtra("result", voice_result);
                ((Activity) mView).sendBroadcast(intent);
            }
        } else if (Constants.mySpeechType.equals(SPEECH_EXIT)) {
            SpecialType specialType = SpecialUtils.doesExist(((Activity) mView), result);
            if (specialType == SpecialType.Exit) {
                Intent intent = new Intent(ACTION_OTHER_FINISH);
                ((Activity) mView).sendBroadcast(intent);
            } else {
                startRecognizerListener();
            }
        }
    }

    private String resFoFinal(int id) {
        String[] arrResult = ((Activity) mView).getResources().getStringArray(id);
        return arrResult[new Random().nextInt(arrResult.length)];
    }


    @Override
    public void onErrInfo() {
//        SpeakIat.getInstance().onError(recognizerListener);
        if (mIat == null) {
            initIat();
        } else {
            startRecognizerListener();
        }
    }

    @Override
    public void onRecognDown() {
//        SpeakIat.getInstance().onError(recognizerListener);
        if (mIat == null) {
            initIat();
        } else {
            startRecognizerListener();
        }
    }

    @Override
    public void onNetwork() {
//        SpeakIat.getInstance().onError(recognizerListener);
        if (mIat == null) {
            initIat();
        } else {
            startRecognizerListener();
        }
    }

    //欢迎词
    public void sayHellow() {
        followType = FollowType.End;
        isTalking = false;
        stopTts();
        doAnswer(resFoFinal(R.array.say_hellow));
    }

    private String[] resArray(int resId) {
        return mView.getContext().getResources().getStringArray(resId);
    }

    //上传热词
    public void uploadUserwords() {
        String contents = FucUtil.readFile(BankApplication.getInstance(), "userwords", "utf-8");

        // 指定引擎类型
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 置编码类型
        mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        ret = mIat.updateLexicon("userword", contents, mLexiconListener);
        if (ret != ErrorCode.SUCCESS)

            Log.e("WWDZ", "上传热词失败,错误码：");
    }

    /**
     * 上传联系人/词表监听器。
     */
    private LexiconListener mLexiconListener = new LexiconListener() {

        @Override
        public void onLexiconUpdated(String lexiconId, SpeechError error) {
            if (error != null) {
                Log.e("WWDZ", "上传热词error：" + error.toString());

            } else {
                Log.e("WWDZ", "上传热词成功");
            }
        }
    };
}
