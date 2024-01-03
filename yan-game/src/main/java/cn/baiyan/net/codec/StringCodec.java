package cn.baiyan.net.codec;

import cn.baiyan.net.Codec;
import cn.baiyan.net.ByteBuffUtil;
import org.json.JSONObject;

import java.nio.ByteBuffer;

public class StringCodec extends Codec {

    //    @Override
//    public String decode(ByteBuffer in, Class<?> type, Class<?> wrapper) {
//        return ByteBuffUtil.readUtf8(in);
//    }
//
    @Override
    public Object decode(JSONObject in, Class<?> type, String name) {
        return in.getString(name);
    }

    @Override
    public void encode(ByteBuffer out, Object value, Class<?> wrapper) {
        ByteBuffUtil.writeUtf8(out, (String) value);
    }
}
