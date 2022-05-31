package com.dawn.mall.service.impl;

import cn.hutool.json.JSONUtil;
import com.dawn.mall.ProductApplicationTests;
import com.dawn.mall.enums.ResultEnum;
import com.dawn.mall.service.CategoryService;
import com.dawn.mall.vo.CategoryVo;
import com.dawn.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
public class CategoryServiceImplTest extends ProductApplicationTests {

    @Resource
    private CategoryService categoryService;

    @Test
    public void listUpAll() {

        ResultVo<List<CategoryVo>> listCategories = categoryService.listUpAll();

        log.info("result={}", JSONUtil.toJsonPrettyStr(listCategories));

        Assert.state(ResultEnum.SUCCESS.getCode() == (listCategories.getStatus()), "测试失败");
    }
}