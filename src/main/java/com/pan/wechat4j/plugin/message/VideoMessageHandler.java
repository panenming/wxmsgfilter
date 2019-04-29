package com.pan.wechat4j.plugin.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blade.kit.json.JSONObject;
import com.pan.wechat4j.core.WechatMeta;

public class VideoMessageHandler extends AbstractMessageHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(VideoMessageHandler.class);

	public VideoMessageHandler(WechatMeta meta) {
		super(meta);
		this.meta = meta;
	}

	@Override
	public void process(JSONObject msg) {
		LOGGER.info("视频消息暂不处理");
//		download(msg, MsgType.VIDEO);
	}



}
