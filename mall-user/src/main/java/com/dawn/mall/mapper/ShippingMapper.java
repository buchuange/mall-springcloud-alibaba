package com.dawn.mall.mapper;

import com.dawn.mall.domain.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {

    int insertSelective(Shipping shipping);

    int delByIdAndUserId(@Param("shippingId") Integer shippingId,
                         @Param("userId") Integer userId);

    int updateByIdAndUserId(Shipping shipping);

    List<Shipping> listAllByUserId(@Param("userId") Integer userId);

    Shipping selectByIdAndUserId(@Param("shippingId") Integer shippingId,
                                 @Param("userId") Integer userId);
}
