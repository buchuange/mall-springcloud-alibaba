package com.dawn.mall.service.impl;

import com.dawn.mall.domain.User;
import com.dawn.mall.enums.ResultEnum;
import com.dawn.mall.enums.RoleEnum;
import com.dawn.mall.exception.UserLoginException;
import com.dawn.mall.exception.UserRegisterException;
import com.dawn.mall.mapper.UserMapper;
import com.dawn.mall.service.UserService;
import com.dawn.mall.utils.ResultVoUtil;
import com.dawn.mall.vo.ResultVo;
import com.dawn.mall.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Dawn
 * @Date: 2022/5/12 23:20
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional
    public ResultVo<String> register(User user) {

        // username不能重复
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if (countByUsername > 0) {
            throw new UserRegisterException(ResultEnum.USERNAME_EXIST);
        }

        // email不能重复
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if (countByEmail > 0) {
            throw new UserRegisterException(ResultEnum.EMAIL_EXIST);
        }

        // MD5摘要算法(Spring自带)
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        user.setRole(RoleEnum.CUSTOMER.getCode());

        // 写入数据库
        int resultCount = userMapper.insertSelective(user);
        if (resultCount != 1) {
            throw new UserRegisterException(ResultEnum.FAILURE);
        }

        return ResultVoUtil.success("注册成功");
    }

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public UserVo login(String username, String password) {

        User user = userMapper.selectByUsername(username);

        if (ObjectUtils.isEmpty(user)) {
            throw new UserLoginException(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        if (!user.getPassword().equalsIgnoreCase(
                DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)))) {
            // 密码错误（返回用户名或密码错误）
            throw new UserLoginException(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        return user.convertToUserVo();
    }
}
