package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/dish")
@Slf4j
public class DishController {
    @Autowired
    DishService dishService;

    /**
     * 添加菜品
     * @param dishDTO
     * @return
     */
    @PostMapping("")
    public Result addDish(@RequestBody DishDTO dishDTO){
        log.info("新增菜品, {}", dishDTO);
        dishService.addDish(dishDTO);
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

}
