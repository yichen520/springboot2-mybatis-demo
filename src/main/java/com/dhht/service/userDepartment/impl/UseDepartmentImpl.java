package com.dhht.service.userDepartment.impl;

import com.dhht.dao.UseDepartmentDao;
import com.dhht.dao.UserDao;
import com.dhht.model.UseDepartment;
import com.dhht.service.userDepartment.UseDepartmentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by imac_dhht on 2018/6/12.
 */
@Service(value = "useDepartmentService")
@Transactional
public class UseDepartmentImpl implements UseDepartmentService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UseDepartmentDao useDepartmentDao;




    /**
     * 查询全部
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<UseDepartment> findAllMakeBySize(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UseDepartment> useDepartments = useDepartmentDao.findAllMake();
        PageInfo result = new PageInfo(useDepartments);
        return result;
    }


}
