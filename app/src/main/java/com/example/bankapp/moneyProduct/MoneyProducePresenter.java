package com.example.bankapp.moneyProduct;

import android.os.Bundle;

import com.example.bankapp.base.presenter.BasePresenter;
import com.example.bankapp.base.view.UiView;

/**
 * Created by dell on 2017/12/12.
 */

public class MoneyProducePresenter extends BasePresenter<UiView> {

    public MoneyProducePresenter(UiView mView) {
        super(mView);
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    @Override
    public void onLocalError() {

    }

    @Override
    public void onInsufficient() {

    }
}
