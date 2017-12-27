package com.example.bankapp.addLocalData;

import com.example.bankapp.base.presenter.BasePresenter;

/**
 * Created by dell on 2017/12/21.
 */

public class AddDataPresenter extends BasePresenter<IAddDataView> {

    public AddDataPresenter(IAddDataView mView) {
        super(mView);
    }

    @Override
    public void onLocalError() {

    }

    @Override
    public void onInsufficient() {

    }
}
