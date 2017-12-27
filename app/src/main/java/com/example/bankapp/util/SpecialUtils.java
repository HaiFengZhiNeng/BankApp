package com.example.bankapp.util;

import android.app.Activity;

import com.example.bankapp.R;
import com.example.bankapp.common.enums.SpecialType;

import java.util.Arrays;

/**
 * Created by zhangyuanyuan on 2017/10/24.
 */

public class SpecialUtils {


    public static String[] speakMusicArray = {"唱歌", "唱歌儿", "唱一首歌", "我想听音乐", "播放音乐", "来首歌曲", "播放歌曲", "唱首歌", "音乐",
            "音乐播放中..."};
    public static String[] speakStoryArray = {"故事", "讲故事", "讲一个故事", "换一个故事吧", "讲个故事", "段子", "讲个故事", "你会讲故事嘛",
            "换一个故事"};
    public static String[] speakJokeArray = {"笑话", "讲笑话", "讲一个笑话", "换一个笑话吧", "讲个段子", "讲个笑话", "段子"};

    //业务查询
    public static String[] speakBusinessService = {"业务查询", ""};
    //e缴费
    public static String[] speakCostService = {"缴费", "交费"};
    //挂号服务
    public static String[] speakRegisterService = {"挂号服务"};
    //违章查询
    public static String[] speakIllegalrService = {"违章查询"};
    //身份认证
    public static String[] speakIdentityService = {"身份认证"};
    //理财产品
    public static String[] speakMoneyService = {"理财产品"};

    public static String[] speakWakeupArray = {"你好芳芳"};
    public static String[] speakForwardArray = {"前进", "向前", "向前走"};
    public static String[] speakBackoffArray = {"后退", "向后", "向后走", "后腿", "后推"};
    public static String[] speakTurnleftArray = {"左转", "向左转"};
    public static String[] speakTurnrightArray = {"右转", "向右转"};
    public static String[] speakExitArray = {"退出", "返回", "发挥", "推出", "闪回", "八回", "回"};

    public static SpecialType doesExist(Activity activity, String speakTxt) {
        if (Arrays.asList(speakWakeupArray).contains(speakTxt)) {
            return SpecialType.WakeUp;
        } else if (Arrays.asList(speakMusicArray).contains(speakTxt) ||
                Arrays.asList(resFoFinal(activity, R.array.other_misic)).contains(speakTxt)) {
            return SpecialType.Music;
        } else if (Arrays.asList(speakStoryArray).contains(speakTxt) ||
                Arrays.asList(resFoFinal(activity, R.array.other_story)).contains(speakTxt)) {
            return SpecialType.Story;
        } else if (Arrays.asList(speakJokeArray).contains(speakTxt) ||
                Arrays.asList(resFoFinal(activity, R.array.other_joke)).contains(speakTxt)) {
            return SpecialType.Joke;
        } else if (Arrays.asList(speakForwardArray).contains(speakTxt)) {
            return SpecialType.Forward;
        } else if (Arrays.asList(speakBackoffArray).contains(speakTxt)) {
            return SpecialType.Backoff;
        } else if (Arrays.asList(speakTurnleftArray).contains(speakTxt)) {
            return SpecialType.Turnleft;
        } else if (Arrays.asList(speakTurnrightArray).contains(speakTxt)) {
            return SpecialType.Turnright;
        } else if (Arrays.asList(speakBusinessService).contains(speakTxt)) {
            return SpecialType.BusinessService;
        } else if (Arrays.asList(speakCostService).contains(speakTxt)) {
            return SpecialType.CostService;
        } else if (Arrays.asList(speakRegisterService).contains(speakTxt)) {
            return SpecialType.RegisterService;
        } else if (Arrays.asList(speakIllegalrService).contains(speakTxt)) {
            return SpecialType.IllegalrService;
        } else if (Arrays.asList(speakIdentityService).contains(speakTxt)) {
            return SpecialType.IdentityService;
        } else if (Arrays.asList(speakMoneyService).contains(speakTxt)) {
            return SpecialType.MoneyService;
        } else if (Arrays.asList(speakExitArray).contains(speakTxt)) {
            return SpecialType.Exit;
        }
//        if (Arrays.asList(speakLocalVoiceArray).contains(speakTxt)) {
//        return SpecialType.LocalVoice;
        return SpecialType.NoSpecial;
    }

    public static SpecialType doesExistVoice(Activity activity, String speakTxt) {
        if (Arrays.asList(speakExitArray).contains(speakTxt)) {
            return SpecialType.Exit;
        }
        return SpecialType.NoSpecial;
    }


    private static String[] resFoFinal(Activity activity, int id) {
        String[] res = activity.getResources().getStringArray(id);
        return res;
    }
}
