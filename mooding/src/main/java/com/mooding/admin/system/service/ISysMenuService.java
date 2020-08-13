package com.mooding.admin.system.service;

import com.mooding.admin.system.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author mooding 
 * @since 2020-07-05
 */
public interface ISysMenuService extends IService<SysMenu> {
    /**
     * 通过权限id 获取权限菜单集合
     * @param roleId
     * @return 权限菜单集合
     */
    public List<SysMenu> getMenuByRoleId (Long roleId);
    /**
     * 通过权限id 获取权限菜单id集合
     * @param roleId
     * @return 权限菜单id集合
     */
    public List<String> getMenuIdByRoleId (Long roleId);
}
