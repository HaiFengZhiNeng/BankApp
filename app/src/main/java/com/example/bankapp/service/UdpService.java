package com.example.bankapp.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.bankapp.base.config.Constant;
import com.example.bankapp.udp.NetClient;
import com.example.bankapp.udp.listener.OnListenerUDPServer;
import com.example.bankapp.udp.listener.UdpReceiver;
import com.example.bankapp.util.L;


/**
 * Created by zhangyuanyuan on 2017/10/20.
 */

public class UdpService extends Service implements OnListenerUDPServer {

    private LocalBroadcastManager mLbmManager;

    private NetClient netClient;

    /**
     * 接受发送的广播
     */
    private UdpSendReceiver mUdpSendReceiver;

    private Handler mHandler = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mLbmManager = LocalBroadcastManager.getInstance(this);

        netClient = NetClient.getInstance(this);

        registerUdp();

        mUdpSendReceiver = new UdpSendReceiver();
        IntentFilter filter = new IntentFilter(Constant.UDP_SEND_ACTION);
        mLbmManager.registerReceiver(mUdpSendReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        netClient.unregisterUdpServer();
        mLbmManager.unregisterReceiver(mUdpSendReceiver);
    }


    private void registerUdp() {
        netClient.registerUdpServer(new UdpReceiver(this));
//        netClient.registerUdpClient(new UdpReceiver(this));
    }


    /**
     * 显示toast
     *
     * @param resId
     */
    public void showToast(final int resId) {
        showToast(getString(resId));
    }

    /**
     * 显示toast
     *
     * @param resStr
     * @return Toast对象，便于控制toast的显示与关闭
     */
    public void showToast(final String resStr) {

        if (TextUtils.isEmpty(resStr)) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(UdpService.this, resStr, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


    private void sendLocal(String content) {
        Intent intent = new Intent(Constant.UDP_ACCEPT_ACTION);
        intent.putExtra("content", content);
        mLbmManager.sendBroadcast(intent);
    }

    @Override
    public void receiver(String receiver) {
        L.e("GG", "udp : " + receiver);
        sendLocal(receiver);
    }

    @Override
    public void acquireIp(boolean isAcquire) {
        L.e("GG", "udp isAcquire: " + isAcquire);
    }

    @Override
    public void onStart(String myIp, int myPort) {
        if (Constant.IP == null) {
            Constant.IP = myIp;
        }
        Constant.PORT = myPort;
        L.e("GG", "udp初始化完成");
        sendLocal("udp初始化完成");
    }

    private class UdpSendReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            showToast("activity发过来的数据");
            String information = intent.getStringExtra("controlSend");
            if (information != null) {
                netClient.sendTextMessageByUdp(information);
            }
        }
    }

}
