package com.mooding.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mooding.admin.common.model.AppHttpCodeEnum;
import com.mooding.admin.common.model.ResponseResult;
import com.mooding.admin.common.contants.SystemContans;
import com.mooding.admin.system.entity.SysMenu;
import com.mooding.admin.system.entity.SysUser;
import com.mooding.admin.system.mapper.SysUserMapper;
import com.mooding.admin.system.service.ISysMenuService;
import com.mooding.admin.system.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author mooding 
 * @since 2020-07-04
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Autowired
    public ISysMenuService menuService;
    /**
     *通过用户对象包含用户名电话号码，确定唯一用户
     * @param "SysUser"
     * @return 用户对象信息
     */
    @Override
    public SysUser getOneUserByUser(SysUser user) {
        if (StringUtils.isBlank(user.getUserName())&&StringUtils.isBlank(user.getPhonenumber())){
            return null;
        }
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>();
        //用户名称
        if (StringUtils.isNotBlank(user.getUserName()))
            queryWrapper.eq(SysUser::getUserName, user.getUserName().trim());
        //电话号码
        if (StringUtils.isNotBlank(user.getPhonenumber()))
            queryWrapper.eq(SysUser::getPhonenumber, user.getPhonenumber().trim());
        //选择未被删除用户
        queryWrapper.eq(SysUser::getDelFlag, SystemContans.USER_UNDEL_FLAG);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 通过用户id 查询用户
     *
     * @param userId 用户id
     * @return 用户对象信息
     */
    public SysUser getUserById(Long userId){
       return baseMapper.selectById(userId);
    }
    /**
     * 校验用户是否有效
     * @param sysUser
     * @return
     */
    @Override
    public ResponseResult checkUserIsEffective(SysUser sysUser) {
        if (sysUser == null) {
            ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_NO_USER_ERROR);
        }
        //情况2：根据用户信息查询，该用户已注销
        if (SystemContans.USER_DEL_FLAG.toString().equals(sysUser.getDelFlag())) {
            return  ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_USER_DEL_FLAG_ERROR);
        }
        //情况3：根据用户信息查询，该用户已冻结
        if (SystemContans.USER_FREEZE.equals(sysUser.getStatus())) {
            /*sysBaseAPI.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已冻结！", CommonConstant.LOG_TYPE_1, null);
            result.error500("该用户已冻结");
            return result;*/
            return  ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_USER_FREEZE_ERROR);
        }
        return null;
    }
    /**
     * 通过用户名获取用户角色集合
     * @param username 用户名
     * @return 角色集合
     */
    @Override
    public Set<String> getUserRolesSet(String username) {
        // 查询用户拥有的角色集合
        //1.查询用户信息
        SysUser user =getOneUserByUser(new SysUser().setUserName(username));
        //2.查询权限信息
        List<String> roles = baseMapper.getRoleIdByUserId(user.getUserId());
        log.info("-------通过数据库读取用户拥有的角色Rules------username： " + username + ",Roles size: " + (roles == null ? 0 : roles.size()));
        return new HashSet<>(roles);
    }
    /**
     * 通过用户名获取用户权限集合
     *
     * @param username 用户名
     * @return 权限集合
     */
    @Override
   public Set<SysMenu> getUserMenuSet(String username){
        List<SysMenu> menuList=null;
        //1.查询用户信息
        SysUser user =getOneUserByUser(new SysUser().setUserName(username));
        //2.查询权限信息
        List<String> roles = baseMapper.getRoleIdByUserId(user.getUserId());
        //3.查询权限下单菜单
        for(String  roleId: roles){
            menuList.addAll( menuService.getMenuByRoleId(Long.parseLong(roleId)));
        }
        return new HashSet<>(menuList);
   }

}
