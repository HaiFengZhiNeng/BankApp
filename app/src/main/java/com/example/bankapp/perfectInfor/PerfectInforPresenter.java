package com.example.bankapp.perfectInfor;

import com.example.bankapp.base.presenter.BasePresenter;

/**
 * Created by dell on 2017/12/12.
 */

public class PerfectInforPresenter extends BasePresenter<IPerfectInforView> {

    public PerfectInforPresenter(IPerfectInforView mView) {
        super(mView);
    }

    @Override
    public void onLocalError() {

    }

    @Override
    public void onInsufficient() {

    }
}
