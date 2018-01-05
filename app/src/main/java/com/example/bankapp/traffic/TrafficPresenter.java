package com.example.bankapp.traffic;

import com.example.bankapp.base.presenter.BasePresenter;

/**
 * Created by dell on 2017/12/28.
 */

public class TrafficPresenter extends BasePresenter<ITrafficView> {

    public TrafficPresenter(ITrafficView mView) {
        super(mView);
    }

    @Override
    public void onLocalError() {

    }

    @Override
    public void onInsufficient() {

    }
}
