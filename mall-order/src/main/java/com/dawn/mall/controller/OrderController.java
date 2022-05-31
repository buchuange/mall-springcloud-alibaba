package com.dawn.mall.controller;

import com.dawn.mall.domain.User;
import com.dawn.mall.dto.OrderCreateDTO;
import com.dawn.mall.service.OrderService;
import com.dawn.mall.utils.MallUtil;
import com.dawn.mall.vo.OrderVo;
import com.dawn.mall.vo.ResultVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @Author: Dawn
 * @Date: 2022/5/21 02:01
 */
@RestController
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("/orders")
    public ResultVo<OrderVo> saveOrder(@Valid @RequestBody OrderCreateDTO orderCreateDTO,
                                       HttpServletRequest request) {

        User user = MallUtil.getUserInfo(request);

        return orderService.saveOrder(user.getId(), orderCreateDTO.getShippingId());
    }

    @GetMapping("/orders")
    public ResultVo<PageInfo> listOrder(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                        HttpServletRequest request) {

        User user = MallUtil.getUserInfo(request);

        return orderService.listOrder(user.getId(), pageNum, pageSize);
    }

    @GetMapping("/orders/{orderNo}")
    public ResultVo<OrderVo> getOrder(@PathVariable("orderNo") String orderNo,
                                      HttpServletRequest request) {

        User user = MallUtil.getUserInfo(request);

        return orderService.getOrder(user.getId(), orderNo);
    }

    @PutMapping("/orders/{orderNo}")
    public ResultVo<String> cancelOrder(@PathVariable("orderNo") String orderNo,
                                      HttpServletRequest request) {

        User user = MallUtil.getUserInfo(request);

        return orderService.cancelOrder(user.getId(), orderNo);
    }
}
