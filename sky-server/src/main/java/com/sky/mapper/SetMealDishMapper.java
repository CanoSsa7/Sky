package com.sky.mapper;

import com.sky.entity.SetmealDish;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    /**
     * 根据菜品id查询套餐id
     * @param ids
     * @return
     */
    List<Long> getSetMealIdsByDishIds(List<Long> ids);


    void addsetMealDishes(List<SetmealDish> setMealDishes);

    @Delete("delete from setmeal_dish where setmeal_id = #{setMealId} ")
    void deleteSetMealDishByMealId(Long setMealId);

    @Select("select * from setmeal_dish where id = #{id} ")
    List<SetmealDish> getSetMealByDishId(@Param("id") Long id);

    void setStatusById(@Param("id") Long id,@Param("status") Integer status);
}
