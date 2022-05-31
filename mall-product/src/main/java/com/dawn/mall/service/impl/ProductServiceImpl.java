package com.dawn.mall.service.impl;

import com.dawn.mall.domain.Product;
import com.dawn.mall.enums.ProductStatusEnum;
import com.dawn.mall.enums.ResultEnum;
import com.dawn.mall.exception.MallException;
import com.dawn.mall.mapper.ProductMapper;
import com.dawn.mall.service.CategoryService;
import com.dawn.mall.service.ProductService;
import com.dawn.mall.utils.ResultVoUtil;
import com.dawn.mall.vo.ProductVo;
import com.dawn.mall.vo.ResultVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Dawn
 * @Date: 2022/5/17 18:58
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private CategoryService categoryService;

    /**
     * 获取在售状态的商品列表
     * @param categoryId 类目Id 查询时子类目的商品也要查出来
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResultVo<PageInfo> listOnSaleProducts(Integer categoryId, Integer pageNum, Integer pageSize) {

        Set<Integer> categoryIdSet = new HashSet<>();

        if (categoryId != null) {

            categoryService.findSubCategoryId(categoryId, categoryIdSet);
            categoryIdSet.add(categoryId);
        }

        PageHelper.startPage(pageNum, pageSize);

        List<Product> productList = productMapper.listUpAllByCategoryIdSet(ProductStatusEnum.ON_SALE.getCode(), categoryIdSet);

        List<ProductVo> productVoList = productList.stream()
                .map(Product::convertToVo)
                .collect(Collectors.toList());

        PageInfo pageInfo = new PageInfo<>(productList);

        pageInfo.setList(productVoList);

        return ResultVoUtil.success(pageInfo);
    }

    @Override
    public ResultVo<Product> getProductById(Integer productId) {

        Product product = productMapper.findById(productId);

        if (ObjectUtils.isEmpty(product) || ProductStatusEnum.OFF_SAGE.getCode() == product.getStatus() ||
        ProductStatusEnum.DELETE.getCode() == product.getStatus()) {

            log.info("【查询商品信息失败】商品Id: " + productId);

            throw new MallException(ResultEnum.PRODUCT_NOT_EXIST);

        }
        return ResultVoUtil.success(product);
    }

    @Override
    public ResultVo<List<Product>> listAllByIdSet(Set<Integer> productIdSet) {

        List<Product> productList = productMapper.listAllByIdSet(productIdSet);


        return ResultVoUtil.success(productList);
    }

    @Override
    public ResultVo<Integer> updateStock(Product product) {

        int result = productMapper.updateStock(product);

        if (result != 1) {
            throw new MallException(ResultEnum.FAILURE);
        }

        return ResultVoUtil.success(result);
    }
}
