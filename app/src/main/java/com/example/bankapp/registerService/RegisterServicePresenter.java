package com.example.bankapp.registerService;

import android.os.Bundle;

import com.example.bankapp.base.presenter.BasePresenter;

/**
 * Created by dell on 2017/12/13.
 */

public class RegisterServicePresenter extends BasePresenter<IRegisterServiceView> {

    public RegisterServicePresenter(IRegisterServiceView mView) {
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
