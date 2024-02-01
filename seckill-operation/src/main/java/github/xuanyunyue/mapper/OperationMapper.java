package github.xuanyunyue.mapper;

import github.xuanyunyue.domain.SeckillProduct;
import github.xuanyunyue.domain.SeckillSession;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 * @author： zyx1128
 * @create： 2024/1/16 11:31
 * @description：TODO
 */
@Mapper
public interface OperationMapper {
    @Insert("INSERT INTO seckill_session (session_date, session_time) VALUES " +
            "(#{sessionDate},#{sessionTime})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "idi")
    void addSeckillSession(SeckillSession session);

    @Insert("INSERT INTO seckill_product(session_id, product_id, seckill_price, seckill_stock) VALUES " +
            "(#{sessionId},#{productId},#{seckillPrice},#{seckillStock})")
    void addSeckillProduct(SeckillProduct product);
}
