package com.dhht.service.user;

import com.dhht.common.AccessResult;
import com.dhht.model.Makedepartment;
import com.dhht.model.Role;
import com.github.pagehelper.PageInfo;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface RoleService {

    AccessResult save(Role role);

    AccessResult updataRole(Role role);

    PageInfo<Role> getRoleList(int pageNum, int pageSize);

    int deleteRole(String id);

    Role findRoleById(String id);
}
