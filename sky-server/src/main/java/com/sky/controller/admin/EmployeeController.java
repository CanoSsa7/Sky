package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@Api(tags = "员工相关接口")
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation(value = "员工登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims
        );

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation(value = "员工退出")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    @ApiOperation(value = "新增员工")
    @PostMapping
    public Result<String> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工: {}", employeeDTO);
        employeeService.addEmployee(employeeDTO);
        return Result.<String>success("用户添加成功");
    }

    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> pageSelectEmp(EmployeePageQueryDTO queryDTO) {
        log.info("分页查询员工：{}", queryDTO);
        PageResult pageResult = employeeService.pageQuery(queryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用、禁用员工账号")
    Result changeEmpState(@PathVariable Integer status, @RequestParam(value = "id") Long id){
        log.info("改变账号状态：{} {}",status, id);
        employeeService.changeEmpStatus(status, id);
        return Result.success();
    }
    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工")
    Result<Employee> getEmployeeById(@PathVariable(value = "id") Long id){
        log.info("查询员工信息，id为：{}", id);
        Employee emp = employeeService.getEmployeeById(id);
        return Result.<Employee>success(emp);
    }

    @PutMapping()
    @ApiOperation("编辑员工信息")
    Result<Employee> setEmployeeInfo(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑员工信息: {}", employeeDTO);
        employeeService.updateEmp(employeeDTO);
        return Result.success();
    }

}
