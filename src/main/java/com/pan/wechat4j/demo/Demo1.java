package com.pan.wechat4j.demo;

import com.pan.wechat4j.WechatStartup;
import com.pan.wechat4j.config.Constant;
import com.pan.wechat4j.core.WechatMeta;
import com.pan.wechat4j.plugin.MessageListener;

public class Demo1 {

    public static void main(String[] args) {
        if (args.length <= 0) {
            System.out.println("需要有正则表达式！");
        }
        System.out.println("正则表达式，" + args[0]);
        Constant.REP = args[0];
        test3();
    }

    // 监听消息
    public static void test3() {
        WechatMeta meta = WechatStartup.login();
        MessageListener listener = new MessageListener(meta);
        listener.listen();
    }

}