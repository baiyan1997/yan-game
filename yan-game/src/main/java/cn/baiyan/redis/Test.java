package cn.baiyan.redis;

import org.redisson.Redisson;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Date;


public class Test {
    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://8.138.84.182:6379")
                .setPassword("baiyan123")
                .setDatabase(0);
        //获取客户端
        RedissonClient redissonClient = Redisson.create(config);
        //获取所有的key
        redissonClient.getKeys().getKeys().forEach(System.out::println);
        RSet<Student> studentSet = redissonClient.getSet("bbb");
//        for (int i = 0; i < 3; i++) {
//            Student student = new Student();
//            student.setAge(i);
//            student.setId(1L);
//            student.setName("baiyan");
//            student.setDate(new Date());
//            studentSet.add(student);
//        }
        System.out.println(studentSet.toString());
//        //关闭客户端
        redissonClient.shutdown();

    }



}
