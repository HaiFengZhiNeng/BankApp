package com.example.bankapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.bankapp.main.MainView;

/**
 * 开机启动 广播
 *
 * @author Guanluocang
 *         created at 2017/12/22 15:30
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //MainActivity就是开机显示的界面
        Intent mBootIntent = new Intent(context, MainView.class);
        //下面这句话必须加上才能开机自动运行app的界面
        mBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mBootIntent);
    }
}
