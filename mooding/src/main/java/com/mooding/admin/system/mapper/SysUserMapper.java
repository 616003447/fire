package com.mooding.admin.system.mapper;

import com.mooding.admin.system.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息表 Mapper 接口
 *
 * @author mooding 
 * @since 2020-07-04
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
