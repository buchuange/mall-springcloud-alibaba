package com.dawn.mall.controller;

import com.dawn.mall.domain.PayInfo;
import com.dawn.mall.domain.User;
import com.dawn.mall.service.PayService;
import com.dawn.mall.utils.MallUtil;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @Author: Dawn
 * @Date: 2022/5/23 03:49
 */
@Controller
@RequestMapping("/pay")
public class PayController {

    @Resource
    private PayService payService;

    @Resource
    private WxPayConfig wxPayConfig;

    @GetMapping("/create")
    public String create(@RequestParam("orderId") String orderId,
                         @RequestParam("amount") BigDecimal amount,
                         @RequestParam("payType") BestPayTypeEnum bestPayTypeEnum,
                         HttpServletRequest request, Model model) {

        User user = MallUtil.getUserInfo(request);

        PayResponse payResponse = payService.create(user.getId(), orderId, amount, bestPayTypeEnum);

        model.addAttribute("codeUrl", payResponse.getCodeUrl());
        model.addAttribute("orderId", orderId);
        model.addAttribute("returnUrl", wxPayConfig.getReturnUrl());

        return "pay/create";
    }

    @PostMapping("/notify")
    public String asyncNotify(@RequestBody String notifyData) {

        payService.asyncNotify(notifyData);

        // 返回给微信处理结果
        return "pay/success";
    }

    @GetMapping("/queryByOrderId")
    @ResponseBody
    public PayInfo queryByOrderId(@RequestParam("orderId") String orderId) {
        return payService.getByOrderNo(orderId);
    }
}
