package com.example.bankapp.modle;

import android.widget.TextView;

import com.example.bankapp.R;

import butterknife.BindView;

/**
 * 理财产品
 *@author Guanluocang
 *created at 2017/12/21 9:40
*/

public class MoneyCurrent {

    private String currentType;//类型
    private String currentperentage;//利率
    private String currentperentageTwo;//年收益率
    private String currentTime;//时间期限
    private String currentAmount;//金额

    public MoneyCurrent(String currentType, String currentperentage, String currentperentageTwo, String currentTime, String currentAmount) {
        this.currentType = currentType;
        this.currentperentage = currentperentage;
        this.currentperentageTwo = currentperentageTwo;
        this.currentTime = currentTime;
        this.currentAmount = currentAmount;
    }

    public String getCurrentperentageTwo() {
        return currentperentageTwo;
    }

    public void setCurrentperentageTwo(String currentperentageTwo) {
        this.currentperentageTwo = currentperentageTwo;
    }

    public String getCurrentType() {
        return currentType;
    }

    public void setCurrentType(String currentType) {
        this.currentType = currentType;
    }

    public String getCurrentperentage() {
        return currentperentage;
    }

    public void setCurrentperentage(String currentperentage) {
        this.currentperentage = currentperentage;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(String currentAmount) {
        this.currentAmount = currentAmount;
    }
}
