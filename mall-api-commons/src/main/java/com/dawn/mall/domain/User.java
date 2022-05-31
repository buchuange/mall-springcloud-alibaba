package com.dawn.mall.domain;

import com.dawn.mall.dto.Converter;
import com.dawn.mall.vo.UserVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Author: Dawn
 * @Date: 2022/5/11 17:44
 */
@Data
public class User {

    /**
     * 用户Id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码 （MD5加密）
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 找回密码问题
     */
    private String question;

    /**
     * 找回密码答案
     */
    private String answer;

    /**
     * 角色0-管理员,1-普通用户
     */
    private Integer role;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public UserVo convertToUserVo() {

        UserConvert userConvert = new UserConvert();

        return userConvert.convert(this);
    }

    private static class UserConvert implements Converter<User, UserVo> {

        @Override
        public UserVo convert(User user) {

            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);

            return userVo;
        }
    }
}
