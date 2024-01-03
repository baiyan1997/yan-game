package cn.baiyan.net;

import cn.baiyan.game.json.JsonUtil;
import cn.baiyan.message.IdSession;
import cn.baiyan.message.Message;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class NettySession implements IdSession {

    public static Logger logger = LoggerFactory.getLogger(NettySession.class);

    /**
     * 网络连接channel
     */
    private Channel channel;

    @Override
    public void sendPacket(JSONObject packet) {
        logger.info("send packet content:" + packet);
        channel.writeAndFlush(new TextWebSocketFrame(packet.toString()));
    }

    @Override
    public long getOwnerId() {
        return 0;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public String getRemoteIP() {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public Object setAttribute(String key, Object value) {
        return null;
    }

    @Override
    public Object getAttribute(String key) {
        return null;
    }

    public NettySession(Channel channel) {
        this.channel = channel;
    }
}
