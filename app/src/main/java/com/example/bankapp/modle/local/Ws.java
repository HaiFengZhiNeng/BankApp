package com.example.bankapp.modle.local;

import java.util.List;


public class Ws {

    private int bg;
    private String slot;
    private List<Cw> cw;
    public void setBg(int bg) {
        this.bg = bg;
    }
    public int getBg() {
        return bg;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }
    public String getSlot() {
        return slot;
    }

    public void setCw(List<Cw> cw) {
        this.cw = cw;
    }
    public List<Cw> getCw() {
        return cw;
    }

}
