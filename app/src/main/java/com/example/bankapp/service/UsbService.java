package com.example.bankapp.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


import com.example.bankapp.usb.UsbSerialDevice;
import com.example.bankapp.usb.UsbSerialInterface;
import com.example.bankapp.util.HexUtils;

import java.util.HashMap;
import java.util.Map;


public class UsbService extends Service {

    public static final String ACTION_USB_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    public static final String ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    public static final String ACTION_USB_RECEIVER = "com.android.example.USB_RECEIVER";

    public static final String ACTION_WRITW_DATA  = "com.android.example.ACTION_WRITW_DATA";

    private static final int BAUD_RATE = 9600; // BaudRate. Change this value if you need

    private Context context;

    public static boolean SERVICE_CONNECTED = false;

    private UsbManager usbManager;
    private UsbDevice device;
    private UsbDeviceConnection connection;
    private UsbSerialDevice serialPort;

    private boolean serialPortConnected;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        serialPortConnected = false;

        UsbService.SERVICE_CONNECTED = true;

        setFilter();
        setFilter1();
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        findSerialPortDevice();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new UsbBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        UsbService.SERVICE_CONNECTED = false;
    }

    public void write(byte[] data) {
        if (serialPort != null)
            serialPort.write(data);
    }

    public class UsbBinder extends Binder {
        public UsbService getService() {
            return UsbService.this;
        }
    }

    private void setFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(ACTION_USB_DETACHED);
        filter.addAction(ACTION_USB_ATTACHED);
        registerReceiver(usbReceiver, filter);
    }

    private void setFilter1(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_WRITW_DATA);
        registerReceiver(writeReceiver, filter);
    }


    private void findSerialPortDevice() {
        // This snippet will try to open the first encountered usb device connected, excluding usb root hubs
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            boolean keep = true;
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                int deviceVID = device.getVendorId();
                int devicePID = device.getProductId();

                if (deviceVID == 1155 && devicePID == 22336) {
                    // There is a device connected to our Android device. Try to open it as a Serial Port.
                    requestUserPermission();
                    keep = false;
                } else {
                    connection = null;
                    device = null;
                }

                if (!keep)
                    break;
            }
            if (!keep) {
                // There is no USB devices connected (but usb host were listed). Send an intent to MainActy.
                Log.e("GG","ACTION_NO_USB");
            }
        } else {
            // There is no USB devices connected. Send an intent to MainActivity
            Log.e("GG","ACTION_NO_USB");
        }
    }

    private void requestUserPermission() {
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        usbManager.requestPermission(device, mPendingIntent);
    }


    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getAction().equals(ACTION_USB_PERMISSION)) {
                boolean granted = arg1.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                if (granted) // User accepted our USB connection. Try to open the device as a serial port
                {
                    Log.e("GG","USB Ready");
                    connection = usbManager.openDevice(device);
                    new ConnectionThread().start();
                } else // User not accepted our USB connection. Send an Intent to the Main Activity
                {//
                    Log.e("GG","USB Permission not granted");
                }
            } else if (arg1.getAction().equals(ACTION_USB_ATTACHED)) {
                if (!serialPortConnected)
                    findSerialPortDevice(); // A USB device has been attached. Try to open it as a Serial port
            } else if (arg1.getAction().equals(ACTION_USB_DETACHED)) {
                // Usb device was disconnected. send an intent to the Main Activity
                Log.e("GG","USB disconnected");
                if (serialPortConnected) {
                    serialPort.close();
                }
                serialPortConnected = false;
            }
        }
    };

    private final BroadcastReceiver writeReceiver = new BootBroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_WRITW_DATA)) {

                String motion = intent.getStringExtra("motion");
                Log.e("GG","usb 接收广播 " + motion);
                byte[] bOutArray = HexUtils.HexToByteArr(motion);
            write(bOutArray);
            }
        }
    };

    private class ConnectionThread extends Thread {
        @Override
        public void run() {
            serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
            if (serialPort != null) {
                if (serialPort.open()) {
                    serialPortConnected = true;
                    serialPort.setBaudRate(BAUD_RATE);
                    serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                    serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                    serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                    serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                    serialPort.read(mCallback);

                    Log.e("GG","Everything went as expected. Send an intent to MainActivity");
                } else {
                    Log.e("GG","open error");
                }
            } else {
                Log.e("GG","USB device not supported");
            }
        }
    }

    private UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {
        @Override
        public void onReceivedData(byte[] arg0) {
            String data = HexUtils.byte2HexStr(arg0).replace(" ", "");
//            Print.e("data : " + data);
            if (data != null && data.equals("A5068003AA")) {
                Intent intent = new Intent(ACTION_USB_RECEIVER);
                sendBroadcast(intent);
            }
        }
    };

}
