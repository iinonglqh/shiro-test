package com.example.shirotest.shiro.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: iinonglqh
 * @Date: 2022/6/23 11:23
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {

    private static String sing;

    private static Integer expire;

    private static Integer refreshTime;

    public void setSing(String sing) {
        JwtUtils.sing = sing;
    }

    public void setExpire(int expire) {
        JwtUtils.expire = expire;
    }

    public void setRefreshTime(int refreshTime) {
        JwtUtils.refreshTime = refreshTime;
    }

    public static Integer getRefreshTime() {
        return JwtUtils.refreshTime;
    }

    private static JwtUtils jwtUtils;

    @PostConstruct
    public void init() {
        jwtUtils = this;
    }

    public static String createToken(Map<String, String> map) {
        //token过期时间 分钟
        Date date = new Date(System.currentTimeMillis() + expire * 60 * 1000);

        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        //创建 Builder
        JWTCreator.Builder builder = JWT.create();
        //遍历map,设置token参数 私有声明
        map.forEach((key, value) -> {
            builder.withClaim(key, value);
        });

        String token = builder
                .withHeader(header)//jwt的header部分
                .withExpiresAt(date)//设置过期时间
                .withIssuedAt(new Date())//签发时间
                .sign(Algorithm.HMAC256(sing));//设置签名

        return token;
    }

    //校验token的有效性，1、token的header和payload是否没改过；2、没有过期
    public static boolean verify(String token) {
        try {
            //解密
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(sing)).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            //log.error(e.getMessage(),e);
            return false;
        }
    }

    //解析token，获取信息
    public static String getString(String token, String search) {
        try {
            if (StringUtils.isNotBlank(token)) {
                String data = JWT.decode(token).getClaim(search).asString();
                return data;
            }
        } catch (JWTDecodeException e) {

        }
        return null;
    }

    //获取 token 得 DecodedJWT
    public static DecodedJWT getTokenInfo(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(sing))
                .build()
                .verify(token);

        return verify;
    }

    //获取token的剩余过期时间 单位为豪秒
    public static long getRemainingTime(String token) {
        long result = 0;
        try {
            long nowMillis = System.currentTimeMillis();
            //剩余过期时间 = token的过期时间-当前时间
            result = getTokenInfo(token).getExpiresAt().getTime() - nowMillis;
        } catch (Exception e) {
            log.error("获取token的剩余过期时间错误", e.getMessage());
        }
        return result;
    }


}