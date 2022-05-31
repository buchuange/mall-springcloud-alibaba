package com.dawn.mall.exception;


import com.dawn.mall.enums.ResultEnum;
import lombok.Getter;

/**
 * @Author: Dawn
 * @Date: 2022/5/11 18:07
 */
@Getter
public class MallException extends RuntimeException {

    private final int code;

    public MallException(ResultEnum resultEnum) {

        super(resultEnum.getDesc());
        this.code = resultEnum.getCode();
    }

    public MallException(Integer code, String msg) {

        super(msg);
        this.code = code;
    }
}
