package com.example.bankapp.main;

import android.os.Bundle;

import com.example.bankapp.R;
import com.example.bankapp.base.presenter.BasePresenter;
import com.example.bankapp.database.manager.IntroduceManager;
import com.example.bankapp.modle.LocalMoneyService;
import com.example.bankapp.util.PreferencesUtils;

/**
 * Created by dell on 2017/12/6.
 */

public class MainPresenter extends BasePresenter<IMainView> {


    public MainPresenter(IMainView mView) {
        super(mView);
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    //添加本地数据
    void addLocalData(IntroduceManager introduceManager) {
        boolean isSaveCountry = PreferencesUtils.getBoolean(mView.getContext(), "localIntroduce", false);
        if (!isSaveCountry) {
            PreferencesUtils.putBoolean(mView.getContext(), "localIntroduce", true);
            String[] introduceQuestion = mView.getContext().getResources().getStringArray(R.array.local_introduce_question);
            String[] introduceAnswer = mView.getContext().getResources().getStringArray(R.array.local_introduce_answer);
            for (int i = 0; i < introduceQuestion.length; i++) {
                introduceManager.insert(new LocalMoneyService(introduceQuestion[i], introduceAnswer[i]));
            }
        }
    }

    @Override
    public void onLocalError() {

    }

    @Override
    public void onInsufficient() {

    }
}
