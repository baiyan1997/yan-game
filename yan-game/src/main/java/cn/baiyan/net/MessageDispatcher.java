package cn.baiyan.net;

import cn.baiyan.GameContext;
import cn.baiyan.actor.CmdMail;
import cn.baiyan.actor.MailBox;
import cn.baiyan.annocation.MessageMeta;
import cn.baiyan.game.module.user.PlayerEnt;
import cn.baiyan.message.IdSession;
import cn.baiyan.message.Message;
import cn.baiyan.thread.ThreadCenter;
import cn.baiyan.utils.ClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MessageDispatcher implements IMessageDispatcher {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final Map<String, CmdExecutor> MODULE_CMD_HANDLERS = new HashMap<>();

    public MessageDispatcher(String scanPath) {
        initialize(scanPath);
    }

    private void initialize(String scanPath) {
        Set<Class<?>> controllers = ClassScanner.listClassesWithAnnotation(scanPath, Controller.class);

        for (Class<?> controller : controllers) {
            try {
                Object handler = controller.newInstance();
                Method[] methods = controller.getDeclaredMethods();
                for (Method method : methods) {
                    RequestMapping mapperAnnotation = method.getAnnotation(RequestMapping.class);
                    if (mapperAnnotation != null) {
                        short[] meta = getMessageMeta(method);
                        if (meta == null) {
                            throw new RuntimeException(String.format("controller[%s] method[%s] lack of" +
                                    " RequestMapping annotation", controller.getName(), method.getName()));
                        }
                        short module = meta[0];
                        short cmd = meta[1];
                        String key = buildKey(module, cmd);
                        CmdExecutor cmdExecutor = MODULE_CMD_HANDLERS.get(key);
//                        if (cmdExecutor != null) {
//                            throw new RuntimeException(String.format("module[%d] cmd[%d] duplicated", module, cmd));
//                        }

                        cmdExecutor = CmdExecutor.valueOf(method, method.getParameterTypes(), handler);
                        MODULE_CMD_HANDLERS.put(key, cmdExecutor);
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    private short[] getMessageMeta(Method method) {
        for (Class<?> paramClazz : method.getParameterTypes()) {
            if (Message.class.isAssignableFrom(paramClazz)) {
                MessageMeta protocol = paramClazz.getAnnotation(MessageMeta.class);
                if (protocol != null) {
                    short[] meta = {protocol.module(), protocol.cmd()};
                    return meta;
                }
            }
        }
        return null;
    }

    private String buildKey(short module, short cmd) {
        return module + "-" + cmd;
    }

    @Override
    public void onSessionCreated(IdSession session) {
        session.setAttribute(SessionProperties.DISTRIBUTE_KEY, SessionManager.INSTANCE.getNextSessionId());
    }

    @Override
    public void dispatch(IdSession session, Message message) {
        short module = message.getModule();
        short cmd = message.getCmd();

        CmdExecutor cmdExecutor = MODULE_CMD_HANDLERS.get(buildKey(module, cmd));
        if (cmdExecutor == null) {
            logger.error("message executor messed, module={},cmd={}", module, cmd);
            return;
        }

        Object[] params = convertToMethodParams(session, cmdExecutor.getParams(), message);
        Object controller = cmdExecutor.getHandler();

        CmdMail task = CmdMail.valueOf(session, controller, cmdExecutor.getMethod(), params);
        // 丢到任务消息队列，不在io线程进行业务处理
        selectMailQueue(session, message).receive(task);
    }

    private MailBox selectMailQueue(IdSession session, Message message) {
        MailBox mailBox = message.mailQueue();
        if (mailBox != null) {
            return mailBox;
        }
        long playerId = session.getOwnerId();
        if (playerId > 0) {
//            PlayerEnt player = GameContext.playerManager.get(playerId);
//            return player.mailBox();
        }
        return ThreadCenter.getLoginQueue().getSharedMailQueue(session.hashCode());
    }

    /**
     * 将各种参数转化为被RequestMapping注解的方法的实参
     */
    private Object[] convertToMethodParams(IdSession session, Class<?>[] methodParams, Message message) {
        Object[] result = new Object[methodParams == null ? 0 : methodParams.length];
        for (int i = 0; i < result.length; i++) {
            Class<?> param = methodParams[i];
            if (IdSession.class.isAssignableFrom(param)) {
                result[i] = session;
            } else if (Long.class.isAssignableFrom(param)) {
                result[i] = session.getOwnerId();
            } else if (long.class.isAssignableFrom(param)) {
                result[i] = session.getOwnerId();
            } else if (Message.class.isAssignableFrom(param)) {
                result[i] = message;
            }
        }
        return result;
    }

    @Override
    public void onSessionClosed(IdSession session) {

    }
}
