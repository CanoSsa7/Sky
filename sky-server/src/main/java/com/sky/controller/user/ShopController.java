package com.sky.controller.user;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

/**
 * @author CanoSsa7
 */
@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
public class ShopController {
    @Autowired
    RedisTemplate redisTemplate;

    final private static String STATUS = "SHOP_STATUS";

    @GetMapping("/status")
    Result<Integer> getStatus(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Integer status = (Integer) valueOperations.get(STATUS);
        if(status != null) {
            log.info("获取店铺应用状态为{}", status == 1?"营业中":"打烊中");
        }
        return Result.success(status);
    }
}
