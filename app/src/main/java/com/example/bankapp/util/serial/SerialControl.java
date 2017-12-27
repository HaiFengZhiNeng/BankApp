package com.example.bankapp.util.serial;


import com.example.bankapp.modle.ComBean;
import com.example.bankapp.service.DispQueueThread;

/**
 * Created by zhangyuanyuan on 2017/9/25.
 */

public class SerialControl extends SerialHelper {

    private DispQueueThread mDispQueueThread;

    public SerialControl(DispQueueThread dispQueueThread) {
        mDispQueueThread = dispQueueThread;
    }

    @Override
    protected void onDataReceived(ComBean ComRecData) {
        mDispQueueThread.AddQueue(ComRecData);
    }
}
