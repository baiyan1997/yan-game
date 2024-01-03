package cn.baiyan.hotswap;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;

public class InstrumentHotswapTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        while(true){
            Thread.sleep(1000);
            System.out.println(new Person().toString());
            File file = new File(InstrumentHotswapTest.class.getClassLoader().getResource("autoupdate.txt").getPath());
            String s = FileUtils.readFileToString(file, "UTF-8");
            String[] classPath = s.split("\n");
            for (String path : classPath) {
                HotSwapManager.INSTANCE.reloadClass(path);
            }
            Student student = new Student();
            System.out.println(student.toString());
        }

    }
}
