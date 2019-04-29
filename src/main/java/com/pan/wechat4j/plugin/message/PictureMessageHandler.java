package com.pan.wechat4j.plugin.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blade.kit.json.JSONObject;
import com.pan.wechat4j.core.WechatMeta;

public class PictureMessageHandler extends AbstractMessageHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(PictureMessageHandler.class);

	public PictureMessageHandler(WechatMeta meta) {
		super(meta);
		this.meta = meta;
	}

	@Override
	public void process(JSONObject msg) {
		LOGGER.info("图片消息暂不处理");
//		download(msg, MsgType.PICTURE);

	}
}
