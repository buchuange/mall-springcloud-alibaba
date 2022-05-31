package com.dawn.mall.utils;

import com.dawn.mall.enums.ResultEnum;
import com.dawn.mall.vo.ResultVo;

/**
 * @Author: Dawn
 * @Date: 2022/5/11 19:16
 */
public class ResultVoUtil {

    public static <T> ResultVo<T> success(String msg) {
        return new ResultVo<>(ResultEnum.SUCCESS.getCode(), msg);
    }

    public static <T> ResultVo<T> success(T data) {
        return new ResultVo<>(ResultEnum.SUCCESS.getCode(), data);
    }

    public static <T> ResultVo<T> success(String msg, T data) {
        return new ResultVo<>(ResultEnum.SUCCESS.getCode(), msg, data);
    }

    public static <T> ResultVo<T> error(Integer code, String msg) {
        return new ResultVo<>(code, msg);
    }

    public static <T> ResultVo<T> error(ResultEnum resultEnum) {
        return new ResultVo<>(resultEnum.getCode(), resultEnum.getDesc());
    }

    public static <T> ResultVo<T> error(T data) {

        return new ResultVo<>(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getDesc(), data);
    }

}
