package com.example.bankapp.modle.voice;


import com.example.bankapp.modle.voice.translation.SlotsBean;
import com.youdao.sdk.ydtranslate.Translate;

import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/11/16.
 */

public class Translation {

    /**
     * intent : TRANSLATION
     * slots : [{"name":"content","value":"苹果"},{"name":"source","value":"cn"},{"name":"target","value":"en"}]
     */
    /**
     * "oriLangCountry":"cn",
     * "original":"苹果",
     * "transLangCountry":"en",
     * "translated":"Apple"
     */

    private String oriLangCountry;
    private String original;
    private String transLangCountry;
    private String translated;

    public String getOriLangCountry() {
        return oriLangCountry;
    }

    public void setOriLangCountry(String oriLangCountry) {
        this.oriLangCountry = oriLangCountry;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getTransLangCountry() {
        return transLangCountry;
    }

    public void setTransLangCountry(String transLangCountry) {
        this.transLangCountry = transLangCountry;
    }

    public String getTranslated() {
        return translated;
    }

    public void setTranslated(String translated) {
        this.translated = translated;
    }

    public Translation(String oriLangCountry, String original, String transLangCountry, String translated) {
        this.oriLangCountry = oriLangCountry;
        this.original = original;
        this.transLangCountry = transLangCountry;
        this.translated = translated;
    }
}
