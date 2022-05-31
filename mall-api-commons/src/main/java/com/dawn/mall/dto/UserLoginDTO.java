package com.dawn.mall.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: Dawn
 * @Date: 2022/5/14 03:31
 */
@Data
public class UserLoginDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
