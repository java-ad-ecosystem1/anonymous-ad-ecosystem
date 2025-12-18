package com.advertisement_sys.service;

import com.advertisement_sys.common.PageResult;
import com.advertisement_sys.model.AdvertisementModel;
import com.advertisement_sys.param.AdvertisementParam;
import com.advertisement_sys.param.AdvertisementQueryParam;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigInteger;
import java.util.List;

/**
 * 广告服务接口
 */
public interface AdvertisementService {

    /**
     * 根据ID查询广告
     */
    AdvertisementModel getById(BigInteger id);

    /**
     * 查询所有广告
     */
    List<AdvertisementModel> getAll();

    /**
     * 分页查询广告
     */
    PageResult<AdvertisementModel> queryByPage(AdvertisementQueryParam param);

    /**
     * 保存广告（新增或更新）
     */
    int save(AdvertisementParam advertisementParam);

    /**
     * 投放广告
     */
    int putAds(BigInteger id);

    /**
     * 根据投放类型查询最新广告
     */
    AdvertisementModel queryLastAds(AdvertisementParam advertisementParam);
}

