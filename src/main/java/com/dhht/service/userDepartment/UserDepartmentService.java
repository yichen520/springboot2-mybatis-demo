package com.dhht.service.userDepartment;

import com.dhht.model.Makedepartment;
import com.dhht.model.UserDepartment;
import com.github.pagehelper.PageInfo;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;

import java.util.List;

/**
 * Created by imac_dhht on 2018/6/12.
 */
public interface UserDepartmentService {

    int insert(UserDepartment userDepartment);

    int update(UserDepartment userDepartment);

    int delete(String userdepartmentCode);

    PageInfo<UserDepartment> findAllMakeBySize(int pageNum, int pageSize);
}
