package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author CanoSsa7
 */
@RestController
@Slf4j
@Api(tags = "套餐相关管理")
@RequestMapping("/admin/setmeal")
public class SetMealController {
    @Autowired
    SetMealService setMealService;

    /**
     * 分页查询
     * @param setmealPageQueryDTO 查询的param参数会自动映射到对象上
     * @return
     */
    @ApiOperation("分页查询套餐")
    @GetMapping("/page")
    public Result<PageResult> getMealsPage(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询");
        PageResult mealsPage = setMealService.getMealsPage(setmealPageQueryDTO);
        return Result.success(mealsPage);
    }
    @ApiOperation("新增套餐")
    @PostMapping
    public Result addMeal(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐");
        setMealService.addSetMeal(setmealDTO);
        return Result.success();
    }
    @ApiOperation("修改套餐")
    @PutMapping
    public Result updateSetMeal(@RequestBody SetmealDTO setmealDTO){
        setMealService.updateSetMeal(setmealDTO);
        return Result.success();
    }
    @ApiOperation("查询单个套餐")
    @GetMapping("/{id}")
    public Result<SetmealVO> getSetMeal(@PathVariable(value = "id") Long id){
        SetmealVO setmealVO =  setMealService.getMealById(id);
        return Result.success(setmealVO);
    }
    @ApiOperation("修改套餐状态")
    @PostMapping("/status/{status}")
    Result setStatus(@PathVariable Integer status,  @RequestParam Long id){
        log.info("修改套餐状态：{}, {}" , id, status);
        setMealService.setStatus(id, status);
        return Result.success();
    }
    @ApiOperation("批量删除套餐")
    @DeleteMapping()
    Result deleteBatch(@RequestParam List<Long> ids){// 自动解析由逗号拼接的id字符串
        log.info("批量删除套餐：{}", ids);
        setMealService.deleteBatch(ids);
        return Result.success();
    }
}
