package com.advertisement_sys.service;

import com.advertisement_sys.model.LoginResult;
import com.advertisement_sys.param.LoginParam;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户登录
     */
    LoginResult login(LoginParam loginParam);
}

