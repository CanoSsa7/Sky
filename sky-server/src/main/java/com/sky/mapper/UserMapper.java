package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where user.openid = #{openid} ")
    User getUserByopenid(String openid);

    void insertUser(User user);
}
