package cn.baiyan.net.codec;

import cn.baiyan.net.Codec;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.*;

public class CollectionCodec extends Codec {

    @Override
    public Object decode(JSONObject in, Class<?> type, String name) {
        JSONArray array = in.getJSONArray(name);
        Collection<Object> result = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            int anInt = array.getInt(i);
            result.add(anInt);
        }
        return result;
    }

    @Override
    public void encode(ByteBuffer out, Object value, Class<?> wrapper) {
    }
}
