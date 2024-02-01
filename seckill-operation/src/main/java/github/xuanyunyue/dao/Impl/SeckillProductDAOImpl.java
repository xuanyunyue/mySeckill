package github.xuanyunyue.dao.Impl;

import github.xuanyunyue.dao.SeckillProductDAO;
import github.xuanyunyue.domain.SeckillProduct;
import github.xuanyunyue.mapper.OperationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author： zyx1128
 * @create： 2024/1/16 12:50
 * @description：TODO
 */
@Repository
public class SeckillProductDAOImpl implements SeckillProductDAO {

    @Autowired
    OperationMapper mapper;
    @Override
    public void addSeckillProduct(SeckillProduct product) {
        mapper.addSeckillProduct(product);
    }
}
