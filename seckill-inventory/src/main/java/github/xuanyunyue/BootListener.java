package github.xuanyunyue;

import com.alibaba.fastjson.JSONObject;
import github.xuanyunyue.redis.RedisCluster;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author： zyx1128
 * @create： 2024/1/18 15:59
 * @description：
 * CommandLineRunner和ApplicationRunner接口用于在SpringBoot启动完成之后立即做的业务处理。<br>
 * 首先执行所有的CommandLineRunner，然后执行所有的ApplicationRunner.<br><br>
 * 1. 如果我们需要在Spring容器启动后执行一些简单的任务，而且不需要获取任何的应用程序参数，
 * 那么我们可以使用CommandLineRunner，它的用法比较简单，只需要实现一个接口，然后写好run方法即可。<br><br>
 * 2. 如果我们需要在Spring容器启动后执行一些复杂的任务，而且需要获取一些应用程序参数，比如Spring的配置参数，那么我们可以使用ApplicationRunner，
 * 它的用法比较灵活，可以通过ApplicationArguments对象来获取各种参数，然后根据参数来执行不同的逻辑。
 */
@Slf4j
@Component
public class BootListener implements CommandLineRunner {
    String CONSUMER_GROUP="seckill-page-consumer-group";
    String TOPIC="seckill_procduct_added_topic";
    String MQ_URL="localhost:9876";


    @Override
    public void run(String... strings) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(CONSUMER_GROUP);
        consumer.setNamesrvAddr(MQ_URL);
        consumer.subscribe(TOPIC,"*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
                for (MessageExt messageExt:list){
                    String msg = new String(messageExt.getBody());
                    log.info("库存系统收到mq消息："+msg);
                    JSONObject productJsonObject = JSONObject.parseObject(msg);

                    // 冻结数据库里的商品用于秒杀
                    // 库存中心分为：可售、锁定、已售、冻结
                    Long productId = productJsonObject.getLong("productId");
                    BigDecimal seckillPrice = productJsonObject.getBigDecimal("seckillPrice");
                    Long seckillStock = productJsonObject.getLong("seckillStock");
                    log.info("调用库存中心的接口，冻结用于秒杀活动的商品库存");

                    // 分片商品，放到各个reids上
                    RedisCluster.getInstance().initSeckillProductStock(productId,seckillStock);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        log.info("库存消费者启动...");
    }
}
