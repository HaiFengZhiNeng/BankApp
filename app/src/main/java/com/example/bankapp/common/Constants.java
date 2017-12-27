package com.example.bankapp.common;

import android.os.Environment;

import java.io.File;

public class Constants {

    public static int displayWidth;
    public static int displayHeight;
    public static int IMSDK_APPID = 1400043768;

    public static int IMSDK_ACCOUNT_TYPE = 17967;
//    public static String YUNTX_APPID = "8aaf07085ea24877015eb77133e20637";

    public static String mySpeechType;
    //exit
    public static final String EXIT_APP = "EXIT_APP";

    public static final String NET_LOONGGG_EXITAPP = "net.loonggg.exitapp";
    //xf
    public static final String PREFER_NAME = "com.iflytek.setting";

    public static final String IAT_LANGUAGE_PREFERENCE = "iat_language_preference";
    public static final String IAT_LANGUAGE_TALKER = "iat_language_talker";
    //    public static String YUNTX_APPTOKEN = "969f99003f9099f33c57970c5e9ebd32";
    public static final String IAT_LOCAL_UPDATELEXICON = "iat_local_updatelexicon";
    public static final String IAT_CLOUD_UPDATELEXICON = "iat_cloud_updatelexicon";
    public static final String IAT_CLOUD_BUILD = "iat_cloud_build";
    public static final String IAT_LOCAL_BUILD = "iat_local_build";

    //udp service
    public static final String UDP_ACCEPT_ACTION = "android.receiver.udpAcceptReceiver";
    public static final String UDP_SEND_ACTION = "android.receiver.udpSendReceiver";

    //udp
    public static String IP;
    public static int PORT;
    public static String CONNECT_IP = null;
    public static int CONNECT_PORT = 0;

    //music
    public final static String CHANGE_MUSIC_SONG = "change.music.song";
    public final static String CHANGE_PLAYED = "change.music.played";

    private static final String M_SDROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String PROJECT_PATH = M_SDROOT_PATH + File.separator + "fangfangBig" + File.separator;

    public static final String GRM_PATH = PROJECT_PATH + "msc";
    public static final String LOG_PATH = PROJECT_PATH + "log" + File.separator;

    public static boolean unusual = true;

    public static class API {

        public static final String OR_CODE = "https://filezoo.cloudbeing.cn/WisdomPlayFSServer/upfilesZoo";

    }

    public static class ACTION {
        public static final String[] action = {"双臂向左欢迎", "双臂向右欢迎", "运动跑步-右手在前", "运动跑步 左手在前",
                "萌-侧开臂", "打招呼-右手", "打招呼-左手", "欢迎-双臂张开", "握手-右手", "加油", "看书-左手看书",
                "拥抱", "可爱", "右看", "左看", "伸左手", "伸右手", "嫌弃1", "嫌弃2",
                "伸左手，向左指示", "伸右手，向右指示", "连续动作：向左指示，向右指示",
                "连续动作：舞蹈摆臂", "连续动作：舞蹈画圈", "连续动作：左指示", "连续动作：右指示",
                "连续动作：运动", "连续动作：握手", "连续动作：可爱",
                "连续动作：拥抱", "连续动作：向左向右指示", "开始跳舞", "停止跳舞"};
        public static final String[] action_order = {"A50C8001AA", "A50C8002AA", "A50C8003AA", "A50C8004AA",
                "A50C8005AA", "A50C8006AA", "A50C8007AA", "A50C8008AA", "A50C8009AA", "A50C800AAA",
                "A50C800BAA", "A50C800CAA", "A50C800EAA", "A50C8010AA", "A50C8011AA", "A50C8012AA",
                "A50C8013AA", "A50C8014AA", "A50C8015AA", "A50C8016AA", "A50C8017AA", "A50C8018AA",
                "A50C8019AA", "A50C801AAA", "A50C8021AA", "A50C8022AA",
                "A50C8023AA", "A50C8024AA", "A50C8025AA",
                "A50C8026AA", "A50C8027AA", "A50C80E1AA", "A50C80E2AA"};

        public static final String[] expression = {"微笑", "萌", "下看", "右看", "左看", "滑稽", "大哭", "心", "疑惑", "委屈", "呆"};
        public static final String[] expression_data = {"A5028001AA", "A5028002AA", "A5028003AA", "A5028004AA", "A5028005AA", "A5028006AA",
                "A5028007AA", "A5028008AA", "A5028009AA", "A502800AAA", "A502800BAA"};

        public static final String[] navigation = {"去first", "去second", "去third", "去forth", "去fifth", "去sixth"};
        public static final String[] navigation_data = {"first", "second", "third", "forth", "fifth", "sixth"};

        public static final String[] dataType = {"本地语音", "本地导航"};
    }


}
