package com.example.bankapp.base.view;

import android.content.Context;

import com.example.bankapp.common.enums.SpecialType;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.radio.Radio;

/**
 *    方便得到Context
 *@author Guanluocang
 *created at 2017/12/6 15:14
*/
public interface UiView {
    /**
     * 返回上下文
     * @return Context
     */
    Context getContext();

    void special(String result, SpecialType type);

    void spakeMove(SpecialType specialType, String result);

    void doAiuiAnwer(String anwer);

    void refHomePage(String question, String finalText);

    void refHomePage(String question, News news);

    void refHomePage(String question, Radio radio);

    void refHomePage(String question, Poetry poetry);

    void refHomePage(String question, Cookbook cookbook);

    void refHomePage(String question, EnglishEveryday englishEveryday);

    void doCallPhone(String phoneNumber);

    void doDance();
}
