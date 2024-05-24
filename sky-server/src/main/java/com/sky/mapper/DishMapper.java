package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void addDish(Dish dish);

    /**
     * 分页查询菜品
     * @param queryDTO
     * @return
     */
    Page<DishVO> getDishPage(DishPageQueryDTO queryDTO);

    /**
     * 批量查询起售中菜品
     * @param ids
     * @return
     */
    List<Dish> getOnSellDishesByIds(List<Long> ids);

    DishVO getDishById(Long id);

    void deleteDishByIds(List<Long> ids);

    @AutoFill(OperationType.UPDATE)
    void upDateDish(Dish dish);

    @Update("update dish set status = #{status} where id = #{dishId}")
    void setStatus(@Param("dishId") Long dishId, @Param("status") Integer status);
}
