package github.xuanyunyue.rocketMq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

/**
 * 我们自己封装的RocketMQ生产者单例类
 */
@Slf4j
public class RocketMQProducer {
    private DefaultMQProducer producer;
    private static String producerGroup;

    private RocketMQProducer(String producerGroup) {
        try {
            this.producer = new DefaultMQProducer(producerGroup);
            this.producer.setNamesrvAddr("localhost:9876");
            this.producer.setSendMsgTimeout(60 * 1000);
            this.producer.start();
        } catch(MQClientException e) {
            log.error("初始化RocketMQ生产者失败：" + e);
        }
    }

    private static class Inner {
        static RocketMQProducer INSTANCE = new RocketMQProducer(producerGroup);
    }

    /**
     * 设置生产者分组名称
     * @param producerGroup
     */
    public static void setProducerGroup(String producerGroup) {
        RocketMQProducer.producerGroup = producerGroup;
    }

    /**
     * 获取单例
     * @return
     */
    public static RocketMQProducer getInstance() {
        return Inner.INSTANCE;
    }

    /**
     * 获取MQ生产者
     * @return
     */
    public DefaultMQProducer getProducer() {
        return producer;
    }

}
