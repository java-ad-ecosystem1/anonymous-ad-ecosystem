package com.shop_sys.dao;

import com.shop_sys.entity.Product;
import com.shop_sys.param.ProductQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品Mapper接口
 */
@Mapper
public interface ProductMapper {

    /**
     * 分页查询产品列表
     *
     * @param param 查询参数
     * @return 产品列表
     */
    List<Product> selectProductList(ProductQueryParam param);

    /**
     * 统计产品总数
     *
     * @param param 查询参数
     * @return 总数
     */
    Long countProduct(ProductQueryParam param);

    /**
     * 根据ID查询产品详情
     *
     * @param id 产品ID
     * @return 产品信息
     */
    Product selectProductById(ProductQueryParam param);
}

