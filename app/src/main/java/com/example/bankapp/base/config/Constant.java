package com.example.bankapp.base.config;

/**
 * Created by dell on 2017/7/27.
 */

public class Constant {
    /**
     * 账号
     */
    public static final String LOGIN_NAME = "bank_test";
    /**
     * 对方账号
     */
    public static final String OTHERS_NAME = "bank_testl_control";


    /**
     * 昵称
     */
    public static final String EXTRA_CALL_NAME = "com.haifeng.robot.VoIP_CALL_NAME";
    /**
     * 通话号码
     */
    public static final String EXTRA_CALL_NUMBER = "com.haifeng.robot.VoIP_CALL_NUMBER";
    /**
     * 呼入方或者呼出方
     */
    public static final String EXTRA_OUTGOING_CALL = "com.haifeng.robot.VoIP_OUTGOING_CALL";
    /**
     * VoIP呼叫
     */
    public static final String ACTION_VOICE_CALL = "com.haifeng.robot.intent.ACTION_VOICE_CALL";
    /**
     * Video呼叫
     */
    public static final String ACTION_VIDEO_CALL = "com.haifeng.robot.intent.ACTION_VIDEO_CALL";
    public static final String ACTION_CALLBACK_CALL = "com.haifeng.robot.intent.ACTION_VIDEO_CALLBACK";
    /**
     * 是否正在呼叫
     */
    public static final String ACTION_CALLBACKING = "com.haifeng.robot.intent.ACTION_VIDEO_CALLING";


    public static final String UDP_ACCEPT_ACTION = "android.receiver.udpAcceptReceiver";
    public static final String UDP_SEND_ACTION = "android.receiver.udpSendReceiver";

    public static String IP;
    public static int PORT;
    public static String CONNECT_IP = null;
    public static int CONNECT_PORT = 0;


    //music
    public final static String CHANGE_MUSIC_SONG = "change.music.song";
    public final static String CHANGE_PLAYED = "change.music.played";

}