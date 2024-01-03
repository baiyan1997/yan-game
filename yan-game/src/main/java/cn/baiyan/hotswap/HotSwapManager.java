package cn.baiyan.hotswap;

import cn.baiyan.utils.FileUtils;
import groovy.lang.GroovyClassLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

/**
 * 以javaAgent的方式热更新文件 只能修改java文件的方法体
 */
public enum HotSwapManager {
    /**
     * 枚举单例
     */
    INSTANCE;

    /**
     * 热更新拓展参数
     */
    private Map<String, Object> extendParams;

    private Logger logger = LoggerFactory.getLogger(HotSwapManager.class);

    /**
     * 加载java源文件并且创建一个新的实例
     */
    public String loadJavaFile(String classFullName) {
        // 类名
        String simpleName = classFullName.substring(classFullName.lastIndexOf(".") + 1);
        try {
            String filePath = getFilePath("groovy" + File.separator + simpleName + ".java");
            String clazzFile = FileUtils.readLines(filePath);
            Class<?> clazz = new GroovyClassLoader().parseClass(clazzFile, classFullName);
            clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return "load class failed ," + e.getMessage();
        }
        return "load class success";
    }

    /**
     * use jdk instrument to hotswap a lodded class you can only modify a class's
     */
    public String reloadClass(String path) {
        try {
            if (JavaDoctor.getInstance().hotSwap(path)) {
                return "热更成功";
            } else {
                return "热更失败";
            }
        } catch (Exception e) {
            logger.error("", e);
            return e.getMessage();
        }
    }

    private String getFilePath(String dir) {
        String defaultDir = System.getProperty("scriptDir");
        String fullPath = dir;
        if (StringUtils.isNoneBlank(defaultDir)) {
            fullPath = dir + File.separator + dir;
        }
        return fullPath;
    }

}
