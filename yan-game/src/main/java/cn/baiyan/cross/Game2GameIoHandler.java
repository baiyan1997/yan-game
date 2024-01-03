package cn.baiyan.cross;

import cn.baiyan.message.Message;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game2GameIoHandler extends IoHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(Game2GameIoHandler.class);

    /**
     * 消息分发器
     */
    private CMessageDispatcher messageDispatcher;

    private AttributeKey attrKey = new AttributeKey(MinaSessionProperties.class,"session_container");

    public Game2GameIoHandler(CMessageDispatcher messageDispatcher){
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public void sessionCreated(IoSession session){
        SCSession sessionContainer = SCSession.valueOf(session);
        session.setAttribute(attrKey, sessionContainer);
    }

    @Override
    public void messageReceived(IoSession session, Object data) throws  Exception{
        SCSession sessionContainer = (SCSession) session.getAttribute(attrKey);
        Message message  = (Message)data;
        // 交给消息分发器处理
        messageDispatcher.serverDispatch(sessionContainer, message);
    }

    @Override
    public void sessionClosed(IoSession session) throws  Exception{
        logger.error("跨服session关闭");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)throws  Exception{
        logger.error("server exception", cause);
    }
}
