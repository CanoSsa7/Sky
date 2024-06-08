package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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
    @Autowired
    SetMealDishMapper setMealDishMapper;

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

    @Override
    @Transactional
    public void deleteDishes(List<Long> ids) {
        //判断菜品是否能删除
        //是否有处于起售状态
        List<Dish> onSellDishes = dishMapper.getOnSellDishesByIds(ids);
        if(onSellDishes != null && !onSellDishes.isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }

        //是否被套餐关联
        List<Long> setMealIds = setMealDishMapper.getSetMealIdsByDishIds(ids);
        if(setMealIds != null && !setMealIds.isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除关联的菜品口味
        dishFlavorMapper.deleteFlavorsByDishIds(ids);
        //删除菜品
        dishMapper.deleteDishByIds(ids);
    }

    @Override
    public DishVO getDishById(Long id) {
        return dishMapper.getDishById(id);
    }

    @Override
    public void updateDish(DishDTO dishDTO) {
        Dish newDish = new Dish();
        BeanUtils.copyProperties(dishDTO, newDish);
        //更改菜品基础信息
        dishMapper.upDateDish(newDish);
        //删除对应菜品口味信息，再添加传过来的口味
        dishFlavorMapper.deleteFlavorsByDishId(newDish.getId());
        //获取新口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        //更新每个口味的对应菜品id
        if(flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> {
                flavor.setDishId(newDish.getId());
            });
            dishFlavorMapper.addBatch(flavors);
        }
    }

    @Override
    public void setStatus(Long dishId, Integer status) {
        if(!status.equals(StatusConstant.ENABLE) && !status.equals(StatusConstant.DISABLE)){
            throw new SetmealEnableFailedException("传入状态非法!");
        }
        dishMapper.setStatus(dishId, status);
    }

    @Override
    public List<Dish> getDishByCategoryId(Long categoryID) {
        return dishMapper.getDishByCategoryId(categoryID);
    }

    /**
     * 根据分类id查询在售中的菜品和口味
     * @param dish 包含categoryId和status
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish){
        List<Dish> dishList = dishMapper.getDishByCategoryId(dish.getCategoryId());
        // 返回空列表，避免后续空指针异常
        if (dishList == null || dishList.isEmpty()) {
            return Collections.emptyList();
        }
        // 保留为在售状态的菜品，懒得新写mapper了
        dishList = dishList.stream()
                .filter(dish1 ->
                        dish1.getStatus().equals(StatusConstant.ENABLE))
                .collect(Collectors.toList());
        // 返回空列表，如果没有在售菜品
        if (dishList.isEmpty()) {
            return Collections.emptyList();
        }
        // 获得所有菜品的id
        List<Long> dishIdList = dishList.stream()
                .map(Dish::getId)
                .collect(Collectors.toList());

        // 查询所有菜品的口味
        List<DishFlavor> flavorsByDishIds = dishFlavorMapper.getFlavorsByDishIds(dishIdList);

        // 生成一个map，其中key是dishId, value是List<DishFlavor>
        Map<Long, List<DishFlavor>> flavorsMap = flavorsByDishIds.stream()
                .collect(Collectors.groupingBy(DishFlavor::getDishId));

        List<DishVO> dishVOList = new ArrayList<>();
        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);
            // 获取菜品的所有口味
            dishVO.setFlavors(flavorsMap.get(d.getId()));
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
