package com.example.bankapp.udp.listener;

/**
 * Created by zhangyuanyuan on 2017/10/20.
 */

public interface OnListenerUDPServer {

    void receiver(String receiver);

    void acquireIp(boolean isAcquire);

    void onStart(String myIp, int myPort);

}
