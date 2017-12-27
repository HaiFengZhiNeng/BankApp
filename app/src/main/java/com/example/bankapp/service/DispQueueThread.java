package com.example.bankapp.service;

import android.app.Activity;


import com.example.bankapp.listener.DispListener;
import com.example.bankapp.modle.ComBean;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by zhangyuanyuan on 2017/10/26.
 */

public class DispQueueThread extends Thread {

    private Queue<ComBean> QueueList = new LinkedList<ComBean>();

    private Activity mActivity;
    private DispListener mDispListener;

    public DispQueueThread(Activity activity) {
        mActivity = activity;
    }

    public void setDispListener(DispListener dispListener) {
        this.mDispListener = dispListener;
    }

    @Override
    public void run() {
        super.run();
        while (!isInterrupted()) {
            final ComBean ComData;
            while ((ComData = QueueList.poll()) != null) {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (mDispListener != null) {
                            mDispListener.dispRecData(ComData);
                        }
                    }
                });
                try {
                    Thread.sleep(100);//显示性能高的话，可以把此数值调小。
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public synchronized void AddQueue(ComBean ComData) {
        QueueList.add(ComData);
    }


}
