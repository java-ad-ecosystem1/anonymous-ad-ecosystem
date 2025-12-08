package com.advertisement_sys.dao;

import com.advertisement_sys.entity.Advertisement;
import com.advertisement_sys.model.AdvertisementModel;
import com.advertisement_sys.param.AdvertisementParam;
import com.advertisement_sys.param.AdvertisementQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

/**
 * 广告Mapper接口
 */
@Mapper
public interface AdvertisementMapper {

    /**
     * 根据ID查询广告
     */
    Advertisement selectById(@Param("id") BigInteger id);

    /**
     * 查询所有广告
     */
    List<Advertisement> selectAll();

    /**
     * 分页查询广告列表
     */
    List<Advertisement> selectByPage(AdvertisementQueryParam param);

    /**
     * 统计符合条件的广告总数
     */
    Long countByCondition(AdvertisementQueryParam param);

    /**
     * 插入广告
     */
    int insert(AdvertisementParam advertisementParam);

    /**
     * 更新广告
     */
    int update(AdvertisementParam advertisementParam);

    /**
     * 根据投放类型查询最新广告
     */
    Advertisement queryLastAds(AdvertisementParam advertisementParam);

}

