package com.dawn.mall.domain;

import com.dawn.mall.dto.Converter;
import com.dawn.mall.vo.CartProductVo;
import com.dawn.mall.vo.ProductVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Product {

    /**
     * 商品id
     */
    private Integer id;

    /**
     * 分类id,对应mall_category表的主键
     */
    private Integer categoryId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品副标题
     */
    private String subtitle;

    /**
     * 产品主图,url相对地址
     */
    private String mainImage;

    /**
     * 图片地址,json格式,扩展用
     */
    private String subImages;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 价格,单位-元保留两位小数
     */
    private BigDecimal price;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 商品状态.1-在售 2-下架 3-删除
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;


    public ProductVo convertToVo() {

        ProductConvert productConvert = new ProductConvert();

        return productConvert.convert(this);
    }


    public static class ProductConvert implements Converter<Product, ProductVo> {

        @Override
        public ProductVo convert(Product product) {

            ProductVo productVo = new ProductVo();

            BeanUtils.copyProperties(product, productVo);

            return productVo;
        }
    }
}