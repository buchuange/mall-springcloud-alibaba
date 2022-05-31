package com.dawn.mall.vo;

import com.dawn.mall.domain.User;
import com.dawn.mall.dto.Converter;
import com.dawn.mall.utils.serializer.Date2LongSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Author: Dawn
 * @Date: 2022/5/14 03:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 角色0-管理员,1-普通用户
     */
    private Integer role;

    /**
     * 创建时间
     */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    public User convertToUser() {

        UserVoConvert userVoConvert = new UserVoConvert();

        return userVoConvert.convert(this);
    }

    private static class UserVoConvert implements Converter<UserVo, User> {

        @Override
        public User convert(UserVo userVo) {

            User user = new User();
            BeanUtils.copyProperties(userVo, user);

            return user;
        }
    }
}
