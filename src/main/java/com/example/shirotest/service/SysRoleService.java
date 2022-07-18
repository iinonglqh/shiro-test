package com.example.shirotest.service;

import com.example.shirotest.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shirotest.entity.SysUser;

import java.util.Set;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author iinonglqh
 * @since 2022-07-06
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> selectRolesByUserId(Long userId);


    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    Set<String> getRolePermission(SysUser user);

}
