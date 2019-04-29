package com.pan.wechat4j.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constant {

    // 特殊用户 须过滤
    public static final List<String> FILTER_USERS = Arrays.asList("newsapp", "fmessage", "weibo", "qqmail", "fmessage", "tmessage",
            "qmessage", "qqsync", "floatbottle", "lbsapp", "shakeapp", "medianote", "qqfriend", "readerapp", "blogapp", "facebookapp", "masssendapp",
            "meishiapp", "feedsapp", "voip", "blogappweixin", "weixin", "brandsessionholder", "weixinreminder", "wxid_novlwrv3lqwv11",
            "gh_22b87fa7cb3c", "officialaccounts", "notification_messages", "wxid_novlwrv3lqwv11", "gh_22b87fa7cb3c", "wxitil",
            "userexperience_alarm", "notification_messages");

    public static final String[] SYNC_HOST = {"webpush.weixin.qq.com", "webpush2.weixin.qq.com", "webpush.wechat.com", "webpush1.wechat.com",
            "webpush2.wechat.com"};

    /***** 是否打印成员信息 ****/
    public static final boolean PRINT_MEMBER_INFO = false;

    public static final String HTTP_OK = "200";
    public static final String BASE_URL = "https://webpush2.weixin.qq.com/cgi-bin/mmwebwx-bin";
    public static final String JS_LOGIN_URL = "https://login.weixin.qq.com/jslogin";
    public static final String QRCODE_URL = "https://login.weixin.qq.com/qrcode/";

    /**
     * 需要过滤的单词
     */
    public static String REP = "";

}
