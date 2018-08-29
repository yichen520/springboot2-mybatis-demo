package com.dhht.service.user.impl;

import com.dhht.common.AccessResult;
import com.dhht.dao.RoleDao;
import com.dhht.dao.RoleResourceDao;
import com.dhht.dao.UserDao;
import com.dhht.model.*;
import com.dhht.service.user.RoleService;
import com.dhht.service.user.UserService;
import com.dhht.util.DaoUtil;
import com.dhht.util.StringUtil;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Transactional
@Service(value = "roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleResourceDao roleResourceDao;

    @Autowired
    private UserDao userDao;


    @Override
    @Transactional
    public AccessResult save(Role role){
        List<String> resourcesIds = role.getResourceIds();
        if (resourcesIds == null){
            return new AccessResult(-1,"请选择对应的权限");
        }
        if (role.getId() == null){
            return new AccessResult(-1,"角色代码不能为空");
        }
        //保存角色表
        roleDao.insertSelective(role);
        //保存到角色资源关联表
//        List<String> rStrs = DaoUtil.parseJsonStrToList(resourcesIds);
        for(String resourcesId:resourcesIds){
            RoleResourceKey sysRoleResource = new RoleResourceKey();
            sysRoleResource.setResourceId(resourcesId); //资源Id
            sysRoleResource.setRoleId(role.getId());
            roleResourceDao.insertSelective(sysRoleResource);
        }
        return new AccessResult(1,"新增角色成功");
    }

    @Override
    //@Transactional
    public AccessResult updataRole(Role role){
        List<String> resourcesIds = role.getResourceIds();
        //先删除角色资源关联表
        roleResourceDao.deleteRole(role.getId());
        //增加新的角色资源关联表
     //   List<String> rStrs = DaoUtil.parseJsonStrToList(resourcesIds);
        for(String resourcesId:resourcesIds){
            RoleResourceKey sysRoleResource = new RoleResourceKey();
            sysRoleResource.setResourceId(resourcesId); //资源Id
            sysRoleResource.setRoleId(role.getId());
            roleResourceDao.insert(sysRoleResource);
        }
        roleDao.updateByPrimaryKeySelective(role);
        return new AccessResult(1,"修改角色成功");
    }

    @Override
    @Transactional
    public PageInfo<Role> getRoleList(int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);

        PageInfo result = new PageInfo(getRoles());
        return result;
    }

    @Override
    @Transactional
    public int deleteRole(String id){
        //删除角色对应的资源信息
        roleResourceDao.deleteRole(id);
        //删除角色
        roleDao.deleteByPrimaryKey(id);
        return 1;
    }

    @Override
    public Role findRoleById(String id){
       return roleDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Role> getRoleListNopage() {
       return getRoles();
    }

    /**
     * 获取角色下的用户
     * @return
     */
    @Override
    public List<Role> getRoleUser(String district) {
       String districtId = StringUtil.getDistrictId(district);
       List<Role> roles = roleDao.findAllRole();
       List<Role> roleList = new ArrayList<>();
           for (Role role : roles) {
               List<UserSimple> list = userDao.getRoleUser(districtId, role.getId());
               if(list.size()>0) {
                   role.setRoleUser(list);
                   roleList.add(role);
               }
           }
        return roleList;
    }

    //获取所有资源方法
     public List<Role> getRoles(){
        List<Role> roles = roleDao.findAllRole();
        for(Role sysRole:roles){
            List<String> resourceIds = new ArrayList<String>();

            List<RoleResourceKey> sysRoleResources = roleResourceDao.selectByRoleID(sysRole.getId());
            for(RoleResourceKey sysRoleResource:sysRoleResources){
                resourceIds.add(sysRoleResource.getResourceId());
            }
            sysRole.setResourceIds(resourceIds);
        }
        return roles;
    }

}
