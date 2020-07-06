package com.mooding.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mooding.admin.system.entity.SysUser;
import com.mooding.admin.system.mapper.SysUserMapper;
import com.mooding.admin.system.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author mooding 
 * @since 2020-07-04
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    /**
     *
     * @param username
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String username) {
        return baseMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, username));
    }
}
