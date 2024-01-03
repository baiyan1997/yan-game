package cn.baiyan.message;

import org.bson.json.JsonObject;
import org.json.JSONObject;

import java.net.InetSocketAddress;

/**
 * 玩家登录session，不与任何nio框架绑定
 */
public interface IdSession {

    String ID = "ID";

    void sendPacket(JSONObject packet);

    long getOwnerId();

    InetSocketAddress getRemoteAddress();

    String getRemoteIP();

    int getRemotePort();

    InetSocketAddress getLocalAddress();

    int getLocalPort();

    /**
     * 更新属性
     */
    Object setAttribute(String key, Object value);

    /**
     * 修改属性值
     */
    Object getAttribute(String key);
}
