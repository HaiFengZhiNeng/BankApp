package com.example.bankapp.udp;



import com.example.bankapp.udp.listener.UdpServerListener;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by zhangyuanyuan on 2017/10/20.
 */

public class UDPClientRunnable implements Runnable {

    private DatagramSocket mServer;
    private UdpServerListener mUdpServerListener;

    private boolean udpLife = true;     //udp生命线程

    private DatagramPacket dpRcv;
    private byte[] msgRcv = new byte[1024];

    public UDPClientRunnable(DatagramSocket datagramSocket, UdpServerListener udpServerListener) {
        this.mServer = datagramSocket;
        this.mUdpServerListener = udpServerListener;
    }


    @Override
    public void run() {
        try {

            dpRcv = new DatagramPacket(msgRcv, msgRcv.length);

            while (udpLife) {
                mServer.receive(dpRcv);

                if (mUdpServerListener != null) {
                    mUdpServerListener.onReceive(dpRcv);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (mUdpServerListener != null) {
                mUdpServerListener.onFail(e);
            }
            mServer.close();
        }
    }
}
