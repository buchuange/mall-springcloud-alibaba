package com.dawn.mall.handler;

import com.dawn.mall.enums.ResultEnum;
import com.dawn.mall.exception.AuthorizeException;
import com.dawn.mall.exception.MallException;
import com.dawn.mall.exception.UserLoginException;
import com.dawn.mall.exception.UserRegisterException;
import com.dawn.mall.utils.ResultVoUtil;
import com.dawn.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * @Author: Dawn
 * @Date: 2022/5/13 00:15
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResultVo<String> handleException(Exception e) {
        log.error("【服务器抛出了异常】:", e);
        return ResultVoUtil.error(e.getMessage());

    }

    @ExceptionHandler(MallException.class)
    public ResultVo<String> handle(MallException s) {

        log.error("【服务器抛出了异常】:", s);
        return ResultVoUtil.error(s.getCode(), s.getMessage());
    }

    @ExceptionHandler(UserLoginException.class)
    public ResultVo<String> handle(UserLoginException e) {

        log.error("【登录异常】：", e);
        return ResultVoUtil.error(e.getResultEnum());
    }


    @ExceptionHandler(UserRegisterException.class)
    public ResultVo<String> handle(UserRegisterException e) {

        log.error("【用户注册异常】：", e);
        return ResultVoUtil.error(e.getResultEnum());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizeException.class)
    public ResultVo<String> handle(AuthorizeException e) {

        log.error("【身份校验异常】：", e);
        return ResultVoUtil.error(ResultEnum.AUTHORIZE_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVo<String> notValidExceptionHandle(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();
        return ResultVoUtil.error(ResultEnum.PARAM_ERROR.getCode(),
                Objects.requireNonNull(bindingResult.getFieldError()).getField() + " " +
                        bindingResult.getFieldError().getDefaultMessage());
    }
}
