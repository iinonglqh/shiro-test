package com.example.shirotest.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author iinonglqh
 * @since 2022-06-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户账号
     */
    @TableField("username")
    private String username;

    /**
     * 用户名称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 用户邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 用户头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 用户性别
     */
    @TableField("sex")
    private String sex;

    /**
     * 手机号码
     */
    @TableField("phonenumber")
    private String phonenumber;

    /**
     * 帐号状态
     */
    @TableField("status")
    private String status;

    /**
     * 最后登录时间
     */
    @TableField(value = "login_date", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime loginDate;

    /**
     * 角色组
     */
    @TableField(exist = false)
    private Long[] roleIds;

}
