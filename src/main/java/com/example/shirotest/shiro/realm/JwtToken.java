package com.example.shirotest.shiro.realm;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Description: JwtToken代替原生的UsernamePasswordToken
 * @Author: iinonglqh
 * @Date: 2022/6/23 15:22
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    //返回原来的字符串，解析交给JwtUtils实现
    @Override
    public Object getPrincipal() {
        return token;
    }

    //返回原来的字符串，解析交给JwtUtils实现
    @Override
    public Object getCredentials() {
        return token;
    }
}
