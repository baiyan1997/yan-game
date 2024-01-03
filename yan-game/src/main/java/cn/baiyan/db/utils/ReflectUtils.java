package cn.baiyan.db.utils;

import cn.baiyan.db.utils.StringUtils;

import java.lang.reflect.Method;

public class ReflectUtils {

    public static Object getMethodValue(Object obj, String property) throws  Exception{
        String methodName = "get" + StringUtils.firstLetterToUpperCase(property);
        Method method = obj.getClass().getMethod(methodName);
        return method.invoke(obj);
    }
}
