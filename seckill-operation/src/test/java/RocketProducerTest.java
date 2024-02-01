import lombok.SneakyThrows;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
 * @author： zyx1128
 * @create： 2024/1/17 11:29
 * @description：TODO
 */
public class RocketProducerTest {
    @SneakyThrows
    public static void main(String[] args) {
        DefaultMQProducer testGroup = new DefaultMQProducer("test_group");
        testGroup.setNamesrvAddr("localhost:9876");
        testGroup.start();
        String msg="hello";
        Message message = new Message("topicTest", msg.getBytes(StandardCharsets.UTF_8));
        SendResult sendResult = testGroup.send(message);
        System.out.printf("%s%n",sendResult);
    }
}
