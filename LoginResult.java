package com.advertisement_sys.model;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 登录结果模型类
 */
public class LoginResult implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private BigInteger userId;

    /**
     * 用户名
     */
    private String username;

    public LoginResult() {
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}

