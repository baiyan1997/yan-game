package cn.baiyan.cross;

import cn.baiyan.message.Message;

public interface CMessageDispatcher {

    /**
     * 服务端节点消息分发
     */
    void serverDispatch(SCSession session, Message message);

    /**
     * 客户端节点消息分支
     */
    void clientDispatch(CCSession session, Message message);
}
