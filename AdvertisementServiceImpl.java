package com.advertisement_sys.service.impl;

import com.advertisement_sys.model.BeanUtils;
import com.advertisement_sys.common.PageResult;
import com.advertisement_sys.dao.AdvertisementMapper;
import com.advertisement_sys.entity.Advertisement;
import com.advertisement_sys.model.AdvertisementModel;
import com.advertisement_sys.param.AdvertisementParam;
import com.advertisement_sys.param.AdvertisementQueryParam;
import com.advertisement_sys.service.AdvertisementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * 广告服务实现类
 */
@Service
@Transactional
public class AdvertisementServiceImpl implements AdvertisementService {

    @Resource
    private AdvertisementMapper advertisementMapper;

    @Override
    public AdvertisementModel getById(BigInteger id) {
        Advertisement advertisement = advertisementMapper.selectById(id);
        return BeanUtils.toModel(advertisement);
    }

    @Override
    public List<AdvertisementModel> getAll() {
        List<Advertisement> advertisementList = advertisementMapper.selectAll();
        return BeanUtils.toModelList(advertisementList);
    }

    @Override
    public PageResult<AdvertisementModel> queryByPage(AdvertisementQueryParam param) {
        // 查询总数
        Long total = advertisementMapper.countByCondition(param);
        
        // 查询分页数据
        List<Advertisement> entityList = advertisementMapper.selectByPage(param);
        
        // 转换为Model
        List<AdvertisementModel> modelList = BeanUtils.toModelList(entityList);
        
        // 构建分页结果
        return new PageResult<>(
            modelList,
            total,
            param.getPageNum(),
            param.getPageSize()
        );
    }

    @Override
    public int save(AdvertisementParam advertisementParam) {
        if (advertisementParam.getId() == null) {
            return insert(advertisementParam);
        } else {
            return update(advertisementParam);
        }
    }

    private int insert(AdvertisementParam advertisementParam) {
        if (advertisementParam.getCreateTime() == null) {
            advertisementParam.setCreateTime(new Date());
        }
        return advertisementMapper.insert(advertisementParam);
    }

    private int update(AdvertisementParam advertisementParam) {
        return advertisementMapper.update(advertisementParam);
    }

    @Override
    public int putAds(BigInteger id) {
        // 投放广告：将putFlag设置为true
        Advertisement advertisement = advertisementMapper.selectById(id);
        if (advertisement != null) {
            AdvertisementParam advertisementParam = new AdvertisementParam();
            advertisementParam.setId(advertisement.getId());
            advertisementParam.setPutFlag(true);
            return advertisementMapper.update(advertisementParam);
        }
        return 0;
    }

    @Override
    public AdvertisementModel queryLastAds(AdvertisementParam advertisementParam) {
        advertisementParam.setPutFlag(true);
        Advertisement advertisement = advertisementMapper.queryLastAds(advertisementParam);
        return BeanUtils.toModel(advertisement);
    }
}

