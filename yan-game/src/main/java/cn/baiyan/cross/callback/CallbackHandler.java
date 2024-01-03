package cn.baiyan.cross.callback;

import cn.baiyan.cross.SCSession;
import cn.baiyan.message.Message;

import java.util.HashMap;
import java.util.Map;

public abstract class CallbackHandler {

    private static Map<Integer, CallbackHandler> handlers = new HashMap<>();

    public static void register(CallbackHandler handler) {
        handlers.put(handler.cmdType(), handler);
    }

    public abstract void onRequest(SCSession session, G2FCallBack req);

    public void sendBack(SCSession session, G2FCallBack req, Message response) {
        F2GCallBack callBack = F2GCallBack.valueOf(response);
        callBack.setIndex(req.getIndex());
        session.sendMessage(callBack);
    }

    public abstract int cmdType();

    public static CallbackHandler queryHandler(int type) {
        return handlers.get(type);
    }

}
