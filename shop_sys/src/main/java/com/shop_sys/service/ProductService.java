package com.shop_sys.service;

import com.shop_sys.common.PageResult;
import com.shop_sys.model.ProductModel;
import com.shop_sys.param.ProductQueryParam;

import java.math.BigInteger;

/**
 * 产品服务接口
 */
public interface ProductService {

    /**
     * 分页查询产品列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<ProductModel> getProductList(ProductQueryParam param);

    /**
     * 根据ID查询产品详情
     *
     * @param id 产品ID
     * @return 产品信息
     */
    ProductModel getProductById(ProductQueryParam param);
}

