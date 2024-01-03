package cn.baiyan.net.codec;

import cn.baiyan.net.ByteBuffUtil;
import cn.baiyan.net.Codec;
import org.json.JSONObject;

import java.nio.ByteBuffer;

public class LongCodec extends Codec {
//    @Override
//    public Long decode(ByteBuffer in, Class<?> type, Class<?> wrapper) {
//        return ByteBuffUtil.readLong(in);
//    }

    @Override
    public Object decode(JSONObject in,Class<?> type, String name) {
        return in.getLong(name);
    }

    @Override
    public void encode(ByteBuffer out, Object value, Class<?> wrapper) {
        ByteBuffUtil.writeLong(out, (long) value);
    }
}
