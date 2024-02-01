package github.xuanyunyue.dao;

import github.xuanyunyue.domain.SeckillProduct;
import github.xuanyunyue.domain.SeckillSession;
import org.springframework.stereotype.Repository;

/**
 * @author： zyx1128
 * @create： 2024/1/16 11:44
 * @description：TODO
 */
public interface SeckillSessionDAO {
    void addSeckillSession(SeckillSession session);
}
