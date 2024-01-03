package cn.baiyan.net.codec;

import cn.baiyan.net.Codec;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

public class FieldCodecMeta {
    private Field field;

    private Class<?> type;

    private Codec codec;
    /**
     * collection类型里的元素
     */
    private Class<?> wrapper;

    public static FieldCodecMeta valueOf(Field field, Codec codec) {
        FieldCodecMeta meta = new FieldCodecMeta();
        meta.field = field;
        Class<?> type = field.getType();
        meta.type = type;
        meta.codec = codec;
        // 判断是个类是不是当前的接口父类等
        if (Collection.class.isAssignableFrom(type)) {
            // 获取集合中的class
            meta.wrapper = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        } else if (type.isArray()) {
            meta.wrapper = type.getComponentType();
        }
        return meta;
    }

    public Field getField() {
        return field;
    }

    public Class<?> getType() {
        return type;
    }

    public Codec getCodec() {
        return codec;
    }

    public Class<?> getWrapper() {
        return wrapper;
    }

    @Override
    public String toString() {
        return "FieldCodecMeta [field=" + field + ", type=" + type + ", serializer=" + codec + ", wrapper="
                + wrapper + "]";
    }

}
