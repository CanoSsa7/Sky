package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 添加员工
     * @param employee
     */
    @AutoFill (value = OperationType.INSERT)
    @Insert("insert into employee(name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user, status)" +
            "values " +
            "(#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser}, #{status})")
    void insertEmp(Employee employee);

    /**
     * 分页查询员工
     * @param queryDTO
     * @return
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO queryDTO);

    /**
     * 改变员工信息
     * @param employee
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);

    /**
     * 查询员工信息
     * @param id
     * @return
     */
    @Select("select * from employee where id = #{id} ")
    Employee getEmployeeById(Long id);
}
