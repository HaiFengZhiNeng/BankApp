package com.example.bankapp.util;

import com.yuntongxun.ecsdk.ECMessage;

/**
 * Created by lyw on 2017/9/22.
 */

public interface ReceiveMessage {
    void OnReceivedMessage(ECMessage msg) ;
}