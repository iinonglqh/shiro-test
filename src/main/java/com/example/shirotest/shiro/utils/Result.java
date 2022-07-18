package com.example.shirotest.shiro.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: iinonglqh
 * @Date: 2022/7/6 9:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private Integer code;

    private String message;

    private Object data;

    public static Result success(Integer code, String message, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static Result fail(Integer code, String message) {
        Result r = new Result();
        r.setCode(code);
        r.setMessage(message);
        r.setData(null);
        return r;
    }
}
