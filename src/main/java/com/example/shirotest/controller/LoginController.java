package com.example.shirotest.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.shirotest.entity.SysUser;
import com.example.shirotest.service.SysUserService;
import com.example.shirotest.shiro.utils.JwtUtils;
import com.example.shirotest.shiro.utils.MD5Utils;
import com.example.shirotest.shiro.utils.RedisUtils;
import com.example.shirotest.shiro.utils.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: iinonglqh
 * @Date: 2022/7/6 9:56
 */
@RestController
public class LoginController {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    RedisUtils redisUtils;


    @PostMapping("/login")
    public Result login(@RequestParam String username, @RequestParam String password) {
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {
            return Result.fail(10001, "账号或密码不能为空");
        }
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", MD5Utils.MD5(password));
        SysUser user = sysUserService.getOne(queryWrapper);

        if (user == null) {
            return Result.fail(10002, "账号或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("id", String.valueOf(user.getId()));
        String token = JwtUtils.createToken(tokenMap);

        return Result.success(200, "登陆成功", token);
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        // 关于 Jwt 注销和续签的解决方案 https://blog.csdn.net/qq_42764468/article/details/107731844
        String token = request.getHeader("Authorization");
        if (StrUtil.isEmpty(token) || !JwtUtils.verify(token)) {
            return Result.fail(10003, "该账户已经自动退出");
        }
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            //退出登录
            subject.logout();
        }
        //把 Token 加入黑名单,设置 redis 的过期时间和 token 的剩余过期时间相同
        String id = JwtUtils.getString(token, "id");
        long remainingTime = JwtUtils.getRemainingTime(token);
        // 加多一分钟避免计算带来的误差
        redisUtils.set(token, id, (remainingTime / 1000) + 60);
        return Result.success(200, "退出登录成功", null);
    }

    @GetMapping("/unauthorized/{message}")
    public Result unauthorized(@PathVariable String message) throws UnsupportedEncodingException {
        String decode = URLDecoder.decode(message, "UTF-8");
        return Result.fail(10000, decode);
    }

    @GetMapping("/unauthorized")
    public Result unauthorized2(@RequestParam String message) {
        return Result.fail(10000, message);
    }

}
