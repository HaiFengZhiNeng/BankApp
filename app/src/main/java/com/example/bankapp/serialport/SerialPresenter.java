package com.example.bankapp.serialport;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.example.bankapp.common.enums.ComType;
import com.example.bankapp.listener.DataReceivedListener;
import com.example.bankapp.modle.ComBean;
import com.example.bankapp.modle.SerialEntity;
import com.example.bankapp.service.UsbService;
import com.example.bankapp.util.HexUtils;
import com.example.bankapp.util.SerialManager;

import java.io.IOException;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPortFinder;

/**
 * Created by zhangyuanyuan on 2017/9/25.
 */

public class SerialPresenter extends ISerialPortView implements DataReceivedListener {

    private ISerialView mSerialView;

    private SerialEntity comA;
    private SerialEntity comB;
    private SerialEntity comC;

    private String devName = "ttyUSB1";//控制运动
    private String voiceName = "ttyUSB0";// 麦克风阵列 唤醒
    private String cruiseName = "ttyUSB2";// 导航

    private SerialManager mSerialManager;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String hex = (String) msg.obj;
                    receiveMotion(ComType.A, hex);
                    break;
                case 1:
                    mSerialView.stopAll();
                    break;
            }
        }
    };

    public SerialPresenter(ISerialView baseView) {
        super(baseView);
        this.mSerialView = baseView;

        mSerialManager = new SerialManager(((Activity) mSerialView), this);

        comA = new SerialEntity();
        comA.setPort("/dev/" + devName);
        comA.setBaudRate(9600);
        // 打开串口
        openComPort(comA);

        comB = new SerialEntity();
        comB.setPort("/dev/" + voiceName);
        comB.setBaudRate(115200);
        openComPort(comB);

        comC = new SerialEntity();
        comC.setPort("/dev/" + cruiseName);
        comC.setBaudRate(57600);
        openComPort(comC);
    }

    @Override
    public boolean isHasDevices(SerialEntity comPort) {
        SerialPortFinder serialPortFinder = new SerialPortFinder();
        String[] devices = serialPortFinder.getAllDevices();
        if (devices != null && devices.length > 0) {
            for (int i = 0; i < devices.length; i++) {
                if (comPort.getPort().equals("/dev/" + devices[i])) {
                    return true;
                }
            }
        }
        Log.e("gg", "此机器没有 Devices " + comPort.getPort());
        return false;
    }

    @Override
    public void openComPort(SerialEntity comPort) {
        if (!isHasDevices(comPort)) {
            return;
        }
        try {
            Log.e("gg", "串口打开 ： " + comPort.getPort());
//            comPort.open();
            mSerialManager.open(comPort);
        } catch (SecurityException e) {
            Log.e("gg", "打开串口失败:没有串口读/写权限!");
        } catch (IOException e) {
            Log.e("gg", "打开串口失败:未知错误!");
        } catch (InvalidParameterException e) {
            Log.e("gg", "打开串口失败:参数错误!");
        }
    }

    @Override
    public void closeComPort() {

        if (comA != null) {
            if (!isHasDevices(comA)) {
                return;
            }
            mSerialManager.stopSend(comA.getPort());
        }

        if (comB != null) {
            if (!isHasDevices(comB)) {
                return;
            }
            mSerialManager.stopSend(comB.getPort());
        }

        if (comC != null) {
            if (!isHasDevices(comC)) {
                return;
            }
            mSerialManager.stopSend(comC.getPort());
        }

        mSerialManager.close();
        comA = null;
        comB = null;
        comC = null;
    }


    @Override
    public void sendPortData(SerialEntity control, String sOut) {

        if (!isHasDevices(control)) {
            return;
        }
        if (control != null) {
            Log.e("gg", "串口指令 ： " + control.getPort() + " , " + control.getBaudRate() + " , " + sOut);
            mSerialManager.sendHex(control, sOut);

        }
    }

    @Override
    public void receiveMotion(ComType comType, String motion) {
        if (comType == ComType.A) {
            sendPortData(comA, motion);
//            Log.e("gg", " usb 发送广播 " + motion);
//            Intent intent = new Intent();
//            intent.setAction(UsbService.ACTION_WRITW_DATA);
//            intent.putExtra("motion", motion);
//            mSerialView.getContext().sendBroadcast(intent);
//            mSerialView.walkUSBChannel(motion);
        } else if (comType == ComType.B) {
            sendPortData(comB, motion);
        } else if (comType == ComType.C) {
            sendPortData(comC, motion);
        }
    }

    @Override
    public void onDataReceiverd(ComBean comRecData) {
        if (comRecData.sComPort.equals(comB.getPort())) {
            StringBuilder sMsg = new StringBuilder();
            //在十六进制转换为字符串后的得到的是Unicode编码,此时再将Unicode编码解码即可获取原始字符串
            sMsg.append(HexUtils.hexStringToString(HexUtils.byte2HexStr(comRecData.bRec)));
            Log.e("gg", "sMsg : " + sMsg);
            if (sMsg.toString().contains("UP!")) {
                sendDelayed();
                if (sMsg.toString().contains("##### IFLYTEK")) {

                    String str = sMsg.toString().substring(sMsg.toString().indexOf("angle:") + 6, sMsg.toString().indexOf("##### IFLYTEK"));
                    final int angle = Integer.parseInt(str.trim());
                    Log.e("gg", "sMsg : " + angle);
//                    ((Activity)mSerialView).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mSerialView.showMsg("sMsg : " +  angle);
//                        }
//                    });

                    if (0 <= angle && angle < 30) {
                        receiveMotion(ComType.A, "A5218201AA");//0
                        receiveMotion(ComType.B, "BEAM 0\n\r");//0
                    } else if (30 <= angle && angle <= 60) {
                        receiveMotion(ComType.A, "A521822DAA");//45
                        receiveMotion(ComType.B, "BEAM 0\n\r");//0
                    } else if (120 <= angle && angle <= 150) {
                        receiveMotion(ComType.A, "A5218287AA");//135
                        receiveMotion(ComType.B, "BEAM 0\n\r");//0
                    } else if (150 < angle && angle <= 180) {
                        receiveMotion(ComType.A, "A52182B3AA\n\r");//180
                    } else if (60 < angle && angle < 120) {
                        receiveMotion(ComType.A, "A521825AAA");//90
                        Log.e("gg", "已唤醒，打招呼 : ");
                    } else {
                    }

                }

            }
        } else if (comRecData.sComPort.equals(comC.getPort())) {
            StringBuilder sMsg = new StringBuilder();
            //在十六进制转换为字符串后的得到的是Unicode编码,此时再将Unicode编码解码即可获取原始字符串
            sMsg.append(HexUtils.hexStringToString(HexUtils.byte2HexStr(comRecData.bRec)));
            Log.e("gg", "sMsg : " + sMsg);
            if (sMsg.toString().trim().equals("first") || sMsg.toString().trim().equals("second") || sMsg.toString().trim().equals("third")
                    || sMsg.toString().trim().equals("fifth") || sMsg.toString().trim().equals("sixth")) {

                receiveMotion(ComType.A, "A50C8001AA");
                mSerialView.onMoveStop();
            }
        } else if (comRecData.sComPort.equals(comA.getPort())) {
//            StringBuilder sMsg = new StringBuilder();

//            //在十六进制转换为字符串后的得到的是Unicode编码,此时再将Unicode编码解码即可获取原始字符串
//            sMsg.append(HexUtils.hexStringToString(HexUtils.byte2HexStr(comRecData.bRec)));
//
//            Print.e("sMsg : " + sMsg);
//            Print.e("wwdz : " + data);
//          //  String data = sMsg.toString().trim();
//            if (data != null && data.equals("A5068003AA")) {
//
//                Intent intent = new Intent(UsbService.ACTION_USB_RECEIVER);
//                mSerialView.getContext().sendBroadcast(intent);
//            }

//            StringBuilder sbMsg = new StringBuilder();
//
//            sbMsg.append(MyFunc.ByteArrToHex(comRecData.bRec));
//            Log.e("wwdz","sbMsg.toString().trim()=="+sbMsg.toString().trim());
//            if (sbMsg.toString().trim().equals("A5")) {
//                mSerialView.sayHellow();
//            }
//           mSerialView.sayHellow();
        }
    }

//    private void sendDelayed(String hex){
//        Message msg = new Message();
//        msg.what = 0;
//        msg.obj = hex;
//        mHandler.sendMessageDelayed(msg, 0);
//    }

    private void sendDelayed() {
        mHandler.sendEmptyMessageDelayed(1, 0);
    }

}
