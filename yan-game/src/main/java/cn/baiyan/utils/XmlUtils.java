package cn.baiyan.utils;

import org.apache.commons.io.FileUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

public class XmlUtils {
    private static Logger logger = LoggerFactory.getLogger(XmlUtils.class.getSimpleName());

    private static final String ROOT_CONFIG_PATH = File.separator;

    public static <T> T loadXmlConfig(String filePath, Class<T> configClass) {
        T ob = null;
        InputStream inputStream = XmlUtils.class.getClassLoader().getResourceAsStream("server.xml");
        File file = null;
        try {
            file = File.createTempFile("tempfile", ".tmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileUtils.copyInputStreamToFile(inputStream, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!file.exists()) {
            return ob;
        }
        String absolutePath = file.getAbsolutePath();
        System.out.println(absolutePath);
        Persister serializer = new Persister();
        try {
            ob = serializer.read(configClass, file);
        } catch (Exception e) {
//            logger.error("文件" + fileName + "配置有误", e);
        }
        return ob;
    }
}
