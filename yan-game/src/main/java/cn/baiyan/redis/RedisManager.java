package cn.baiyan.redis;

import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisManager {

    public static RedissonClient redissonClient;

    public static void init() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://8.138.84.182:6379")
                .setPassword("baiyan123")
                .setDatabase(0);
        //获取客户端
        redissonClient = Redisson.create(config);
    }

    public static <V> RSet<V> getSet(String key) {
        return redissonClient.getSet(key);
    }

    public static <V> RList<V> getList(String key) {
        return redissonClient.getList(key);
    }


}
