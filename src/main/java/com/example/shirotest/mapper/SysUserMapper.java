package com.example.shirotest.mapper;

import com.example.shirotest.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author iinonglqh
 * @since 2022-06-23
 */
@Repository
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
