package cn.baiyan.db.converter;

public interface AttributeConverter<X, Y> {

    Y convertToDatabaseColumn(X attribute);

    X convertToEntityAttribute(Class<X> clazz, Y dbData);
}
