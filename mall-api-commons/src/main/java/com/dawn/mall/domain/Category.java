package com.dawn.mall.domain;

import com.dawn.mall.dto.Converter;
import com.dawn.mall.vo.CategoryVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
public class Category {

    /**
     * 类别Id
     */
    private Integer id;

    /**
     * 父类别id当id=0时说明是根节点,一级类别
     */
    private Integer parentId;

    /**
     * 类别名称
     */
    private String name;

    /**
     * 类别状态1-正常,2-已废弃
     */
    private Boolean status;

    /**
     * 排序编号,同类展示顺序,数值相等则自然排序
     */
    private Integer sortOrder;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;


    public CategoryVo convertToVo() {

        CategoryConvert categoryConvert = new CategoryConvert();

        return categoryConvert.convert(this);
    }

    public static class CategoryConvert implements Converter<Category, CategoryVo> {

        @Override
        public CategoryVo convert(Category category) {

            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(category, categoryVo);

            return categoryVo;
        }
    }
}