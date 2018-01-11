package com.example.bankapp.main.videocall;

import com.example.bankapp.base.view.UiView;

/**
 * Created by dell on 2018/1/6.
 */

public interface IVideoCallView extends UiView {
    void setTopText(String text);

    void setRemoteVisiable(boolean visiable);

    void exit();
}
