package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;


public interface DishService {
    //新增菜品和对应口味
    void addDish(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param queryDTO
     * @return
     */
    PageResult getDishPage(DishPageQueryDTO queryDTO);
}
