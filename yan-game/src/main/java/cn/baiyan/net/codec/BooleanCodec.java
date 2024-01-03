package cn.baiyan.net.codec;

import cn.baiyan.net.Codec;
import org.json.JSONObject;

import java.nio.ByteBuffer;

public class BooleanCodec extends Codec {
    @Override
    public Boolean decode(JSONObject json, Class<?> type, String name) {
        return json.getBoolean(name);
    }

    @Override
    public void encode(ByteBuffer out, Object value, Class<?> wrapper) {
    }
}
