package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 批量添加口味
     * @param flavors
     */
    void addBatch(List<DishFlavor> flavors);

    @Select("select * from dish_flavor where dish_flavor.dish_id = #{id} ")
    List<DishFlavor> getFlavorByDishId(Long id);

    List<DishFlavor> getFlavorsByDishIds(List<Long> dishIds);
}
