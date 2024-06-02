package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/dish")
@Slf4j
public class DishController {
    @Autowired
    DishService dishService;

    /**
     * 添加菜品
     * @param dishDTO
     * @return
     */
    @PostMapping("")
    public Result addDish(@RequestBody DishDTO dishDTO){
        log.info("新增菜品, {}", dishDTO);
        dishService.addDish(dishDTO);
        return Result.success();
    }

    /**
     * 分页查询菜品
     * @param queryDTO 前端传参形式为param，自动映射
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> getDishPage(DishPageQueryDTO queryDTO){
        log.info("分页查询菜品");
        PageResult dishPage = dishService.getDishPage(queryDTO);
        return Result.success(dishPage);
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping()
    public Result deleteDishes(@RequestParam List<Long> ids){ //自动解析由逗号分隔的id字符串
        log.info("批量删除菜品, {}", ids);
        dishService.deleteDishes(ids);
        return Result.success("删除成功");
    }

    @GetMapping("/{id}")
    public Result<DishVO> getDishById(@PathVariable Long id){
        log.info("根据id查询菜品，{}",id);
        DishVO dishDTO = dishService.getDishById(id);
        return Result.success(dishDTO);
    }

    @PutMapping()
    public Result updateDish(@RequestBody DishDTO dishDTO){
        dishService.updateDish(dishDTO);
        return Result.success();
    }
    @PostMapping("/status/{status}")
    public Result updateDishStatus(@PathVariable Integer status, @RequestParam(value = "id") Long dishId){
        dishService.setStatus(dishId, status);
        return Result.success();
    }
    @GetMapping("/list")
    public Result getDishByCategoryId(@RequestParam Long categoryId){
        List<Dish> dishList = dishService.getDishByCategoryId(categoryId);
        return Result.success(dishList);
    }
}
