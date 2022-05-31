package com.dawn.mall.controller;

import com.dawn.mall.domain.Shipping;
import com.dawn.mall.domain.User;
import com.dawn.mall.dto.ShippingDTO;
import com.dawn.mall.service.ShippingService;
import com.dawn.mall.utils.MallUtil;
import com.dawn.mall.vo.ResultVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * @Author: Dawn
 * @Date: 2022/5/16 02:44
 */
@RestController
public class ShippingController {

    @Resource
    private ShippingService shippingService;

    @PostMapping("/shippings")
    public ResultVo<Map<String, Integer>> saveShipping(@Valid @RequestBody ShippingDTO shippingDTO, HttpServletRequest request) {

        User user = MallUtil.getUserInfo(request);

        Shipping shipping = shippingDTO.convertToShipping();
        shipping.setUserId(user.getId());

        return shippingService.saveShipping(shipping);
    }

    @DeleteMapping("/shippings/{shippingId}")
    public ResultVo<String> deleteShipping(@PathVariable("shippingId") Integer shippingId,
                                           HttpServletRequest request) {

        User user = MallUtil.getUserInfo(request);

        return shippingService.deleteShipping(shippingId, user.getId());
    }

    @PutMapping("/shippings/{shippingId}")
    public ResultVo<String> updateShipping(@PathVariable("shippingId") Integer shippingId,
                                           @Valid @RequestBody ShippingDTO shippingDTO,
                                           HttpServletRequest request) {

        Shipping shipping = shippingDTO.convertToShipping();

        User user = MallUtil.getUserInfo(request);

        shipping.setId(shippingId);
        shipping.setUserId(user.getId());

        return shippingService.updateShipping(shipping);
    }

    @GetMapping("/shippings")
    public ResultVo<PageInfo> listShipping(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                           @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                           HttpServletRequest request) {

        User user = MallUtil.getUserInfo(request);

        return shippingService.listAllByUserId(user.getId(), pageNum, pageSize);
    }

    @GetMapping("/shippings/order")
    public ResultVo<Shipping> getByIdAndUid(@RequestParam("shippingId") Integer shippingId,
                                            @RequestParam("userId") Integer userId) {

        return shippingService.selectByIdAndUserId(shippingId, userId);
    }
}
