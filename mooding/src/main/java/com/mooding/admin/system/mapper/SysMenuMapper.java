package com.mooding.admin.system.mapper;

import com.mooding.admin.system.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author mooding 
 * @since 2020-07-05
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    //通过角色id获取菜单id
    @Select("select menu_id from t_sys_role_menu where role_id =#{roleId} ")
    public List<String> getMenuIdByRoleId(@Param("roleId") Long roleId);

}
