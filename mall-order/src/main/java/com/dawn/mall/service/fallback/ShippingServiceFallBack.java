package com.dawn.mall.service.fallback;

import com.dawn.mall.domain.Shipping;
import com.dawn.mall.enums.ResultEnum;
import com.dawn.mall.service.ShippingService;
import com.dawn.mall.utils.ResultVoUtil;
import com.dawn.mall.vo.ResultVo;
import org.springframework.stereotype.Component;

/**
 * @Author: Dawn
 * @Date: 2022/5/21 02:27
 */
@Component
public class ShippingServiceFallBack implements ShippingService {

    @Override
    public ResultVo<Shipping> getByIdAndUid(Integer shippingId, Integer userId) {

        return ResultVoUtil.error(ResultEnum.MICROSERVICE_INVOKE_ERROR);
    }
}
