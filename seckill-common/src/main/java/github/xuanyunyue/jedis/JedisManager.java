package github.xuanyunyue.jedis;


import redis.clients.jedis.Jedis;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author： zyx1128
 * @create： 2024/1/17 10:04
 * @description：封装Jedis的单例类
 */
public class JedisManager {
    private static ConcurrentHashMap<String, Jedis> jedisMap = new ConcurrentHashMap<>();

    // 外懒内汉式单例
    private static class Inner {
        private static final JedisManager INSTANCE = new JedisManager();
    }

    public static JedisManager getInstance() {
        return Inner.INSTANCE;
    }

    // 获取jedis实例
    public Jedis getJedis(String host, Integer port) {
        String key = host + ":" + port;
        if (!jedisMap.containsKey(key)) {
            synchronized (this) {
                if (!jedisMap.containsKey(key)) {
                    jedisMap.put(key, new Jedis(host, port));
                }
            }
        }
        return jedisMap.get(key);
    }

    /**
     - @description: 获取默认端口(6379)的reids实例
     * @param
    - @return redis.clients.jedis.Jedis
     */
    public Jedis getJedis() {
        return getJedis("127.0.0.1", 6379);
    }

}
