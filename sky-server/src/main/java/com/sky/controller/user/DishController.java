package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        log.info("小程序端查询分类{}下菜品", categoryId);
        // 查询redis
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // redis中的key
        String key = "dish_" + categoryId;
        // 放入的java类型为List<DishVO>，取出也为List<DishVO>
        List<DishVO> dishVOList = (List<DishVO>) valueOperations.get(key);
        // 命中redis缓存，返回结果
        if(dishVOList != null && !dishVOList.isEmpty()){
            return Result.success(dishVOList);
        }
        // redis中不存在，查询数据库，并将结果存入redis
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品
        // 查询数据库
        List<DishVO> list = dishService.listWithFlavor(dish);
        // 放入redis
        valueOperations.set(key, list);
        return Result.success(list);
    }

}
