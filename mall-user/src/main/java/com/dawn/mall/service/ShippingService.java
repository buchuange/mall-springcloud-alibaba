package com.dawn.mall.service;

import com.dawn.mall.domain.Shipping;
import com.dawn.mall.vo.ResultVo;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface ShippingService {

    ResultVo<Map<String, Integer>> saveShipping(Shipping shipping);

    ResultVo<String> deleteShipping(Integer shippingId, Integer userId);

    ResultVo<String> updateShipping(Shipping shipping);

    ResultVo<PageInfo> listAllByUserId(Integer userId, Integer pageNum, Integer pageSize);

    ResultVo<Shipping> selectByIdAndUserId(Integer shippingId, Integer userId);
}
