package com.advertisement_sys.dao;

import com.advertisement_sys.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据ID查询用户
     */
    User selectById(@Param("id") BigInteger id);

}

