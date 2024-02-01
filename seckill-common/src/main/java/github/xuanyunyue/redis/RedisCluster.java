package github.xuanyunyue.redis;

import lombok.SneakyThrows;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * @author： zyx1128
 * @create： 2024/1/18 16:25
 * @description: 用于对大量数据分片到每台redis的工具类
 */
public class RedisCluster {
    // redis集群对应的java客户端
    private List<Jedis> cluster=new ArrayList<>();
    private RedisCluster(){
        // 初始化redis集群
        cluster.add(new Jedis("localhost",6380));
        cluster.add(new Jedis("localhost",6381));
        cluster.add(new Jedis("localhost",6382));
    }
    private static class Inner{
        private static final RedisCluster INSTANCE=new RedisCluster();
    }

    public static RedisCluster getInstance(){
        return Inner.INSTANCE;
    }

    @SneakyThrows
    public void initSeckillProductStock(long productId,long seckillStock){
        int sizeCluster = cluster.size();
        long perRedisNum = seckillStock / sizeCluster;
        long remainNum= seckillStock - perRedisNum * sizeCluster;
        long lastRedisNum = perRedisNum + remainNum;
        for (int i=0;i<sizeCluster;i++){
            Jedis jedis = cluster.get(i);
            JSONObject jsonObject = new JSONObject();
            // 正在秒杀的商品数量
            long salingNum=(i==sizeCluster-1?lastRedisNum:perRedisNum);
            jsonObject.put("saling_stock",salingNum);
            // 秒杀成功未被支付锁定的商品数量
            jsonObject.put("locked_stock",0);
            // 秒杀成功且被支付的商品数量
            jsonObject.put("saled_stock",0);
            String key="seckill:product:"+productId+":stock";
            jedis.set(key,jsonObject.toString());
            System.out.println("成功放入第"+i+1+"个数据库，个数为："+salingNum);
        }
    }
}
