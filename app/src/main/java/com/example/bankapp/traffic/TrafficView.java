package com.example.bankapp.traffic;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bankapp.R;
import com.example.bankapp.asr.MySpeech;
import com.example.bankapp.base.presenter.BasePresenter;
import com.example.bankapp.base.view.PresenterActivity;
import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.radio.Radio;
import com.example.bankapp.traffic.record.TrafficRecordView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.huashi.otg.sdk.HSInterface;
import com.huashi.otg.sdk.HsOtgService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 违章查询
 *
 * @author Guanluocang
 *         created at 2017/12/28 19:17
 */
public class TrafficView extends PresenterActivity<TrafficPresenter> implements ITrafficView {

    @BindView(R.id.sam)
    TextView sam;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_trafficName)
    TextView tvTrafficName;
    @BindView(R.id.tv_trafficSex)
    TextView tvTrafficSex;
    @BindView(R.id.tv_trafficNation)
    TextView tvTrafficNation;
    @BindView(R.id.tv_trafficBirthday)
    TextView tvTrafficBirthday;
    @BindView(R.id.tv_trafficAddress)
    TextView tvTrafficAddress;
    @BindView(R.id.tv_confim)
    TextView tvConfim;
    @BindView(R.id.tv_trafficOffic)
    TextView tvTrafficOffic;
    @BindView(R.id.tv_trafficData)
    TextView tvTrafficData;
    @BindView(R.id.tv_trafficFinger)
    TextView tvTrafficFinger;
    @BindView(R.id.ll_infoOne)
    LinearLayout llInfoOne;
    @BindView(R.id.tv_trafficIdCard)
    TextView tvTrafficIdCard;
    //链接
    private MyConn conn;
    private HSInterface HSinterface;
    private Intent iService;
    private String filepath = "";

    private GoogleApiClient client;
    // 判断读取还是去查询
    private int confimNum = 0;

    @Override
    public TrafficPresenter createPresenter() {
        return new TrafficPresenter(this);
    }

    @Override
    public void onResumeVoice() {
        //开启语音
        mPresenter.setEngineType(MySpeech.SPEECH_NULL);
        mPresenter.setMySpeech(MySpeech.SPEECH_VOICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BasePresenter.ACTION_OTHER_EXIT);
        filter.addAction(BasePresenter.ACTION_OTHER_RESULT);
        registerReceiver(businessReceiver, filter);
    }

    @Override
    public void onPauseReceiver() {
        mPresenter.stopRecognizerListener();
        unregisterReceiver(businessReceiver);
    }

    private BroadcastReceiver businessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BasePresenter.ACTION_OTHER_FINISH)) {
                onExit();
            }
        }
    };

    @Override
    protected int getContentViewResource() {
        return R.layout.activity_traffic_view;
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
        filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wltlib";// 授权目录
        iService = new Intent(TrafficView.this, HsOtgService.class);

        mPresenter.doAnswer("请将身份证放在读卡器处，然后点击读取按钮。");
        // 进行链接
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        // 链接
        conn = new MyConn();
        //绑定服务
        bindService(iService, conn, Service.BIND_AUTO_CREATE);
    }

    @OnClick(R.id.tv_confim)
    public void onViewClicked() {
        if (confimNum == 0) {
            // 读取信息
            Intent intent = new Intent();
            intent.setClass(TrafficView.this, TrafficRecordView.class);
            this.startActivity(intent);
            finish();
//            doRead();
        } else {
            // 查询 违章记录
            Intent intent = new Intent();
            intent.setClass(TrafficView.this, TrafficRecordView.class);
            this.startActivity(intent);
            finish();
        }
    }

    // 认证 与读卡

    public void doRead() {
        if (HSinterface == null)
            return;
        int retOne = HSinterface.Authenticate();
        if (retOne == 1) {
            showToast("卡认证成功");
        } else if (retOne == 2) {
            showToast("卡认证失败");
        } else if (retOne == 0) {
            showToast("未连接");
        }


        // 读卡
        if (HSinterface == null)
            return;
        showToast("读卡……");
        ivPhoto.setImageBitmap(null);
        int ret = HSinterface.ReadCard();
        if (ret == 1) {
            //成功
            byte[] fp = new byte[1024];
            fp = HsOtgService.ic.getFpDate();
            String m_FristPFInfo = "";
            String m_SecondPFInfo = "";

            if (fp[4] == (byte) 0x01) {
                m_FristPFInfo = String.format("指纹  信息：第一枚指纹注册成功。指位：%s。指纹质量：%d \n", GetFPcode(fp[5]), fp[6]);
            } else {
                m_FristPFInfo = "身份证无指纹 \n";
            }
            if (fp[512 + 4] == (byte) 0x01) {
                m_SecondPFInfo = String.format("指纹  信息：第二枚指纹注册成功。指位：%s。指纹质量：%d \n", GetFPcode(fp[512 + 5]),
                        fp[512 + 6]);
            } else {
                m_SecondPFInfo = "身份证无指纹 \n";
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");// 设置日期格式

            tvTrafficName.setText(HsOtgService.ic.getPeopleName());//姓名
            tvTrafficSex.setText(HsOtgService.ic.getPeople());//性别：
            tvTrafficBirthday.setText(df.format(HsOtgService.ic.getBirthDay()));//出生日期
            tvTrafficAddress.setText(HsOtgService.ic.getAddr());//地址
            tvTrafficName.setText(HsOtgService.ic.getPeopleName());//身份号码
            tvTrafficOffic.setText(HsOtgService.ic.getDepartment());//签发机关
            tvTrafficData.setText(HsOtgService.ic.getStrartDate() + "-" + HsOtgService.ic.getEndDate());//有效期限
            tvTrafficIdCard.setText(HsOtgService.ic.getIDCard());//身份号码
            tvTrafficFinger.setText("第一枚指纹信息:" + m_FristPFInfo + "\n" + "第二枚指纹信息:" + m_SecondPFInfo);//指纹

            confimNum = 1;
            tvConfim.setText("查询");
            mPresenter.doAnswer("请点击查询按钮进行查询");
            try {
                ret = HSinterface.Unpack();// 照片解码
                if (ret != 0) {// 读卡失败

                    return;
                }
                FileInputStream fis = new FileInputStream(filepath + "/zp.bmp");
                Bitmap bmp = BitmapFactory.decodeStream(fis);
                fis.close();
                ivPhoto.setImageBitmap(bmp);
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "头像不存在！", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "头像读取错误", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "头像解码失败", Toast.LENGTH_SHORT).show();
            } finally {
                HsOtgService.ic = null;
            }
        } else {
            //失败
            showToast("读卡失败");
//            iv_photo.setBackgroundResource(R.mipmap.ic_launcher);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Main Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.example.bankapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);

    }

    @Override
    public void onStop() {
        super.onStop();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Main Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.example.bankapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();

        HSinterface.unInit();
        unbindService(conn);
    }


    @Override
    public void special(String result, SpecialType type) {

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


    class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            HSinterface = (HSInterface) service;
            int i = 2;
            while (i > 0) {
                i--;
                int ret = HSinterface.init();
                if (ret == 1) {
                    i = 0;
                    Log.e("GG", "已连接" + HSinterface.GetSAM());
//                    tv_statu.setText("已连接");
//                    tv_sam.setText(HSinterface.GetSAM()); // 模块号
                } else {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            conn = null;
            HSinterface = null;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    /**
     * 指纹 指位代码
     *
     * @param FPcode
     * @return
     */
    String GetFPcode(int FPcode) {
        switch (FPcode) {
            case 11:
                return "右手拇指";
            case 12:
                return "右手食指";
            case 13:
                return "右手中指";
            case 14:
                return "右手环指";
            case 15:
                return "右手小指";
            case 16:
                return "左手拇指";
            case 17:
                return "左手食指";
            case 18:
                return "左手中指";
            case 19:
                return "左手环指";
            case 20:
                return "左手小指";
            case 97:
                return "右手不确定指位";
            case 98:
                return "左手不确定指位";
            case 99:
                return "其他不确定指位";
            default:
                return "未知";
        }
    }
}
