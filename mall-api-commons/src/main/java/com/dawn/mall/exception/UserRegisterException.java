package com.dawn.mall.exception;


import com.dawn.mall.enums.ResultEnum;
import lombok.Getter;

/**
 * @Author: Dawn
 * @Date: 2022/5/13 00:09
 */
@Getter
public class UserRegisterException extends RuntimeException {

    private final ResultEnum resultEnum;

    public UserRegisterException(ResultEnum resultEnum) {

        super(resultEnum.getDesc());
        this.resultEnum = resultEnum;
    }
}
