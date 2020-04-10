package com.homw.schedule.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.homw.schedule.entity.SysRoleEntity;
import com.homw.schedule.entity.SysUserEntity;
import com.homw.schedule.service.SysRoleService;
import com.homw.schedule.service.SysUserRoleService;
import com.homw.schedule.shiro.ShiroUtil;

/**
 * @description Controller公共组件
 * @author Hom
 * @version 1.0
 * @since 2020-04-01
 */
public abstract class AbstractController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected SysRoleService sysRoleService;
    @Autowired
    protected SysUserRoleService sysUserRoleService;

    protected SysUserEntity getUser() {
        return ShiroUtil.getUserEntity();
    }

    protected Long getUserId() {
        return getUser().getUserId();
    }

    /**
     * 获取当前用户下的角色列表
     */
    protected List<Long> getSubRoleList() {
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(getUserId());
        if (roleIdList != null && roleIdList.size() > 0) {
            Long roleId = roleIdList.get(0);
            roleIdList.clear();
            getSubRoleList(roleId, roleIdList);

            roleIdList.remove(roleId);
        }
        return roleIdList == null || roleIdList.isEmpty() ? null : roleIdList;
    }

    /**
     * 获取当前角色下的角色列表
     *
     * @param roleId
     * @param roleList
     */
    protected void getSubRoleList(Long roleId, List<Long> roleList) {
        SysRoleEntity parent = sysRoleService.queryObject(roleId);
        if (parent != null) {
            roleList.add(parent.getRoleId());

            Map<String, Object> map = new HashMap<>();
            map.put("parentId", parent.getRoleId());
            List<SysRoleEntity> children = sysRoleService.queryList(map);
            if (children != null) {
                for (SysRoleEntity child : children) {
                    getSubRoleList(child.getRoleId(), roleList);
                }
            }
        }
    }
}
