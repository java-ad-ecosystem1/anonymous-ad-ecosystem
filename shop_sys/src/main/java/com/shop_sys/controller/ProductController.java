package com.shop_sys.controller;

import com.shop_sys.common.PageResult;
import com.shop_sys.common.Result;
import com.shop_sys.common.ResultCode;
import com.shop_sys.model.ProductModel;
import com.shop_sys.param.ProductQueryParam;
import com.shop_sys.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 产品控制器
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 分页查询产品列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result<PageResult<ProductModel>> getProductList(ProductQueryParam param) {
        PageResult<ProductModel> pageResult = productService.getProductList(param);
        return Result.success(pageResult);
    }

    /**
     * 根据ID查询产品详情
     *
     * @param param 查询参数
     * @return 产品信息
     */
    @GetMapping("/detail")
    public Result<ProductModel> getProductById(ProductQueryParam param) {
        if (param.getId() == null) {
            return Result.error(ResultCode.PARAM_ERROR.getCode(), "产品ID不能为空");
        }
        return Result.success(productService.getProductById(param));
    }
}

