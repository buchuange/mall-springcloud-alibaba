package com.dawn.mall.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Dawn
 * @Date: 2022/5/11 19:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ResultVo<T> implements Serializable {

    private static final long serialVersionUID = 8852458120907810130L;

    private Integer status;

    private String msg;

    private T data;

    public ResultVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ResultVo(Integer status, T data) {
        this.status = status;
        this.data = data;
    }
}
