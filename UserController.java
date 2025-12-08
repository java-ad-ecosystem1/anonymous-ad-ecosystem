package com.advertisement_sys.controller;

import com.advertisement_sys.common.Result;
import com.advertisement_sys.model.LoginResult;
import com.advertisement_sys.param.LoginParam;
import com.advertisement_sys.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户控制器
 */
@CrossOrigin(origins = "*")  // ← 添加这行
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResult> login(@RequestBody LoginParam loginParam) {
        try {
            LoginResult loginResult = userService.login(loginParam);
            return Result.success("登录成功", loginResult);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("登录失败：" + e.getMessage());
        }
    }
}

