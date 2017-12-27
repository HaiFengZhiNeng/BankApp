package com.example.bankapp.register;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.bankapp.R;
import com.example.bankapp.base.presenter.BasePresenter;

/**
 * Created by dell on 2017/12/7.
 */

public class RegisterPresenter extends BasePresenter<IRegisiterView> {
    public RegisterPresenter(IRegisiterView mView) {
        super(mView);
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    //头部 四个选项
    void doSelectTextColor(String which) {
        switch (which) {
            case "PersonalForeign":
                mView.setSelectTextColor(getContext().getResources().getColor(R.color.color_red), getContext().getResources().getColor(R.color.color_main_function_item), which);
                break;
            case "Personal":
                mView.setSelectTextColor(getContext().getResources().getColor(R.color.color_red), getContext().getResources().getColor(R.color.color_main_function_item), which);
                break;
            case "Public":
                mView.setSelectTextColor(getContext().getResources().getColor(R.color.color_red), getContext().getResources().getColor(R.color.color_main_function_item), which);
                break;
            case "Appointment":
                mView.setSelectTextColor(getContext().getResources().getColor(R.color.color_red), getContext().getResources().getColor(R.color.color_main_function_item), which);
                break;
        }
    }

    // 立即排号  预约排号
    void doSelectRegisterItem(String which) {
        switch (which) {
            case "Appointment":
                mView.setRegisterSelectItem(getContext().getResources().getColor(R.color.color_white), getContext().getResources().getColor(R.color.color_main_function_item), which);
                break;
            case "LineUp":
                mView.setRegisterSelectItem(getContext().getResources().getColor(R.color.color_white), getContext().getResources().getColor(R.color.color_main_function_item), which);
                break;
        }
    }

    @Override
    public void onLocalError() {

    }

    @Override
    public void onInsufficient() {

    }
}
