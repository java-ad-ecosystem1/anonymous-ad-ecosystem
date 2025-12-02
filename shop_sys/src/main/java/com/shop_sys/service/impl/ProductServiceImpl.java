package com.shop_sys.service.impl;

import com.shop_sys.common.PageResult;
import com.shop_sys.dao.ProductMapper;
import com.shop_sys.entity.Product;
import com.shop_sys.model.ProductModel;
import com.shop_sys.model.ProductModel;
import com.shop_sys.param.ProductQueryParam;
import com.shop_sys.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品服务实现类
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public PageResult<ProductModel> getProductList(ProductQueryParam param) {
        // 查询总数
        Long total = productMapper.countProduct(param);
        
        // 查询列表
        List<Product> productList = productMapper.selectProductList(param);
        
        // 转换为VO
        List<ProductModel> modelList = new ArrayList<>();
        for (Product product : productList) {
            ProductModel model = new ProductModel();
            BeanUtils.copyProperties(product, model);
            modelList.add(model);
        }
        
        // 构建分页结果
        return new PageResult<>(modelList, total, param.getPageNum(), param.getPageSize());
    }

    @Override
    public ProductModel getProductById(ProductQueryParam param) {
        Product product = productMapper.selectProductById(param);
        if (product == null) {
            return null;
        }
        
        ProductModel model = new ProductModel();
        BeanUtils.copyProperties(product, model);
        return model;
    }

    /**
     * 将Entity列表转换为Model列表
     *
     * @param productList Entity列表
     * @return Model列表
     */
    private List<ProductModel> convertToModelList(List<Product> productList) {
        List<ProductModel> modelList = new ArrayList<>();
        if (productList != null && !productList.isEmpty()) {
            for (Product product : productList) {
                ProductModel model = convertToModel(product);
                modelList.add(model);
            }
        }
        return modelList;
    }

    /**
     * 将Entity转换为Model
     *
     * @param product Entity对象
     * @return Model对象
     */
    private ProductModel convertToModel(Product product) {
        if (product == null) {
            return null;
        }
        ProductModel model = new ProductModel();
        BeanUtils.copyProperties(product, model);
        return model;
    }
    
}

