import com.alibaba.fastjson.JSONObject;
import github.xuanyunyue.jedis.JedisManager;
import github.xuanyunyue.domain.SeckillSession;
import lombok.SneakyThrows;
import redis.clients.jedis.Jedis;

/**
 * @author： zyx1128
 * @create： 2024/1/17 10:53
 * @description：TODO
 */
public class SeckillSessionTest {
    @SneakyThrows
    public static void main(String[] args) {
        SeckillSession session = new SeckillSession(2L, "2024-01-17", "10:55:26");
        Jedis jedis = JedisManager.getInstance().getJedis();
        jedis.lpush("seckill:session:"+session.getSessionDate(), JSONObject.toJSONString(session));
    }
}
