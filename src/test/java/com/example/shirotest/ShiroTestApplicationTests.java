package com.example.shirotest;

import com.example.shirotest.mapper.SysUserMapper;
import com.example.shirotest.service.SysMenuService;
import com.example.shirotest.service.SysRoleService;
import com.example.shirotest.service.SysUserService;
import com.example.shirotest.shiro.utils.JwtUtils;
import com.example.shirotest.shiro.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@SpringBootTest
class ShiroTestApplicationTests {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisUtils redisUtils;


    private static final long EXPIRE = 30 * 60 * 1000L;


    @Test
    void contextLoads() {
//        List<SysUser> list = sysUserService.list();
//        SysUser sysUser = sysUserMapper.selectById(1);
//        System.out.println(sysUser);

//        Map<String, String> map = new HashMap<>();
//        map.put("id", "1");
//        String token = JwtUtils.createToken(map);
//        boolean verify = JwtUtils.verify(token);
//        String id = JwtUtils.getString(token, "id");
//        DecodedJWT tokenInfo = JwtUtils.getTokenInfo(token);
//        System.out.println(tokenInfo.getHeader());
//        System.out.println(tokenInfo.getPayload());
//        System.out.println(tokenInfo.getSignature());
//        System.out.println(tokenInfo.getToken());

//        List<String> list = Arrays.asList("One",
//                "Two",
//                "Three",
//                "Four",
//                "Five");
//        System.out.println(list.toString());

//        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
//        queryWrapper.eq("username","admin");
//        queryWrapper.eq("password", MD5Utils.MD5("admin"));
//        SysUser user = sysUserService.getOne(queryWrapper);
//        System.out.println(user);

//        Set<String> strings = sysRoleService.selectRoleKeys(2L);
//        System.out.println(strings);
//        Set<String> strings1 = sysMenuService.selectPermsByUserId(2L);
//        System.out.println(strings1);

//        SysUser sysUserServiceById = sysUserService.getById(2L);
//        System.out.println(sysUserServiceById);

//        String token = "aaeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJleHAiOjE2NTcxNzkwNjEsImlhdCI6MTY1NzE3NzI2MX0.c6IzfYxEgSWtzsxJ4tTYjl88rB8WuCsREROHW6F1Beo";
//        boolean verify = JwtUtils.verify(token);
//        System.out.println(verify);

//        try {
//            String a = URLEncoder.encode("token认证","UTF-8");
//            System.out.println(a);
//            String str = URLDecoder.decode(a, "UTF-8");
//            System.out.println(str);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

//        redisTemplate.opsForValue().set("key111", "value222");
//        Object o = redisTemplate.opsForValue().get("key111");
//        System.out.println(redisTemplate);

//        Object key111 = redisUtils.get("key111");
//        System.out.println(key111);

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJleHAiOjE2NTczNDkwMzQsImlhdCI6MTY1NzI2MjYzNH0.0yVckr7bqYuGlo0gnN85y6s-bWoj0DxPAZ6ZqHPQ2Mk";
        long remainingTime = JwtUtils.getRemainingTime(token);
        System.out.println(remainingTime / 1000);
        System.out.println(redisUtils.getExpire(token));
    }

}
