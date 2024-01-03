package cn.baiyan.net.codec;

import cn.baiyan.net.ByteBuffUtil;
import cn.baiyan.net.Codec;
import org.json.JSONObject;

import java.nio.ByteBuffer;

public class IntCodec extends Codec {
//    @Override
//    public Integer decode(ByteBuffer in, Class<?> type, Class<?> wrapper) {
//        return ByteBuffUtil.readInt(in);
//    }

    @Override
    public Object decode(JSONObject in, Class<?> type, String name) {
        return in.getInt(name);
    }

    @Override
    public void encode(ByteBuffer out, Object value, Class<?> wrapper) {
        ByteBuffUtil.writeInt(out, (int) value);
    }
}
