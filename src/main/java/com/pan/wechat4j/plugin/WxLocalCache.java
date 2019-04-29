package com.pan.wechat4j.plugin;

import com.blade.kit.StringKit;
import com.blade.kit.json.JSONArray;
import com.blade.kit.json.JSONObject;
import com.blade.kit.json.JSONValue;
import com.pan.wechat4j.core.WechatMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WxLocalCache {

    private final String CONTACT = "webwxgetcontact";
    private final String LATEST_CONTACT = "latest_webwxgetcontact";

    private Map<String, CacheItem> cacheInstance;
    private ContactManager contactManager;

    private Logger logger = LoggerFactory.getLogger(WxLocalCache.class);

    private WxLocalCache(WechatMeta meta) {
        super();
        this.cacheInstance = new HashMap<String, CacheItem>();
        this.contactManager = new ContactManager(meta);
    }

    static class Holder {
        static WxLocalCache instance = null;
    }

    public static WxLocalCache instance() {
        if (Holder.instance == null) {
            throw new IllegalStateException("wxLocalCache not init");
        }
        return Holder.instance;

    }

    public static WxLocalCache init(WechatMeta meta) {
        if (Holder.instance != null) {
            return Holder.instance;
        }
        Holder.instance = new WxLocalCache(meta);
        return Holder.instance;
    }

    public JSONArray getContactList() {
        JSONArray arr = get(CONTACT);
        if (arr == null && (arr = contactManager.getContactList()) == null) {
            logger.error("获取联系人失败");
            return null;
        }
        return arr;
    }

    public JSONObject getContactByRemarkName(String remarkName) {
        return getContactByName("RemarkName", remarkName);
    }

    public JSONObject getContactByNickName(String nickName) {
        return getContactByName("NickName", nickName);
    }

    public JSONObject getContactByUserName(String ContactName) {
        return getContactByName("UserName", ContactName);
    }

    private JSONObject getContactByName(String fieldName, String fieldVal) {
        JSONArray arr = getContactList();
        if (arr == null) {
            return null;
        }

        for (int i = 0; i < arr.size(); i++) {
            JSONObject val = arr.get(i).asJSONObject();
            String userName = val.getString("UserName");
            String nickName = val.getString("NickName");
            String remarkName = val.getString("RemarkName");
            if (fieldName.equals("UserName") && userName.equals(fieldVal)) {
                return val;
            } else if (fieldName.equals("NickName") && nickName.equals(fieldVal)) {
                return val;
            } else if (fieldName.equals("RemarkName") && remarkName.equals(fieldVal)) {
                return val;
            }
        }
        logger.error("can't find the contact of  " + fieldName + " named  " + fieldName);
        return null;

    }

    public void put(String key, Object data) {
        CacheItem item = new CacheItem(key, data);
        cacheInstance.put(key, item);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        CacheItem item = cacheInstance.get(key);
        if (item == null) {
            return null;
        }
        return (T) item.getData();

    }

    public static class CacheItem {

        private String key;
        private Object data;
        private long timestamp;

        public CacheItem(String key, Object data) {
            this.key = key;
            this.data = data;
            this.timestamp = System.currentTimeMillis();

        }

        public String getKey() {
            return key;
        }

        public Object getData() {
            return data;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }


    public void setLatestContactList(JSONArray contactList) {
        put(LATEST_CONTACT, contactList);

        for (int i = 0; i < contactList.size(); i++) {
            JSONObject item = contactList.get(i).asJSONObject();
            logger.info("初始化最新联系人 ,item:{}", item.getString("NickName"));
            String userName = item.getString("UserName");
            // 最近联系的群
            if (userName.startsWith("@@")) {
                Storage.instance().addLasetChatroomUserName(userName);
                continue;
            }
        }

    }

    public String getUserRemarkName(String id) {
        String name = "";
        for (int i = 0, len = Storage.instance().getAllContact().size(); i < len; i++) {
            JSONObject member = Storage.instance().getAllContact().get(i).asJSONObject();
            if (!member.getString("UserName").equals(id)) {
                continue;
            }
            if (StringKit.isNotBlank(member.getString("RemarkName"))) {
                name = member.getString("RemarkName");
            } else {
                name = member.getString("NickName");
            }
            return name;
        }
        return name;
    }

    public String getMemberRemarkName(String chatRoomUserName, String memberName) {
        return getNickName(chatRoomUserName, memberName, 0);
    }

    public String getGroupRemarkName(JSONObject msg) {
        String chatRoomUserName;
        if (isSlefSend(msg)) {
            chatRoomUserName = msg.getString("ToUserName");
            return getNickName(chatRoomUserName, chatRoomUserName, 1);
        } else if (isGroupMsg(msg)) {
            chatRoomUserName = msg.getString("FromUserName");
            return getNickName(chatRoomUserName, chatRoomUserName, 1);
        } else {
            // 单人聊天的情况
            String nickName = WxLocalCache.instance().getContactByUserName(msg.getString("FromUserName")).getString("NickName");
            return "双人聊天[" + nickName + "]";
        }

    }

    private String getNickName(String chatRoomUserName, String memberName, int type) {
        JSONObject chatRoom = Storage.instance().getChatRoomMap().get(chatRoomUserName);
        if (chatRoom == null || chatRoom.isEmpty()) {
            // 初始化该数据
            contactManager.initMember(chatRoomUserName);
        }
        chatRoom = Storage.instance().getChatRoomMap().get(chatRoomUserName);
        if (chatRoom != null) {
            if (type == 1) {
                if (memberName.equals(chatRoom.getString("UserName"))) {
                    return chatRoom.getString("NickName");
                }
            } else {
                JSONArray members = chatRoom.get("MemberList").asArray();
                for (JSONValue jo : members) {
                    if (jo != null && memberName.equals(jo.asJSONObject().getString("UserName"))) {
                        return jo.asJSONObject().getString("NickName");
                    }
                }
            }

        } else {
            return "未知";
        }
        Storage.instance().getChatRoomMap().remove(chatRoomUserName);
        return "未知";
    }

    public boolean isSlefSend(JSONObject msg) {
        return msg.getString("FromUserName").equals(contactManager.getMeta().getUser().getString("UserName"));
    }

    public String getSelfNickName() {
        return contactManager.getMeta().getUser().getString("NickName");
    }

    public String getGroupMemberName(JSONObject msg) {
        String memberUserName = msg.getString("Content").split(":")[0];
        return getMemberRemarkName(msg.getString("FromUserName"), memberUserName);
    }

    public String getMemberNickName(JSONObject msg) {
        if (isSlefSend(msg)) {
            return getSelfNickName();
        } else if (isGroupMsg(msg)) {
            return getGroupMemberName(msg);
        } else {
            return getUserRemarkName(msg.getString("FromUserName"));
        }
    }

    public boolean isGroupMsg(JSONObject msg) {
        return msg.getString("FromUserName").startsWith("@@");
    }
}