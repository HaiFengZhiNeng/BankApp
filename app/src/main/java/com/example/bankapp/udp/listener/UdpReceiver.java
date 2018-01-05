package com.example.bankapp.udp.listener;


import com.example.bankapp.base.config.Constant;
import com.example.bankapp.udp.SocketManager;

/**
 * Created by zhangyuanyuan on 2017/10/20.
 */

public class UdpReceiver extends UdpRegisterRequestListener {

    private OnListenerUDPServer onListenerUDPServer;

    public UdpReceiver(OnListenerUDPServer onListenerUDPServer) {
        this.onListenerUDPServer = onListenerUDPServer;
    }


    @Override
    public void onFail(Exception e) {
        super.onFail(e);
        e.printStackTrace();
    }

    @Override
    public void onReceive(String ip, int port, String result) {
        super.onReceive(ip, port, result);
        if (!SocketManager.getInstance().isGetTcpIp) {

            if(Constant.CONNECT_IP == null){
                Constant.CONNECT_IP = ip;
            }
            if(Constant.CONNECT_PORT == 0){
                Constant.CONNECT_PORT = port;
            }
                SocketManager.getInstance().setUdpIp();
                if (onListenerUDPServer != null)
                    onListenerUDPServer.acquireIp(true);
        } else {
            if (onListenerUDPServer != null)
                onListenerUDPServer.receiver(result);
        }
    }

    @Override
    public void onStart(String myIp, int myPort) {
        super.onStart(myIp, myPort);
        if (onListenerUDPServer != null)
            onListenerUDPServer.onStart(myIp, myPort);
    }
}
