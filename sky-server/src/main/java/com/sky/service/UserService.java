package com.sky.service;

import com.sky.entity.User;

public interface UserService {
    User userWxLogin(String code);
}
