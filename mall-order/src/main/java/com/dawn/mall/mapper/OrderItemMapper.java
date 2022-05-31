package com.dawn.mall.mapper;

import com.dawn.mall.domain.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface OrderItemMapper {

    int insertBatch(List<OrderItem> orderItemList);

    List<OrderItem> listAllByOrderNo(@Param("orderNoSet") Set<String> orderNoSet);
}
