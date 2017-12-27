package com.example.bankapp.business;

import com.example.bankapp.base.presenter.BasePresenter;

/**
 * Created by dell on 2017/12/11.
 */

public class BusinessPresenter extends BasePresenter<IBusinessView> {

    public BusinessPresenter(IBusinessView mView) {
        super(mView);
    }

    @Override
    public void onLocalError() {

    }

    @Override
    public void onInsufficient() {

    }
}
