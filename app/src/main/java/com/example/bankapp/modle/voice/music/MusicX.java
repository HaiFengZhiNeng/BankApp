package com.example.bankapp.modle.voice.music;

import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/11/13.
 */

public class MusicX {

    private boolean save_history;
    private int rc;
    private List<Semantic> semantic;
    private String service;
    private String uuid;
    private String text;
    private AnswerMusic answerMusic;
    private String dialog_stat;
    private String sid;

    public boolean isSave_history() {
        return save_history;
    }

    public void setSave_history(boolean save_history) {
        this.save_history = save_history;
    }

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public List<Semantic> getSemantic() {
        return semantic;
    }

    public void setSemantic(List<Semantic> semantic) {
        this.semantic = semantic;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AnswerMusic getAnswerMusic() {
        return answerMusic;
    }

    public void setAnswerMusic(AnswerMusic answerMusic) {
        this.answerMusic = answerMusic;
    }

    public String getDialog_stat() {
        return dialog_stat;
    }

    public void setDialog_stat(String dialog_stat) {
        this.dialog_stat = dialog_stat;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
