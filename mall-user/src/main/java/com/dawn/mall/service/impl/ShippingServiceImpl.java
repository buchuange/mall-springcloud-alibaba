package com.dawn.mall.service.impl;

import com.dawn.mall.domain.Shipping;
import com.dawn.mall.enums.ResultEnum;
import com.dawn.mall.exception.MallException;
import com.dawn.mall.mapper.ShippingMapper;
import com.dawn.mall.service.ShippingService;
import com.dawn.mall.utils.ResultVoUtil;
import com.dawn.mall.vo.ResultVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Dawn
 * @Date: 2022/5/16 02:56
 */
@Service
public class ShippingServiceImpl implements ShippingService {

    @Resource
    private ShippingMapper shippingMapper;

    @Override
    public ResultVo<Map<String, Integer>> saveShipping(Shipping shipping) {

        int resultCount = shippingMapper.insertSelective(shipping);

        if (resultCount != 1) {
            throw new MallException(ResultEnum.FAILURE.getCode(), "新建地址失败");
        }

        Map<String, Integer> map = new HashMap<>();
        map.put("shippingId", shipping.getId());

        return ResultVoUtil.success("新建地址成功", map);
    }

    @Override
    public ResultVo<String> deleteShipping(Integer shippingId, Integer userId) {

        int resultCount = shippingMapper.delByIdAndUserId(shippingId, userId);

        if (resultCount != 1) {
            throw new MallException(ResultEnum.FAILURE.getCode(), "删除地址失败");
        }

        return ResultVoUtil.success("删除地址成功");
    }

    @Override
    public ResultVo<String> updateShipping(Shipping shipping) {

        int resultCount = shippingMapper.updateByIdAndUserId(shipping);

        if (resultCount != 1) {
            throw new MallException(ResultEnum.FAILURE.getCode(), "更新地址失败");
        }

        return ResultVoUtil.success("更新地址成功");
    }

    @Override
    public ResultVo<PageInfo> listAllByUserId(Integer userId, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        List<Shipping> shippingList = shippingMapper.listAllByUserId(userId);

        PageInfo<Shipping> shippingPageInfo = new PageInfo<>(shippingList);

        return ResultVoUtil.success(shippingPageInfo);
    }

    @Override
    public ResultVo<Shipping> selectByIdAndUserId(Integer shippingId, Integer userId) {

        Shipping shipping = shippingMapper.selectByIdAndUserId(shippingId, userId);

        return ResultVoUtil.success(shipping);
    }
}
