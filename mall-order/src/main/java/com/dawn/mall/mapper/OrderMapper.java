package com.dawn.mall.mapper;

import com.dawn.mall.domain.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Dawn
 * @Date: 2022/5/21 01:58
 */
public interface OrderMapper {

    int insertSelective(Order order);

    List<Order> listAllByUserId(@Param("userId") Integer userId);

    Order getByOrderNo(@Param("orderNo") String orderNo);

    int updateStatus(Order order);
}
