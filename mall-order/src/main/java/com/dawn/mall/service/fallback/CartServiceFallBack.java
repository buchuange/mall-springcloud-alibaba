package com.dawn.mall.service.fallback;

import com.dawn.mall.domain.Cart;
import com.dawn.mall.enums.ResultEnum;
import com.dawn.mall.service.CartService;
import com.dawn.mall.utils.ResultVoUtil;
import com.dawn.mall.vo.CartVo;
import com.dawn.mall.vo.ResultVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Dawn
 * @Date: 2022/5/21 02:33
 */
@Component
public class CartServiceFallBack implements CartService {

    @Override
    public ResultVo<List<Cart>> listCartsByUserId(Integer userId) {

        return ResultVoUtil.error(ResultEnum.MICROSERVICE_INVOKE_ERROR);
    }

    @Override
    public ResultVo<CartVo> deleteCart(Integer userId, Integer productId) {
        return ResultVoUtil.error(ResultEnum.MICROSERVICE_INVOKE_ERROR);
    }
}
