package com.dhht.service.userDepartment.impl;

import com.dhht.dao.UserDao;
import com.dhht.dao.UserDepartmentDao;
import com.dhht.model.Makedepartment;
import com.dhht.model.UserDepartment;
import com.dhht.model.Users;
import com.dhht.service.userDepartment.UserDepartmentService;
import com.dhht.util.MD5Util;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by imac_dhht on 2018/6/12.
 */
@Service(value = "userDepartmentService")
@Transactional
public class UserDepartmentImpl implements UserDepartmentService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserDepartmentDao userDepartmentDao;

    @Override
    public int insert(UserDepartment userDepartment) {
        String code = UUIDUtil.generate();
        userDepartment.setUserdepartmentCode(UUIDUtil.generate());
        Users users = new Users();
        users.setUserName(code);
        users.setRealName(userDepartment.getDepartmentName());
        users.setPassword(MD5Util.toMd5("123456"));//设置默认密码
        users.setRegionId(userDepartment.getDistrictId());
        userDao.addUser(users);
        return userDepartmentDao.insert(userDepartment);
    }

    @Override
    public int update(UserDepartment userDepartment) {
        Users users = new Users();
        users.setUserName(userDepartment.getDepartmentName());
        users.setIsDeleted(userDepartment.getIsDelete());
        users.setRegionId(userDepartment.getDistrictId());
        userDao.updateUserDepartment(users);
        return userDepartmentDao.updateByPrimaryKey(userDepartment);
    }

    @Override
    public int delete(String userdepartmentCode) {
        return userDepartmentDao.deleteByPrimaryKey(userdepartmentCode);
    }

    /**
     * 查询全部
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<UserDepartment> findAllMakeBySize(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserDepartment> userDepartments = userDepartmentDao.findAllMake();
        PageInfo result = new PageInfo(userDepartments);
        return result;
    }


}
