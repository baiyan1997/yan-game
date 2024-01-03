package cn.baiyan.net;

import cn.baiyan.annocation.MessageMeta;
import cn.baiyan.message.Message;
import cn.baiyan.utils.ClassScanner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum MessageFactory {

    /**
     * 枚举单例
     */
    INSTANCE;

    private Map<Integer, Class<?>> id2Clazz = new HashMap<>();

    private Map<Class<?>, Integer> clazz2Id = new HashMap<>();

    /**
     * scan all message class and register into pool
     */
    public void initMessagePool(String scanPath) {
        Set<Class<?>> messages = ClassScanner.listAllSubclasses(scanPath, Message.class);
        for (Class<?> clazz : messages) {
            MessageMeta meta = clazz.getAnnotation(MessageMeta.class);
            if (meta == null) {
                throw new RuntimeException("messages[" + clazz.getSimpleName() + "] messed MessageMate annotation");
            }
            int key = buildKey(meta.module(), meta.cmd());
            if (id2Clazz.containsKey(key)) {
                throw new RuntimeException("message meta[" + key + "] duplicate! !");
            }
            id2Clazz.put(key, clazz);
            clazz2Id.put(clazz, key);

        }
    }

    private int buildKey(short module, byte cmd) {
        int result = Math.abs(module) * 1000 + Math.abs(cmd);
        return cmd < 0 ? -result : result;
    }

    public Class<?> getMessage(short module, byte cmd) {
        return id2Clazz.get(buildKey(module, cmd));
    }
}

