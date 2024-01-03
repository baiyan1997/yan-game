package cn.baiyan.hotswap;

import cn.baiyan.db.utils.DbUtils;
import com.sun.tools.attach.VirtualMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaDoctor {

    private static final JavaDoctor self = new JavaDoctor();

    private Map<String, ClassFileMeta> reloadFiles = new HashMap<>();

    public static byte[] fixData;

    public static String log;

    public static Exception exception;

    private Logger logger = LoggerFactory.getLogger(JavaDoctor.class);

    public static JavaDoctor getInstance() {
        return self;
    }

    public synchronized boolean hotSwap(String filePath) throws Exception {
        String s = filePath.replace(".","/" );
        String path = JavaDoctor.class.getClassLoader().getResource(s + ".class").getPath();
        System.out.println(path);
        ByteArrayOutputStream bou = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(bou));
        DynamicClassLoader classLoader = new DynamicClassLoader();
        Map<String, ClassFileMeta> currFiles = new HashMap<>();
        File file = new File(path);
        ClassFileMeta fileMeta = new ClassFileMeta(file);
        reloadFiles.put(fileMeta.className, fileMeta);
        currFiles.put(fileMeta.className, fileMeta);
        classLoader.loadClass(fileMeta.className);
        dos.writeInt(currFiles.entrySet().size());
        for (Map.Entry<String, ClassFileMeta> entry : currFiles.entrySet()) {
            String fileName = entry.getKey();
            ClassFileMeta meta = entry.getValue();
            dos.writeUTF(fileName);
            dos.writeInt(meta.data.length);
            dos.write(meta.data);
        }
        dos.flush();

        fixData = bou.toByteArray();

        reloadClass(filePath, currFiles);

        return true;
    }

    private String reloadClass(String path, Map<String, ClassFileMeta> reloadFiles) {
        try {
            // 拿到当前jvm的进程id
            String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
            VirtualMachine vm = VirtualMachine.attach(pid);
            log = "empty";
            exception = null;
            logger.error("热更目录[{}]，总共有{}个文件", path, reloadFiles.size());
            // path参数即agentmain()方法的第一个参数
//            vm.loadAgent("E:\\git\\JavaCoreCode\\Core\\libs\\hotswap-1.0.0.jar");
            URL resource = JavaDoctor.class.getClassLoader().getResource("hotswap-1.0.0.jar");
            File file = new File(resource.getPath());
            vm.loadAgent(file.getAbsolutePath());
            logger.error("热更完成--> {}", log);
            if (exception != null) {
                logger.error("热更异常{}", exception);
            }
            return log;
        } catch (Throwable e) {
            logger.error("", e);
            return "热更失败";
        }
    }
}
