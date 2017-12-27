package com.example.bankapp.common.enums;

import android.widget.TextView;

import com.example.bankapp.R;

import java.io.Serializable;

import butterknife.BindView;

/**
 * Created by zhangyuanyuan on 2017/10/15.
 */


public enum SpecialType implements Serializable {
    WakeUp, Music, Story, Joke, NoSpecial,
    Forward, Backoff, Turnleft, Turnright,
    BusinessService,//业务查询
    CostService,//e缴费
    RegisterService,//挂号服务
    IllegalrService,//违章查询
    IdentityService,//身份认证
    MoneyService,//理财产品
    Exit, LocalVoice

}

