package github.xuanyunyue.dao.Impl;

import github.xuanyunyue.domain.SeckillProduct;
import github.xuanyunyue.domain.SeckillSession;
import github.xuanyunyue.mapper.OperationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author： zyx1128
 * @create： 2024/1/16 11:46
 * @description：TODO
 */
@Repository
public class SeckillSessionDAOImpl implements github.xuanyunyue.dao.SeckillSessionDAO {
    @Autowired
    OperationMapper mapper;

    @Override
    public void addSeckillSession(SeckillSession session) {
        mapper.addSeckillSession(session);
    }
}
