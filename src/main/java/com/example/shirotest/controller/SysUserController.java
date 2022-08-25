package com.example.shirotest.controller;


import com.example.shirotest.entity.SysUser;
import com.example.shirotest.service.SysMenuService;
import com.example.shirotest.service.SysRoleService;
import com.example.shirotest.service.SysUserService;
import com.example.shirotest.shiro.utils.JwtUtils;
import com.example.shirotest.shiro.utils.Result;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author iinonglqh
 * @since 2022-06-23
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    /**
     * 各个注解的作用说明
     *
     * @RequiresAuthentication 用户登录了之后才可以访问这个接口 不鉴权
     * @RequiresRoles 拥有这个角色的用户才可以访问这个接口 鉴权
     * @RequiresPermissions 拥有这个权限的用户才可以访问这个接口 鉴权
     */

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysMenuService sysMenuService;


    @RequiresAuthentication
    @RequestMapping("/test")
    public Result test() {
        return Result.success(200, "test", null);
    }

    /**
     * 不需要权限访问
     *
     * @return
     */
    @GetMapping("/list")
    public Result list() {
        return Result.success(200, "list success", null);
    }

    /**
     * 菜单权限 system:user:list
     */
    @GetMapping("/user")
    @RequiresPermissions("system:user:list")
    public Result userlist() {
        return Result.success(200, "user list success", null);
    }


    /**
     * 角色权限 admin
     */
    @GetMapping("/role")
    @RequiresRoles("admin")
    public Result rolelist() {
        return Result.success(200, "role list success", null);
    }


    @GetMapping("/getVipMessage")
    @RequiresRoles(logical = Logical.OR, value = {"admin", "common"})
    @RequiresPermissions("system:role:list")
    public Result getVipMessage() {
        return Result.success(200, "成功获取VIP信息", null);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public Result getInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String id = JwtUtils.getString(token, "id");
        SysUser user = sysUserService.getById(id);
        // 角色集合
        Set<String> roles = sysRoleService.getRolePermission(user);
        // 权限集合
        Set<String> menus = sysMenuService.getMenuPermission(user);
        Map map = new HashMap();
        map.put("user", user);
        map.put("roles", roles);
        map.put("menus", menus);
        return Result.success(200,"获取用户信息成功",map);
    }

}
