package com.dhht.service.userDepartment;

import com.dhht.model.UseDepartment;
import com.github.pagehelper.PageInfo;


/**
 * Created by imac_dhht on 2018/6/12.
 */
public interface UseDepartmentService {


    PageInfo<UseDepartment> findAllMakeBySize(int pageNum, int pageSize);
}
