package com.example.bankapp.distinguish.faceDistinguish.face;


import java.util.List;

public class YtGroupids extends YtPerson {

    private List<String> group_ids;

    public List<String> getGroup_ids() {
        return group_ids;
    }

    public void setGroup_ids(List<String> group_ids) {
        this.group_ids = group_ids;
    }

    @Override
    public String toString() {
        return "YtGroupids{" +
                "group_ids=" + group_ids +
                '}';
    }
}
