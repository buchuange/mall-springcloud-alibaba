package com.dawn.mall.service.fallback;

import com.dawn.mall.domain.Product;
import com.dawn.mall.enums.ResultEnum;
import com.dawn.mall.service.ProductService;
import com.dawn.mall.utils.ResultVoUtil;
import com.dawn.mall.vo.ProductVo;
import com.dawn.mall.vo.ResultVo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @Author: Dawn
 * @Date: 2022/5/18 01:55
 */
@Component
public class ProductServiceFallBack implements ProductService {

    @Override
    public ResultVo<Product> getProductById(Integer productId) {

        return ResultVoUtil.error(ResultEnum.MICROSERVICE_INVOKE_ERROR);
    }

    @Override
    public ResultVo<List<Product>> listAllByIdSet(Set<Integer> productIdSet) {

        return ResultVoUtil.error(ResultEnum.MICROSERVICE_INVOKE_ERROR);
    }
}
