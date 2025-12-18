package com.advertisement_sys.service.impl;

import com.advertisement_sys.dao.UserMapper;
import com.advertisement_sys.entity.User;
import com.advertisement_sys.model.LoginResult;
import com.advertisement_sys.param.LoginParam;
import com.advertisement_sys.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 用户服务实现类
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public LoginResult login(LoginParam loginParam) {
        // 参数校验
        if (loginParam.getUsername() == null || loginParam.getUsername().trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (loginParam.getPassword() == null || loginParam.getPassword().trim().isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }

        // 根据用户名查询用户
        User user = userMapper.selectByUsername(loginParam.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new RuntimeException("用户已被禁用");
        }

        // 验证密码（简单比较，实际项目中应使用加密后的密码比较）
        if (!loginParam.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 构建登录结果
        LoginResult loginResult = new LoginResult();
        loginResult.setUserId(user.getId());
        loginResult.setUsername(user.getUsername());

        return loginResult;
    }

}

