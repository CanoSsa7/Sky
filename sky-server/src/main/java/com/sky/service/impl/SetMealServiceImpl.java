package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author CanoSsa7
 */
@Service
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    SetMealDishMapper setMealDishMapper;
    @Override
    public PageResult getMealsPage(SetmealPageQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        Page<SetmealVO> res= setmealMapper.getMealsPage(queryDTO);
        return new PageResult(res.getTotal(), res.getResult());
    }

    /**
     *
     * @param setmealDTO 前端传过来的套餐信息
     */
    @Override
    @Transactional
    public void addSetMeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //添加套餐表的基础信息
        setmealMapper.addMeal(setmeal);
        //拿到自增的套餐id
        Long setMealId = setmeal.getId();

        //给每个菜品绑定套餐
        List<SetmealDish> setMealDishes = setmealDTO.getSetmealDishes();
        setMealDishes.forEach((dish)->{
            dish.setSetmealId(setMealId);
        });
        //添加套餐与菜品的关系
        setMealDishMapper.addsetMealDishes(setMealDishes);
    }

    @Override
    public void updateSetMeal(SetmealDTO setmealDTO) {
        //更新套餐基础信息
        Setmeal setMeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setMeal);
        setmealMapper.update(setMeal);

        //更新套餐与菜品关系
        Long setMealId = setMeal.getId();
        setMealDishMapper.deleteSetMealDishByMealId(setMealId);
        List<SetmealDish> newDishes = setmealDTO.getSetmealDishes();
        newDishes.forEach(dish->{
            dish.setSetmealId(setMealId);
        });
        setMealDishMapper.addsetMealDishes(newDishes);
    }

    /**
     * 批量修改套餐
     * @param ids
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        setmealMapper.deleteBatch(ids);
    }


    /**
     * 修改套餐状态
     * @param id
     * @param status
     */
    @Override
    public void setStatus(Long id, Integer status) {
        setMealDishMapper.setStatusById(id, status);
    }

    @Override
    public SetmealVO getMealById(Long id) {
        //查套餐基础信息
        SetmealVO setmealVO = setmealMapper.getSetMealByid(id);

        //查套餐相关菜品信息
        List<SetmealDish> dishes =  setMealDishMapper.getSetMealByDishId(id);
        setmealVO.setSetmealDishes(dishes);

        return setmealVO;
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据套餐id查询包含所有菜品的详细信息
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
