package github.xuanyunyue;

import com.alibaba.fastjson.JSONObject;
import github.xuanyunyue.freemarker.FreemarkerHelper;
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
 * @create： 2024/1/18 11:56
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
                    log.info("页面渲染系统收到mq消息："+msg);
                    JSONObject productJsonObject = JSONObject.parseObject(msg);

                    // 反序列化
                    // 这里应调用商品中心的接口，获取商品的详情信息
                    Long productId = productJsonObject.getLong("productId");
                    BigDecimal seckillPrice = productJsonObject.getBigDecimal("seckillPrice");
                    Long seckillStock = productJsonObject.getLong("seckillStock");

                    FreemarkerHelper freemarkerHelper = new FreemarkerHelper();
                    Map<String, Object> map = new HashMap<>();
                    map.put("productId",productId);
                    map.put("seckillPrice",seckillPrice);
                    map.put("seckillStock",seckillStock);
                    String html = freemarkerHelper.parseTemplate("autolist.ftl", map);

                    // 将渲染完毕的秒杀商品html写入磁盘文件
                    log.info("将渲染完毕的秒杀商品html写入磁盘文件...");
                    System.out.println(html);
                    // 将磁盘的html用scp命令传送到nginx
                    log.info("将磁盘的html用scp命令传送到nginx...");

                    // 调用云厂商的CDN的API，让CDN刷新静态页面的缓存
                    log.info("调用云厂商的CDN的API，让CDN刷新静态页面的缓存...");

                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        log.info("页面渲染消费者启动...");
    }
}
