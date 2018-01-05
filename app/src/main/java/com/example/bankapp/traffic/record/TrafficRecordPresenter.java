package com.example.bankapp.traffic.record;

import com.example.bankapp.base.presenter.BasePresenter;

/**
 * Created by dell on 2017/12/28.
 */

public class TrafficRecordPresenter extends BasePresenter<ITrafficRecordView> {

    public TrafficRecordPresenter(ITrafficRecordView mView) {
        super(mView);
    }

    @Override
    public void onLocalError() {

    }

    @Override
    public void onInsufficient() {

    }
}
