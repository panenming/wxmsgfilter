package com.pan.wechat4j.plugin.message;

import com.blade.kit.json.JSONObject;
import com.pan.wechat4j.config.Constant;
import com.pan.wechat4j.core.WechatApiUtil;
import com.pan.wechat4j.core.WechatMeta;
import com.pan.wechat4j.plugin.WxLocalCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class TxtMessageHandler extends AbstractMessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TxtMessageHandler.class);
    private static Pattern pattern = Pattern.compile(Constant.REP);


    public TxtMessageHandler(WechatMeta meta) {
        super(meta);
        this.meta = meta;
    }

    @Override
    public void process(JSONObject msg) {
        String content = msg.getString("Content");
        if (content == null) {
            return;
        }
        if (!preHandle(msg)) {
            return;
        }

        if (filterByRe(content)) {
            String groupNickName = WxLocalCache.instance().getGroupRemarkName(msg);
            String memberNickName = WxLocalCache.instance().getMemberNickName(msg);
            // 消息去除杂质
            content = content.replaceAll("^@\\w+:<br/>", "");
            LOGGER.info("msg:{}", msg);
            content += "[来自群:" + groupNickName + ",用户:" + memberNickName + "]";
            // 转发给自己的filehelper
            WechatApiUtil.webwxsendmsg(this.meta, content, "filehelper");
        }
    }

    /**
     * 使用正则表达式判定
     *
     * @param content
     * @return
     */
    private boolean filterByRe(String content) {
        return pattern.matcher(content).find();
    }

    public static void main(String[] args) {
        String content = "晚8";
        Pattern pattern = Pattern.compile("(车寻人)*.*(((晚\\w*|今\\w*晚\\w*)+(8|20))|20:)+.*(十里河|分钟寺|九龙山|平乐园|将台)+");
        System.out.println(pattern.matcher(content).find());
    }

}
