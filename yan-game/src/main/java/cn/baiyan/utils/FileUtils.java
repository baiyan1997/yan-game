package cn.baiyan.utils;

import java.io.*;
import java.util.function.Predicate;

public class FileUtils {

    /**
     * 读取文件并返回utf8编码
     */
    public static String readLines(String fileName) throws IOException{
        File file = new File(fileName);
        if(!file.exists()){
            throw new FileNotFoundException();
        }
        FileInputStream in = new FileInputStream(file);
        StringBuffer result = new StringBuffer("");
        // 读取指定文件时以UTF-8的格式读取
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(in, "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line).append("\n");
            }
        }
        return result.toString();
    }

    public static String readLines(String fileName, Predicate<String> predicate) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        FileInputStream in = new FileInputStream(file);
        StringBuffer result = new StringBuffer("");
        // 指定读取文件时以UTF-8的格式读取
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(in, "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (predicate.test(line)) {
                    result.append(line).append("\n");
                }
            }
        }
        return result.toString();
    }

    public static String readLines(InputStream inputStream) throws IOException {
        StringBuffer result = new StringBuffer("");
        // 指定读取文件时以UTF-8的格式读取
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(inputStream, "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line).append("\n");
            }
        }
        return result.toString();
    }
}
