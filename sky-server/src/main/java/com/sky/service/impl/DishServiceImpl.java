package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author CanoSsa7
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;
    // 新增菜品和对应口味
    @Override
    @Transactional
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 向菜品表插入一条数据
        dishMapper.addDish(dish);
        // 获取sql生成的id值，通过xml中的useGeneratedKeys和keyProperty将主键值自动映射回对象的字段
        Long dishId = dish.getId();
        // 向口味表插入多条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()){
            // 遍历List，为每一个口味都增加对应的菜品id
            flavors.forEach(flavor->{
                flavor.setDishId(dishId);
            });
            dishFlavorMapper.addBatch(flavors);
        }
    }
    public PageResult getDishPage(DishPageQueryDTO queryDTO) {
        //标记分页查询的参数
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        //查询
        Page<DishVO> page = dishMapper.getDishPage(queryDTO);
        //获取结果
        List<DishVO> result = page.getResult();
        return new PageResult(page.getTotal(), result);
    }

}
