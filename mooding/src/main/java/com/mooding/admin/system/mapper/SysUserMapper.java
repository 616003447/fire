package com.mooding.admin.system.mapper;

import com.mooding.admin.system.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 用户信息表 Mapper 接口
 *
 * @author mooding 
 * @since 2020-07-04
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("select role_id from t_sys_user_role where user_id =#{userId} ")
    public List<String> getRoleIdByUserId(@Param("userId") Long userId);
}
