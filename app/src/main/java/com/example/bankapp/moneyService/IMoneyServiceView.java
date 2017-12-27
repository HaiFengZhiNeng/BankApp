package com.example.bankapp.moneyService;

import com.example.bankapp.base.view.UiView;

/**
 * Created by dell on 2017/12/11.
 */

public interface IMoneyServiceView extends UiView {

    void isIntroduceVisibility(boolean isVisibility);

    void isIntroduceFinanceVisibility(boolean isVisibility);
}
