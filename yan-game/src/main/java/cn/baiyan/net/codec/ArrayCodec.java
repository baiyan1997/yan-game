package cn.baiyan.net.codec;

import cn.baiyan.net.Codec;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;

public class ArrayCodec extends Codec {
//    public Object decode(ByteBuffer in, Class<?> type, Class<?> wrapper) {
//        return null;
//    }

    @Override
    public Object decode(JSONObject in, Class<?> type, String name) {
        String string = in.getString(name);
        return string.split(",");
    }

    @Override
    public void encode(ByteBuffer out, Object value, Class<?> wrapper) {
    }
}
