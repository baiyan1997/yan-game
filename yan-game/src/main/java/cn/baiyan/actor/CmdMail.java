package cn.baiyan.actor;

import cn.baiyan.message.IdSession;
import cn.baiyan.message.Message;
import org.checkerframework.checker.units.qual.C;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 将客户端消息封装成一个邮箱，放到actor的邮箱里
 */
public class CmdMail implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(CmdMail.class);

    private IdSession session;

    /**
     * message controller
     **/
    private Object handler;

    /**
     * target method of the controller
     */
    private Method method;

    /**
     * arguments passed to the method
     */
    private Object[] params;

    public static CmdMail valueOf(IdSession session, Object handler, Method method, Object[] params) {
        CmdMail msgTask = new CmdMail();
        msgTask.session = session;
        msgTask.handler = handler;
        msgTask.method = method;
        msgTask.params = params;
        return msgTask;
    }


    @Override
    public void run() {
        try {
            Object response = method.invoke(handler, params);
            if (response != null) {
                session.sendPacket((JSONObject) response);
            }

        } catch (Exception e) {
            logger.error("message task execute failed", e);
        }
    }

    public Object getHandler() {
        return handler;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getParams() {
        return params;
    }

    @Override
    public String toString() {
        return  "[" + handler.getClass().getName() + "@" + method.getName() + "]";
    }
}
