package com.example.bankapp.modle;

/**
 * Created by zhangyuanyuan on 2017/11/8.
 */

public class AddData {

    private String name;
    private Class fragment;

    public AddData(String name, Class fragment) {
        this.name = name;
        this.fragment = fragment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }


}
