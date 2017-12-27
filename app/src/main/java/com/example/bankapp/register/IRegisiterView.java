package com.example.bankapp.register;

import com.example.bankapp.base.view.UiView;

/**
 * Created by dell on 2017/12/7.
 */

public interface IRegisiterView extends UiView {

    void setSelectTextColor(int selectColor, int unSelectColor, String which);

    void setRegisterSelectItem(int selectColor, int unSelectColor,String which);
}
