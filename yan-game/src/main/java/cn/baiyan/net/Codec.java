package cn.baiyan.net;

import cn.baiyan.net.codec.*;

import cn.baiyan.net.codec.ByteCodec;
import org.json.JSONObject;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public abstract class Codec {
    private static Map<Class<?>, Codec> class2Serializers = new ConcurrentHashMap<>();

    static {
        register(Boolean.TYPE, new BooleanCodec());
        register(Boolean.class, new BooleanCodec());
        register(Byte.TYPE, new ByteCodec());
        register(Byte.class, new ByteCodec());
        register(Short.TYPE, new ShortCodec());
        register(Short.class, new ShortCodec());
        register(Integer.TYPE, new IntCodec());
        register(Integer.class, new IntCodec());
        register(Float.TYPE, new FloatCodec());
        register(Float.class, new FloatCodec());
        register(Double.TYPE, new DoubleCodec());
        register(Double.class, new DoubleCodec());
        register(Long.TYPE, new LongCodec());
        register(Long.class, new LongCodec());
        register(String.class, new StringCodec());
        register(List.class, new CollectionCodec());
        register(Set.class, new CollectionCodec());
        register(Object[].class, new ArrayCodec());

    }

    public static void register(Class<?> clazz, Codec codec) {
        class2Serializers.put(clazz, codec);
    }

    public static Codec getSerializer(Class<?> clazz) {
        if (class2Serializers.containsKey(clazz)) {
            return class2Serializers.get(clazz);
        }
        if (clazz.isArray()) {
            return class2Serializers.get(Object[].class);
        }
        Field[] fields = clazz.getDeclaredFields();
        LinkedHashMap<Field, Class<?>> sortedFields = new LinkedHashMap<>();
        List<FieldCodecMeta> fieldMeta = new ArrayList<>();
        for (Field field : fields) {
            int modifier = field.getModifiers();
            if (Modifier.isFinal(modifier) || Modifier.isStrict(modifier) || Modifier.isTransient(modifier)) {
                continue;
            }
            field.setAccessible(true);
            sortedFields.put(field, field.getType());
            Class<?> type = field.getType();
            Codec codec = Codec.getSerializer(type);
            fieldMeta.add(FieldCodecMeta.valueOf(field, codec));
        }
        Codec messageCodec = MessageCodec.valueOf(fieldMeta);
        Codec.register(clazz, messageCodec);
        return messageCodec;
    }

    /**
     * 消息编码
     */
    public abstract Object decode(JSONObject in, Class<?> type, String name);

    /**
     * 消息解码
     *
     * @param out
     * @param value
     * @param wrapper
     * @return
     */
    public abstract void encode(ByteBuffer out, Object value, Class<?> wrapper);
}
