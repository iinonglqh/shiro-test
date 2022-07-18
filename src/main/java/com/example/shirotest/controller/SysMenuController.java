package com.example.shirotest.controller;


import com.example.shirotest.entity.SysMenu;
import com.example.shirotest.entity.SysUser;
import com.example.shirotest.service.SysMenuService;
import com.example.shirotest.shiro.utils.JwtUtils;
import com.example.shirotest.shiro.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 菜单权限表 前端控制器
 * </p>
 *
 * @author iinonglqh
 * @since 2022-07-06
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    SysMenuService sysMenuService;

    /**
     * 获取路由菜单信息
     * @param request
     * @return
     */
    @GetMapping("getRouters")
    public Result getRouters(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String id = JwtUtils.getString(token, "id");
        List<SysMenu> menus = sysMenuService.selectMenuTreeByUserId(Long.valueOf(id));
        return Result.success(200,"获取路由信息成功",menus);
    }

}
