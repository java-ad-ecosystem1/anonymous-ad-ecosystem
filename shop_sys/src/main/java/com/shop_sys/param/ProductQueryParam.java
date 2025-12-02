package com.shop_sys.param;

import com.shop_sys.common.PageParam;

import java.math.BigInteger;

/**
 * 产品查询参数类
 */
public class ProductQueryParam extends PageParam {
    private static final long serialVersionUID = 1L;

    private BigInteger id;

    /**
     * 产品类型
     */
    private String productType;

    /**
     * 产品名称（模糊查询）
     */
    private String productName;

    public ProductQueryParam() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}

