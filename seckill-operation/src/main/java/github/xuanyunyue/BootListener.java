package github.xuanyunyue;

import github.xuanyunyue.rocketMq.RocketMQProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

/**
 * @author： zyx1128
 * @create： 2024/1/17 16:56
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
    @Override
    public void run(String... strings) throws Exception {
        RocketMQProducer.setProducerGroup("seckill-operation-producer-group");
        DefaultMQProducer producer = RocketMQProducer.getInstance().getProducer();
        Message message = new Message(
                "testTopic",
                null,
                ("Hello RocketMQ").getBytes(StandardCharsets.UTF_8)
        );
        SendResult sendResult = producer.send(message);
        log.debug("%s%n", sendResult);
        log.info("系统启动时发送测试消息到RocketMQ的testTopic成功......");
    }
}
