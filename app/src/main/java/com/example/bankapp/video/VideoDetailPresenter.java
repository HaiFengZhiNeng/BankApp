package com.example.bankapp.video;

import com.example.bankapp.base.presenter.BasePresenter;

/**
 * Created by dell on 2017/12/22.
 */

public class VideoDetailPresenter extends BasePresenter<IVideoDetailView> {

    public VideoDetailPresenter(IVideoDetailView mView) {
        super(mView);
    }

    @Override
    public void onLocalError() {

    }

    @Override
    public void onInsufficient() {

    }
}
