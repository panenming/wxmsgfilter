package com.pan.wechat4j.plugin.message;

import com.blade.kit.json.JSONObject;

public interface IMessageHandler {
	public void process(JSONObject msg);


}
