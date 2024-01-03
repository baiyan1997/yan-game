package cn.baiyan.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static TypeFactory typeFactory = TypeFactory.defaultInstance();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String object2String(Object object) {
        StringWriter writer = new StringWriter();
        try{
           MAPPER.writeValue(writer, object);
        }catch (Exception e){
           return  null;
        }
        return writer.toString();
    }


    @SuppressWarnings("unchecked")
    public static <T> T string2Object(String json, Class<T> clazz) {
        JavaType type = typeFactory.constructType(clazz);
        try {
            return (T) MAPPER.readValue(json, type);
        } catch(Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public static <K, V> Map<K, V> string2Map(String json, Class<K> keyClazz, Class<V> valueClazz) {
        JavaType type = typeFactory.constructMapType(HashMap.class, keyClazz, valueClazz);
        try {
            return MAPPER.readValue(json, type);
        } catch(Exception e) {
            return null;
        }
    }


}
