package com.dawn.mall.mapper;

import com.dawn.mall.domain.OrderShipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface OrderShippingMapper {

    int insertSelective(OrderShipping orderShipping);

    List<OrderShipping> listAllByOrderNo(@Param("orderNoSet") Set<String> orderNoSet);
}
