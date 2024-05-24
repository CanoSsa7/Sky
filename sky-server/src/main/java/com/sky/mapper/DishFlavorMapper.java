package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 批量删除口味
     * @param dishIds
     */
    void deleteFlavorsByDishIds(List<Long> dishIds);

    /**
     * 根据菜品id删除口味
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId} ")
    void deleteFlavorsByDishId(Long dishId);

    /**
     * 批量添加口味
     * @param flavors
     */
    void addBatch(List<DishFlavor> flavors);

    @Select("select * from dish_flavor where dish_flavor.dish_id = #{id} ")
    List<DishFlavor> getFlavorByDishId(Long id);

    List<DishFlavor> getFlavorsByDishIds(List<Long> dishIds);
}
