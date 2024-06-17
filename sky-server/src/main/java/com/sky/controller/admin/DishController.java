package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("admin/dish")
@Slf4j
public class DishController {
    @Autowired
    DishService dishService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    CategoryService categoryService;

    /**
     * 添加菜品
     * @param dishDTO
     * @return
     */
    @PostMapping("")
    public Result addDish(@RequestBody DishDTO dishDTO){
        log.info("新增菜品, {}", dishDTO);
        dishService.addDish(dishDTO);
        // 清理redis中的缓存数据
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);
        return Result.success();
    }

    /**
     * 分页查询菜品
     * @param queryDTO 前端传参形式为param，自动映射
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> getDishPage(DishPageQueryDTO queryDTO){
        log.info("分页查询菜品");
        PageResult dishPage = dishService.getDishPage(queryDTO);
        return Result.success(dishPage);
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping()
    public Result deleteDishes(@RequestParam List<Long> ids){ //自动解析由逗号分隔的id字符串
        log.info("批量删除菜品, {}", ids);
        dishService.deleteDishes(ids);
        // 将所有菜品缓存清理掉
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return Result.success("删除成功");
    }

    @GetMapping("/{id}")
//    @Cacheable(cacheNames = )
    public Result<DishVO> getDishById(@PathVariable Long id){
        log.info("根据id查询菜品，{}",id);
        DishVO dishDTO = dishService.getDishById(id);
        return Result.success(dishDTO);
    }

    @PutMapping()
//    @CachePut(cacheNames = "dish", key = "#dishDTO.categoryId")
    public Result updateDish(@RequestBody DishDTO dishDTO){
        dishService.updateDish(dishDTO);
        // 将所有菜品缓存清理掉
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return Result.success();
    }

    /**
     *
     * @param status
     * @param dishId
     * @return
     */
    @PostMapping("/status/{status}")
    public Result updateDishStatus(@PathVariable Integer status, @RequestParam(value = "id") Long dishId){
        dishService.setStatus(dishId, status);
        // 将所有菜品缓存清理掉
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result getDishByCategoryId(@RequestParam Long categoryId){
        List<Dish> dishList = dishService.getDishByCategoryId(categoryId);
        return Result.success(dishList);
    }

    /**
     * 清理缓存数据
     * @param pattern
     */
    private void cleanCache(String pattern){
        // 将所有菜品缓存清理掉
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
