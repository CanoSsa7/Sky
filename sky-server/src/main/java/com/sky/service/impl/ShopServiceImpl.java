package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * @author CanoSsa7
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Qualifier("redisTemplate")
    @Autowired
    RedisTemplate redis;
    @Override
    public void setStatus(Integer status) {

    }
}
