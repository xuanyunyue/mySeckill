package github.xuanyunyue.service.Impl;

import com.alibaba.fastjson.JSONObject;
import github.xuanyunyue.jedis.JedisManager;
import github.xuanyunyue.dao.SeckillSessionDAO;
import github.xuanyunyue.domain.SeckillSession;
import github.xuanyunyue.service.SeckillSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * @author： zyx1128
 * @create： 2024/1/16 12:04
 * @description：
 */
@Service
public class SeckillSessionServiceImpl implements SeckillSessionService {

    @Autowired
    SeckillSessionDAO sessionDAO;

    @Override
    public void addSeckillSession(SeckillSession session) {
        sessionDAO.addSeckillSession(session);
        Jedis jedis = JedisManager.getInstance().getJedis();
        jedis.lpush("seckill:session:"+session.getSessionDate(), JSONObject.toJSONString(session));
    }
}
