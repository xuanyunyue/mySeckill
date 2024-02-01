package github.xuanyunyue.service.Impl;

import com.alibaba.fastjson.JSONObject;
import github.xuanyunyue.jedis.JedisManager;
import github.xuanyunyue.dao.SeckillProductDAO;
import github.xuanyunyue.domain.SeckillProduct;
import github.xuanyunyue.rocketMq.RocketMQProducer;
import github.xuanyunyue.service.SeckillProductService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.nio.charset.StandardCharsets;

/**
 * @author： zyx1128
 * @create： 2024/1/16 12:48
 * @description：TODO
 */
@Service
@Slf4j
public class SeckillProductServiceImpl implements SeckillProductService {
    String TOPIC="seckill_procduct_added_topic";

    @Autowired
    SeckillProductDAO productDAO;

    @Override
    public boolean addSeckillProduct(SeckillProduct product) {
        productDAO.addSeckillProduct(product);
        // 存入redis缓存
        Jedis jedis = JedisManager.getInstance().getJedis();
        jedis.lpush("seckill:product:" + product.getSessionId(), JSONObject.toJSONString(product));
        // 通过MQ发送消息
        try {
            Message message = new Message(TOPIC, null, JSONObject.toJSONString(product).getBytes(StandardCharsets.UTF_8));
            DefaultMQProducer producer = RocketMQProducer.getInstance().getProducer();
            SendResult sendResult = producer.send(message);
        } catch (Exception e) {
            log.error("增加秒杀商品时，发送消息到MQ失败：" + e);
            return false;
        }
        return true;
    }
}
