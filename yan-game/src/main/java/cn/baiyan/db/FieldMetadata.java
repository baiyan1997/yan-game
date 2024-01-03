package cn.baiyan.db;

import cn.baiyan.db.converter.AttributeConverter;
import cn.baiyan.db.converter.Convert;
import cn.baiyan.db.converter.ConverterUtil;

import java.lang.reflect.Field;

public class FieldMetadata {

    private Field field;

    private AttributeConverter converter;

    public static FieldMetadata valueOf(Field field) {
        field.setAccessible(true);
        FieldMetadata metadata = new FieldMetadata();
        metadata.field = field;
        Convert annotation = field.getAnnotation(Convert.class);
        if (annotation != null) {
            AttributeConverter convert = ConverterUtil.getAttributeConverter(annotation.converter());
            metadata.converter = convert;
        }
        return metadata;
    }

    public Field getField() {
        return field;
    }

    public AttributeConverter getConverter() {
        return converter;
    }

}
