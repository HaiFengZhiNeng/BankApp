package com.example.bankapp.udp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created by zhangyuanyuan on 2017/10/20.
 */

public class UDPAcceptReceiver extends BroadcastReceiver {

    private UDPAcceptInterface mUdpAcceptInterface;

    public UDPAcceptReceiver(UDPAcceptInterface udpAcceptInterface) {
        this.mUdpAcceptInterface = udpAcceptInterface;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String content = intent.getStringExtra("content");
        if (content != null) {
            if(content != null){
                if(content.contains(",")) {
                } else if(content.contains("udp")) {
                    mUdpAcceptInterface.UDPinitFinsih(content);
                } else{
                    mUdpAcceptInterface.UDPAcceptMessage(content);

                }
            }
        }
    }


    public interface UDPAcceptInterface {
        void UDPAcceptMessage(String content);

        void UDPinitFinsih(String content);
    }

}
