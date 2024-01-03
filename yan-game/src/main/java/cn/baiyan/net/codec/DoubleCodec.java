package cn.baiyan.net.codec;

import cn.baiyan.net.Codec;
import org.json.JSONObject;

import java.nio.ByteBuffer;

public class DoubleCodec extends Codec {
    @Override
    public Object decode(JSONObject in, Class<?> type, String name) {
        return null;
    }

    @Override
    public void encode(ByteBuffer out, Object value, Class<?> wrapper) {
    }
}
