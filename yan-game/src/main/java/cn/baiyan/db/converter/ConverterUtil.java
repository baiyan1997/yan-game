package cn.baiyan.db.converter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConverterUtil {

    private static Map<Class<?>, AttributeConverter> converters = new ConcurrentHashMap<>();

    public static AttributeConverter getAttributeConverter(Class<?> clazz) {
        return converters.computeIfAbsent(clazz, c->{
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
