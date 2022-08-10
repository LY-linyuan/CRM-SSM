package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.User;

import java.util.Map;

/**
 * @Author 临渊
 * @Date 2022-08-10 9:56
 */
public interface UserService {
    User queryUserByLoginActAndPwd(Map<String, Object> map);
}
