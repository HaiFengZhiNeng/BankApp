package com.example.bankapp.addLocalData.video;

import com.example.bankapp.base.presenter.BasePresenter;

/**
 * Created by dell on 2017/12/21.
 */

public class AddVideoPresenter extends BasePresenter<IAddVideoView> {

    public AddVideoPresenter(IAddVideoView mView) {
        super(mView);
    }

    @Override
    public void onLocalError() {

    }

    @Override
    public void onInsufficient() {

    }
}
