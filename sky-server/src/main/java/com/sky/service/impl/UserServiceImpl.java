package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    // 登录凭证校验。通过 wx.login 接口获得临时登录凭证 code 后
    // 传到开发者服务器调用此接口完成登录流程
    static final String LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    WeChatProperties weChatProperties;

    @Autowired
    UserMapper userMapper;
    @Override
    /**
     * 微信登录
     * @param code 暂时的用户登录凭证
     */
    public User userWxLogin(String code) {
        // 调用微信接口，使用 code 换取 openid（用户唯一标识）
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", weChatProperties.getAppid());
        paramMap.put("secret", weChatProperties.getSecret());
        paramMap.put("js_code", code);
        paramMap.put("grant_type", "authorization_code");
        String result = HttpClientUtil.doGet(LOGIN_URL, paramMap);

        // 解析结果
        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.getString("openid");

        // 如果openid为空，登录失败
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 判断用户是否为新用户，新用户自动完成注册
        User user = userMapper.getUserByopenid(openid);
        if(user == null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
        }
        userMapper.insertUser(user);

        return user;
    }

}
