package com.example.bankapp.modle.voice;

/**
 * Created by dell on 2017/12/15.
 */

public class EnglishEveryday {
    private String caption;//"词霸每日一句"
    private String content;//"The more you know, the more you know you don't know."
    private String translation;//"你知道的越多，你会发现你不知道的也就越多"
    private String url;//""http://news.iciba.com/admin/tts/2014-01-23.mp3""

    public EnglishEveryday(String caption, String content, String translation, String url) {
        this.caption = caption;
        this.content = content;
        this.translation = translation;
        this.url = url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
