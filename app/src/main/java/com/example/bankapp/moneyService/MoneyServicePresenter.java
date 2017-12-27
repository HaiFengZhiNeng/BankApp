package com.example.bankapp.moneyService;

import android.os.Bundle;

import com.example.bankapp.base.presenter.BasePresenter;

/**
 * Created by dell on 2017/12/11.
 */

public class MoneyServicePresenter extends BasePresenter<IMoneyServiceView> {

    private boolean isIntroduce = true;
    private boolean isIntroduceFinance = true;

    public MoneyServicePresenter(IMoneyServiceView mView) {
        super(mView);
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

    }

    //理财介绍
    public void setIntroduceVisibility() {
        if (isIntroduce) {
            mView.isIntroduceVisibility(false);
        } else {
            mView.isIntroduceVisibility(true);
        }
        isIntroduce = !isIntroduce;
    }

    //理财与财经
    public void setIntroduceFinanceVisibility() {
        if (isIntroduceFinance) {
            mView.isIntroduceFinanceVisibility(false);
        } else {
            mView.isIntroduceFinanceVisibility(true);
        }
        isIntroduceFinance = !isIntroduceFinance;
    }

    @Override
    public void onLocalError() {

    }

    @Override
    public void onInsufficient() {

    }
}
