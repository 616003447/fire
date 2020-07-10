package com.mooding.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mooding.admin.common.config.model.AppHttpCodeEnum;
import com.mooding.admin.common.config.model.ResponseResult;
import com.mooding.admin.common.contants.SystemContans;
import com.mooding.admin.system.entity.SysUser;
import com.mooding.admin.system.mapper.SysUserMapper;
import com.mooding.admin.system.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
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
     *通过用户对象包含用户名电话号码，确定唯一用户
     * @param SysUser
     * @return 用户对象信息
     */
    @Override
    public SysUser getOneUserByUser(SysUser user) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>();
        //用户名称
        if (StringUtils.isNotBlank(user.getUserName().trim()))
            queryWrapper.eq(SysUser::getUserName, user.getUserName().trim());
        //电话号码
        if (StringUtils.isNotBlank(user.getPhonenumber().trim()))
            queryWrapper.eq(SysUser::getPhonenumber, user.getPhonenumber().trim());
        //选择未被删除用户
        queryWrapper.eq(SysUser::getDelFlag, SystemContans.USER_UNDEL_FLAG);
        return baseMapper.selectOne(queryWrapper);
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


}
