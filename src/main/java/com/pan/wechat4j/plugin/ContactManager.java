package com.pan.wechat4j.plugin;

import com.blade.kit.DateKit;
import com.blade.kit.StringKit;
import com.blade.kit.http.HttpRequest;
import com.blade.kit.json.JSONArray;
import com.blade.kit.json.JSONKit;
import com.blade.kit.json.JSONObject;
import com.pan.wechat4j.core.WechatMeta;
import com.pan.wechat4j.exception.WechatException;
import com.pan.wechat4j.util.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactManager {

    public static Logger LOGGER = LoggerFactory.getLogger(ContactManager.class);
    private WechatMeta meta;

    private HttpClient httpRequsetUtil;

    public ContactManager(WechatMeta meta) {
        super();
        this.meta = meta;
        this.httpRequsetUtil = new HttpClient(meta);
    }

    /**
     * 获取通讯录列表
     */
    public JSONArray getContactList() {
        String url = meta.getBase_uri() + "/webwxgetcontact?&seq=0&pass_ticket=" + meta.getPass_ticket() + "&skey=" + meta.getSkey() + "&r="
                + DateKit.getCurrentUnixTime();
        JSONObject body = new JSONObject();
        body.put("BaseRequest", meta.getBaseRequest());
        JSONObject response = httpRequsetUtil.postJSON(url, body);
        JSONArray memberList = response.get("MemberList").asArray();
        return memberList;

    }

    public void initMember(String chatRommName) {
        JSONArray groupArray = new JSONArray();
        JSONObject groupItem = new JSONObject();
        groupItem.put("UserName", chatRommName);
        groupItem.put("ChatRoomId", "");
        groupArray.add(groupItem);
        JSONObject latestChatRoom = getMemberListByChatroom(groupArray);
        Storage.instance().getChatRoomMap().put(chatRommName, latestChatRoom);

    }

    public JSONObject getMemberListByChatroom(JSONArray chatRoomList) {
        String url = meta.getBase_uri() + "/webwxbatchgetcontact?type=ex&r=" + DateKit.getCurrentUnixTime() + "&lang=zh_CN";
        JSONObject body = new JSONObject();
        body.put("BaseRequest", meta.getBaseRequest());
        body.put("Count", chatRoomList.size());
        body.put("List", chatRoomList);
        HttpRequest request = HttpRequest.post(url).contentType("application/json;charset=utf-8").header("Cookie", meta.getCookie())
                .send(body.toString());
        String res = request.body();
        request.disconnect();
        if (StringKit.isBlank(res)) {
            throw new WechatException("获取群成员列表失败");
        }
        JSONObject jsonObject = JSONKit.parseObject(res);
        JSONObject baseResponse = jsonObject.get("BaseResponse").asJSONObject();
        if (null == baseResponse || baseResponse.getInt("Ret", -1) != 0) {
            LOGGER.warn("获取群列表失败,{}", baseResponse);
            return null;
        }
        LOGGER.info("获取群成员:{}", jsonObject.get("ContactList"));
        return jsonObject.get("ContactList").asArray().get(0).asJSONObject();
    }

    public WechatMeta getMeta() {
        return meta;
    }
}
