package com.advertisement_sys.controller;

import com.advertisement_sys.common.PageResult;
import com.advertisement_sys.common.Result;
import com.advertisement_sys.common.ResultCode;
import com.advertisement_sys.model.AdvertisementModel;
import com.advertisement_sys.param.AdvertisementParam;
import com.advertisement_sys.param.AdvertisementQueryParam;
import com.advertisement_sys.service.AdvertisementService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;

/**
 * 广告控制器
 */
@RestController
@RequestMapping("/api/advertisement")
public class AdvertisementController {

    @Resource
    private AdvertisementService advertisementService;

    /**
     * 分页查询广告
     */
    @PostMapping("/list")
    public Result<PageResult<AdvertisementModel>> queryByPage(@RequestBody AdvertisementQueryParam param) {
        try {
            PageResult<AdvertisementModel> pageResult = advertisementService.queryByPage(param);
            return Result.success("查询成功", pageResult);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询广告
     */
    @GetMapping("/{id}")
    public Result<AdvertisementModel> getById(@PathVariable BigInteger id) {
        try {
            AdvertisementModel advertisementModel = advertisementService.getById(id);
            if (advertisementModel != null) {
                return Result.success("查询成功", advertisementModel);
            } else {
                return Result.error(ResultCode.DATA_NOT_FOUND);
            }
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存广告（新增或更新）
     */
    @PostMapping("/save")
    public Result<BigInteger> save(@RequestBody AdvertisementParam advertisementParam) {
        try {
            int count = advertisementService.save(advertisementParam);
            if (count > 0) {
                return Result.success("保存成功", advertisementParam.getId());
            } else {
                return Result.error("保存失败");
            }
        } catch (Exception e) {
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 投放
     */
    @PostMapping("/putAds/{id}")
    public Result<BigInteger> putAds(@PathVariable BigInteger id) {
        try {
            int count = advertisementService.putAds(id);
            if (count > 0) {
                return Result.success("投放成功",id);
            } else {
                return Result.error(ResultCode.DATA_NOT_FOUND);
            }
        } catch (Exception e) {
            return Result.error("投放失败：" + e.getMessage());
        }
    }

    @GetMapping("/queryLastAds")
    public Result<AdvertisementModel> queryLastAds(AdvertisementParam advertisementParam){
        try {
            AdvertisementModel advertisementModel = advertisementService.queryLastAds(advertisementParam);
            if (advertisementModel != null) {
                return Result.success("查询成功", advertisementModel);
            } else {
                return Result.error(ResultCode.DATA_NOT_FOUND);
            }
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

}

