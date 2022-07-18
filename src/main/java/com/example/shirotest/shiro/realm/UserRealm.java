package com.example.shirotest.shiro.realm;

import com.example.shirotest.entity.SysUser;
import com.example.shirotest.service.SysMenuService;
import com.example.shirotest.service.SysRoleService;
import com.example.shirotest.service.SysUserService;
import com.example.shirotest.shiro.utils.JwtUtils;
import com.example.shirotest.shiro.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description:
 * @Author: iinonglqh
 * @Date: 2022/6/23 11:34
 */
@Slf4j
@Component
public class UserRealm extends AuthorizingRealm {


    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    RedisUtils redisUtils;

    /**
     * 多重写一个support
     * 标识这个Realm是专门用来验证JwtToken
     * 不负责验证其他的token（UsernamePasswordToken）
     *
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        //这个token就是从过滤器中传入的jwtToken
        return token instanceof JwtToken;
    }

    /**
     * 授权
     * 只有当需要检测用户权限的时候才会调用此方法，例如@RequiresRoles,@RequiresPermissions之类的
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("doGetAuthorizationInfo 方法被调用");
        String id = JwtUtils.getString(principalCollection.toString(), "id");
        SysUser sysUser = sysUserService.getById(id);

        // 角色列表
        Set<String> roles = new HashSet<String>();
        // 菜单列表
        Set<String> menus = new HashSet<String>();

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 管理员拥有所有权限
        if (sysUser.getId() != null && 1L == sysUser.getId()) {
            info.addRole("admin");
            info.addStringPermission("*:*:*");
        } else {
            roles = sysRoleService.selectRolesByUserId(sysUser.getId());
            menus = sysMenuService.selectPermsByUserId(sysUser.getId());
            // 角色加入AuthorizationInfo认证对象
            info.setRoles(roles);
            // 权限加入AuthorizationInfo认证对象
            info.setStringPermissions(menus);
        }
        return info;
    }

    /**
     * 认证
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可，在需要用户认证和鉴权的时候才会调用
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("doGetAuthenticationInfo 方法被调用");
        String token = (String) authenticationToken.getCredentials();
        // 验证用户是否存在
        String id = JwtUtils.getString(token, "id");
        SysUser sysUser = sysUserService.getById(id);
        // 判断 token 是否存在黑名单中，如果存在则为无效 token，需要重新登录
        boolean key = redisUtils.hasKey(token);
        if (!JwtUtils.verify(token) || key) {
            throw new AuthenticationException("token认证失败，请重新登录!");
        }
        if (sysUser == null) {
            throw new AuthenticationException("用户不存在!");
        }
        return new SimpleAuthenticationInfo(token, token, "JwtRealm");
    }
}
