package com.dawn.mall.mapper;

import com.dawn.mall.domain.PayInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: Dawn
 * @Date: 2022/5/23 03:50
 */
public interface PayMapper {

    int insertSelective(PayInfo payInfo);

    PayInfo getByOrderNo(@Param("orderNo") String orderNo);

    int updateStatus(PayInfo payInfo);
}
