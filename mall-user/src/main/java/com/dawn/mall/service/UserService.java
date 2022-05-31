package com.dawn.mall.service;

import com.dawn.mall.domain.User;
import com.dawn.mall.vo.ResultVo;
import com.dawn.mall.vo.UserVo;

public interface UserService {

    UserVo login(String username, String password);

    ResultVo<String> register(User user);
}
