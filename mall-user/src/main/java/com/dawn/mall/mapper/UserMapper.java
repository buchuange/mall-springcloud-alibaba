package com.dawn.mall.mapper;

import com.dawn.mall.domain.User;

public interface UserMapper {

    User selectByUsername(String username);

    int countByEmail(String email);

    int insertSelective(User user);

    int countByUsername(String username);
}
