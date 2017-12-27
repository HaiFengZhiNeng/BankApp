package com.example.bankapp.serialport;


import com.example.bankapp.base.view.UiView;
import com.example.bankapp.common.enums.ComType;
import com.example.bankapp.modle.SerialEntity;

/**
 * Created by zhangyuanyuan on 2017/9/25.
 */

public abstract class ISerialPortView {

    private ISerialView mBaseView;

    public ISerialPortView(ISerialView baseView) {
        mBaseView = baseView;
    }

    public abstract boolean isHasDevices(SerialEntity comPort);

    public abstract void openComPort(SerialEntity ComPort);

    public abstract void closeComPort();

    public abstract void sendPortData(SerialEntity control, String sOut);

    public abstract void receiveMotion(ComType type, String motion);

    public interface ISerialView extends UiView {

        void stopAll();

        void onMoveStop();

        void sayHellow();

//        void walkUSBChannel(String motion);
    }
}
