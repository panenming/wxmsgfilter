package com.pan.wechat4j.plugin.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blade.kit.json.JSONObject;
import com.pan.wechat4j.core.WechatMeta;

public class MpMessageHandler extends AbstractMessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoiceMessageHandler.class);

    public MpMessageHandler(WechatMeta meta) {
        super(meta);
        this.meta = meta;
    }

    @Override
    public void process(JSONObject msg) {
        LOGGER.info("分享名片暂不处理");
    }


}
