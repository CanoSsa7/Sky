package com.sky.service;

import com.sky.dto.DishDTO;
import org.springframework.stereotype.Service;


public interface DishService {
    //新增菜品和对应口味
    void addDish(DishDTO dishDTO);
}