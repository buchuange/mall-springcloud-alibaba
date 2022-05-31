package com.dawn.mall.dto;

import com.dawn.mall.domain.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;

/**
 * @Author: Dawn
 * @Date: 2022/5/14 03:31
 */
@Data
public class UserRegisterDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    public User convertToUser() {

        UserRegisterDTOConvert userRegisterDTOConvert = new UserRegisterDTOConvert();

        return userRegisterDTOConvert.convert(this);
    }


    private static class UserRegisterDTOConvert implements Converter<UserRegisterDTO, User> {

        @Override
        public User convert(UserRegisterDTO userRegisterDTO) {

            User user = new User();
            BeanUtils.copyProperties(userRegisterDTO, user);

            return user;
        }
    }
}
