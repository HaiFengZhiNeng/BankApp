package com.example.bankapp.addLocalData.voice;

import com.example.bankapp.base.presenter.BasePresenter;

/**
 * Created by dell on 2017/12/21.
 */

public class AddVoicePreSenter extends BasePresenter<IAddVoiceView> {
    public AddVoicePreSenter(IAddVoiceView mView) {
        super(mView);
    }

    @Override
    public void onLocalError() {

    }

    @Override
    public void onInsufficient() {

    }
}
