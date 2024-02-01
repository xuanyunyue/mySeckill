package github.xuanyunyue.controller;

import github.xuanyunyue.domain.SeckillProduct;
import github.xuanyunyue.domain.SeckillSession;
import github.xuanyunyue.service.SeckillProductService;
import github.xuanyunyue.service.SeckillSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author： zyx1128
 * @create： 2024/1/16 11:13
 * @description：秒杀运营的接口
 */
@RestController
@RequestMapping("/seckill/operation")
public class OperationController {
    // 为了方便测试，全部使用get方式


    @Autowired
    private SeckillSessionService seckillSessionService;
    @Autowired
    private SeckillProductService seckillProductService;

    /**
     - @description: 增加秒杀活动场次
     * @param session
    - @return java.lang.String
     */
    @GetMapping("/activity/add")
    public String addSeckillSession(SeckillSession session){
        seckillSessionService.addSeckillSession(session);
        return "success";
    }

    @PostMapping("/product/add")
    public String addSeckillProduct(@RequestBody SeckillProduct product){
        boolean b = seckillProductService.addSeckillProduct(product);
        if (b){
            return "success";
        }else return "failure";
    }

}
