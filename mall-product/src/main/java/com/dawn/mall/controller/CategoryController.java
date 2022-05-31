package com.dawn.mall.controller;

import cn.hutool.json.JSONUtil;
import com.dawn.mall.domain.User;
import com.dawn.mall.service.CategoryService;
import com.dawn.mall.utils.MallUtil;
import com.dawn.mall.vo.CategoryVo;
import com.dawn.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: Dawn
 * @Date: 2022/5/17 02:30
 */
@RestController
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/categories")
    public ResultVo<List<CategoryVo>> listAll() {

        return categoryService.listUpAll();
    }
}
