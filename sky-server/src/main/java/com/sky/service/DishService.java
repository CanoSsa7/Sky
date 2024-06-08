package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DishService {
    //新增菜品和对应口味
    void addDish(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param queryDTO
     * @return
     */
    PageResult getDishPage(DishPageQueryDTO queryDTO);

    void deleteDishes(List<Long> ids);

    DishVO getDishById(Long id);

    void updateDish(DishDTO dishDTO);

    void setStatus(Long dishId, Integer status);

    List<Dish> getDishByCategoryId(Long categoryID);

    List<DishVO> listWithFlavor(Dish dish);
}
