package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetMealService {
    PageResult getMealsPage(SetmealPageQueryDTO setmealPageQueryDTO);
    void addSetMeal(SetmealDTO setmealDTO);

    void updateSetMeal(SetmealDTO setmealDTO);

    SetmealVO getMealById(Long id);

    void setStatus(Long id, Integer status);

    void deleteBatch(List<Long> ids);
}
