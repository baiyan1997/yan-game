package cn.baiyan.db;

import cn.baiyan.db.utils.StringUtils;
import cn.baiyan.net.Packet;
import cn.baiyan.utils.ClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum OrmProcessor {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(OrmProcessor.class);
    /**
     * entity与对应bridge的映射关系
     */
    private Map<Class<?>, OrmBridge> classOrmMapper = new HashMap<>();


    /**
     * scanPath to load rom entities
     */
    public void initOrmBridges(String scanPath) {
        Set<Class<?>> entityClasses = listEntityClasses(scanPath);
        for (Class<?> clazz : entityClasses) {
            OrmBridge bridge = createBridge(clazz);
            this.classOrmMapper.put(clazz, bridge);
        }
    }

    private OrmBridge createBridge(Class<?> clazz) {
        OrmBridge bridge = new OrmBridge();
        Entity entity = clazz.getAnnotation(Entity.class);
        // 没有配置tablename 则用entity名首字母小写
        if (entity.name().length() <= 0) {
            bridge.setTableName(StringUtils.firstLetterToLowerCase(clazz.getSimpleName()));
        } else {
            bridge.setTableName(entity.name());
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            String fieldName = field.getName();
            try {
                if (column == null) {
                    continue;
                }
                FieldMetadata metadata = FieldMetadata.valueOf(field);
                bridge.addFieldMetadata(fieldName, metadata);
                if (field.getAnnotation(Id.class) != null) {
                    bridge.addUniqueKey(fieldName);
                }
                if (!StringUtils.isEmpty(column.name())) {
                    bridge.addPropertyColumnOverride(fieldName, column.name());
                }
            } catch (Exception e) {
                // TODO 抛除异常需要修改
                logger.info("抛除异常");
            }
        }
        return bridge;
    }

    private Set<Class<?>> listEntityClasses(String scanPath) {
        return ClassScanner.listClassesWithAnnotation(scanPath, Entity.class);
    }

    public OrmBridge getOrmBridge(Class<?> clazz){
        return this.classOrmMapper.get(clazz);
    }

}
