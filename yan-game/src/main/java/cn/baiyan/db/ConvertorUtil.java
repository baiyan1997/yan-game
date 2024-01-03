package cn.baiyan.db;

import cn.baiyan.db.converter.AttributeConverter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConvertorUtil {

    private static Map<Class<?>, AttributeConverter> converters = new ConcurrentHashMap<>();

    public static AttributeConverter getAttributeConverter(Class<?> clazz) {
        return converters.computeIfAbsent(clazz, c -> {
            try {
                return (AttributeConverter) clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
