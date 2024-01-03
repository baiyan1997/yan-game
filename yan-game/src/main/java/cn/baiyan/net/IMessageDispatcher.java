package cn.baiyan.net;

import cn.baiyan.message.IdSession;
import cn.baiyan.message.Message;

/**
 * 消息分发器
 */
public interface IMessageDispatcher {

    void onSessionCreated(IdSession session);

    /**
     * 选择哪个线程去处理
     * message entrance, in which io thread dispatch message
     */
    void dispatch(IdSession session, Message message);

    /**
     * 分发session关闭事件
     */
    void onSessionClosed(IdSession session);


}
