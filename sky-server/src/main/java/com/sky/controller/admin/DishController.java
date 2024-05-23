package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/dish")
@Slf4j
public class DishController {
    @Autowired
    DishService dishService;
    @PostMapping("")
    public Result addDish(@RequestBody DishDTO dishDTO){
        log.info("新增菜品, {}", dishDTO);
        dishService.addDish(dishDTO);
        return Result.success();
    }
}