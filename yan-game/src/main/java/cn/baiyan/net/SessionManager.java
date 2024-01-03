package cn.baiyan.net;

import cn.baiyan.message.IdSession;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public enum SessionManager {

    /**
     * 枚举单例
     */
    INSTANCE;

    /**
     * distributeKey auto generator
     */
    private AtomicInteger distributeKeyGenerator = new AtomicInteger();

    /**
     * key=playerId, value=session
     */
    private ConcurrentHashMap<String, IdSession> players2sessions = new ConcurrentHashMap<>();

    public void registerNewPlayer(String playerId, IdSession session) {
        // build playerId to Session
        session.setAttribute(IdSession.ID, playerId);
        this.players2sessions.put(playerId, session);
    }

    /**
     * get session's playerId
     */
    public long getPlayerIdBy(IdSession session) {
        if (session != null) {
            return session.getOwnerId();
        }
        return 0;
    }

    public IdSession getSessionBy(String playerId) {
        return players2sessions.get(playerId);
    }

    /**
     * get appointed sessionAttr
     */
    @SuppressWarnings("uncheck")
    public <T> T getSessionAttr(IoSession session, AttributeKey attributeKey, Class<T> attrType) {
        return (T) session.getAttribute(attributeKey, attrType);
    }

    public int getNextSessionId() {
        return this.distributeKeyGenerator.getAndIncrement();
    }

    public String getRemoteIp(IoSession session) {
        return ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
    }


}
