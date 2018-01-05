package com.example.bankapp.modle;

/**
 * Created by dell on 2018/1/3.
 */

public class TrafficRecord {
    private String mTrafficRecordCar;//车牌
    private String mTrafficRecordInfo;//违章信息
    private String mTrafficRecordMoney;//罚款
    private String mTrafficRecordData;//扣分

    public TrafficRecord(String mTrafficRecordCar, String mTrafficRecordInfo, String mTrafficRecordMoney, String mTrafficRecordData) {
        this.mTrafficRecordCar = mTrafficRecordCar;
        this.mTrafficRecordInfo = mTrafficRecordInfo;
        this.mTrafficRecordMoney = mTrafficRecordMoney;
        this.mTrafficRecordData = mTrafficRecordData;
    }

    public String getmTrafficRecordCar() {
        return mTrafficRecordCar;
    }

    public void setmTrafficRecordCar(String mTrafficRecordCar) {
        this.mTrafficRecordCar = mTrafficRecordCar;
    }

    public String getmTrafficRecordInfo() {
        return mTrafficRecordInfo;
    }

    public void setmTrafficRecordInfo(String mTrafficRecordInfo) {
        this.mTrafficRecordInfo = mTrafficRecordInfo;
    }

    public String getmTrafficRecordMoney() {
        return mTrafficRecordMoney;
    }

    public void setmTrafficRecordMoney(String mTrafficRecordMoney) {
        this.mTrafficRecordMoney = mTrafficRecordMoney;
    }

    public String getmTrafficRecordData() {
        return mTrafficRecordData;
    }

    public void setmTrafficRecordData(String mTrafficRecordData) {
        this.mTrafficRecordData = mTrafficRecordData;
    }
}
