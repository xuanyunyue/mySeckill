package github.xuanyunyue.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author： zyx1128
 * @create： 2024/1/16 12:14
 * @description：TODO
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SeckillProduct {
    private Long id;
    private Long sessionId;
    private Long productId;
    private BigDecimal seckillPrice;
    private Long seckillStock;
}
