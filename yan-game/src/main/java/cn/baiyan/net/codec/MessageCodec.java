package cn.baiyan.net.codec;

import cn.baiyan.net.Codec;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * 消息或者vo的解析器
 */
public class MessageCodec extends Codec {

    private List<FieldCodecMeta> fieldsMeta;

    public static MessageCodec valueOf(List<FieldCodecMeta> fieldsMeta) {
        MessageCodec serializer = new MessageCodec();
        serializer.fieldsMeta = fieldsMeta;
        return serializer;
    }


    @Override
    public Object decode(JSONObject json, Class<?> type, String name) {
        try {
            Object bean = type.newInstance();
            for (FieldCodecMeta fieldMeta : fieldsMeta) {
                Field field = fieldMeta.getField();
                Codec fieldCodec = fieldMeta.getCodec();
                Object value = fieldCodec.decode(json, type, field.getName());
                field.set(bean, value);
            }
            return bean;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void encode(ByteBuffer out, Object message, Class<?> wrapper) {
        try {
            for (FieldCodecMeta fieldMeta : fieldsMeta) {
                Field field = fieldMeta.getField();
                Codec fileCodec = Codec.getSerializer(fieldMeta.getType());
                Object value = field.get(message);
                fileCodec.encode(out, value, fieldMeta.getWrapper());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
