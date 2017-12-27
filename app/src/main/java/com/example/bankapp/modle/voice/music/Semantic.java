package com.example.bankapp.modle.voice.music;

import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/11/13.
 */

public class Semantic {

    private String intent;
    private List<SlotsMusic> slots;

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public List<SlotsMusic> getSlots() {
        return slots;
    }

    public void setSlots(List<SlotsMusic> slots) {
        this.slots = slots;
    }
}
