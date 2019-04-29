package com.pan.wechat4j.plugin.message;

import com.blade.kit.json.JSONObject;
import com.pan.wechat4j.config.Constant;
import com.pan.wechat4j.core.WechatMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMessageHandler implements IMessageHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractMessageHandler.class);
    protected WechatMeta meta;


    public AbstractMessageHandler(WechatMeta meta) {
        super();
        this.meta = meta;
    }


    public boolean preHandle(JSONObject msg) {

        if (Constant.FILTER_USERS.contains(msg.getString("ToUserName"))) {
            LOGGER.info("你收到一条被过滤的消息");
            return false;
        }
        if (isSlefSend(msg)) {
            LOGGER.info("你发送了一条消息 ");
            return true;
        }

        if (msg.getString("FromUserName").startsWith("@@")) {
            LOGGER.info("您收到一条群聊消息");
            return true;
        }
        if (msg.getString("FromUserName").startsWith("@")) {
            LOGGER.info("您收到一条好友消息");
            return true;
        }
        LOGGER.warn("您收到一条 未知类型消息:{}", msg.toString());
        return true;

    }

    public boolean isSlefSend(JSONObject msg) {
        return msg.getString("FromUserName").equals(this.meta.getUser().getString("UserName"));
    }
}
