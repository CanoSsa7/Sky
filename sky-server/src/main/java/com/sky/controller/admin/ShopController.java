package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@Slf4j
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
public class ShopController {
    @Autowired
    RedisTemplate redisTemplate;

    final private static String STATUS = "SHOP_STATUS";
    @PutMapping("/{status}")
    Result setStatus(@PathVariable Integer status){
        if(status != StatusConstant.DISABLE || status != StatusConstant.ENABLE){
            return Result.error("非法状态！");
        }
        log.info("设置店铺应用状态为{}", status == 1?"营业中":"打烊中");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(STATUS, status);
        return Result.success();
    }

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
