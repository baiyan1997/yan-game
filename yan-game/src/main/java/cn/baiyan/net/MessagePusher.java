package cn.baiyan.net;

import cn.baiyan.message.IdSession;
import cn.baiyan.message.Message;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;

import java.util.Collection;

public class MessagePusher {

    public static void pushMessage(String userId, JSONObject message) {
        IdSession userSession = SessionManager.INSTANCE.getSessionBy(userId);
        pushMessage(userSession, message);
    }

    public static void pushMessage(Collection<String> playerIds, JSONObject message) {
//        for (long playerId : playerIds) {
//            pushMessage(playerId, message);
//        }
    }

    public static void pushMessage(IdSession session, JSONObject message) {
        if (session == null || message == null) {
            return;
        }
        session.sendPacket(message);
    }

//    public static void notify2Player(IoSession session, int i18nId) {
//        ConfigNoticeStorage noticeStorage = ConfigDataPool.getInstance().getStorage(ConfigNoticeStorage.class);
//        ConfigNotice idResource = noticeStorage.getNoticeBy(i18nId);
//        if (idResource != null) {
////            MessagePusher.pushMessage(session, new RespMsg(idResource.getContent()));
//        }
//    }
//
//    public static void notify2Player(IoSession session, int i18nId, Object... args) {
//        ConfigNoticeStorage noticeStorage = ConfigDataPool.getInstance().getStorage(ConfigNoticeStorage.class);
//        ConfigNotice idResource = noticeStorage.getNoticeBy(i18nId);
//        if (idResource != null) {
//            String content = String.format(idResource.getContent(), args);
//        }
//    }


}
