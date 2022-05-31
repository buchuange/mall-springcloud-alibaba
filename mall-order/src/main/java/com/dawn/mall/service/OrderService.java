package com.dawn.mall.service;

import com.dawn.mall.vo.OrderVo;
import com.dawn.mall.vo.ResultVo;
import com.github.pagehelper.PageInfo;

public interface OrderService {

    ResultVo<OrderVo> saveOrder(Integer userId, Integer shippingId);

    ResultVo<PageInfo> listOrder(Integer userId, Integer pageNum, Integer pageSize);

    ResultVo<OrderVo> getOrder(Integer userId, String orderNo);

    ResultVo<String> cancelOrder(Integer userId, String orderNo);

    void paid(String orderNo);
}
