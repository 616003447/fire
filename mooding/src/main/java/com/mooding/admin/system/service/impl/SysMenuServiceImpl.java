package com.mooding.admin.system.service.impl;

import com.mooding.admin.system.entity.SysMenu;
import com.mooding.admin.system.mapper.SysMenuMapper;
import com.mooding.admin.system.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author mooding 
 * @since 2020-07-05
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    /**
     * 通过权限id 获取权限菜单集合
     * @param roleId
     * @return 权限菜单集合
     */
    public List<SysMenu> getMenuByRoleId (Long roleId){
        List<SysMenu> menuList=null;
        List<String> menuIdList=baseMapper.getMenuIdByRoleId(roleId);
        for  (String  id :menuIdList ){
            menuList.add(baseMapper.selectById(id));
        }
        return  menuList;
    }
    /**
     * 通过权限id 获取权限菜单id集合
     * @param roleId
     * @return 权限菜单id集合
     */
    public List<String> getMenuIdByRoleId (Long roleId){
        List<String> menuIdList=baseMapper.getMenuIdByRoleId(roleId);
        return  menuIdList;
    }
}
