package com.fruitshop.vo;

import lombok.Data;

/**
 * 登录返回视图对象
 */
@Data
public class LoginVO {

    /**
     * 令牌
     */
    private String token;

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String name;
}
