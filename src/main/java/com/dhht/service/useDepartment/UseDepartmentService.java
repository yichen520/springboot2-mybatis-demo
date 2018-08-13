package com.dhht.service.useDepartment;

import com.dhht.common.JsonObjectBO;

import com.dhht.model.UseDepartment;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Created by imac_dhht on 2018/6/12.
 */
public interface UseDepartmentService {

    JsonObjectBO insert(UseDepartment useDepartment,HttpServletRequest httpServletRequest);

    JsonObjectBO update(UseDepartment useDepartment,HttpServletRequest httpServletRequest);

    JsonObjectBO find(String localDistrictId,String code,String name,String districtId,String departmentStatus,int pageNum, int pageSize);

    JsonObjectBO delete(UseDepartment useDepartment);

    JsonObjectBO showHistory(String flag);

    UseDepartment selectDetailById(String id);

    List<UseDepartment> selectUseDepartment(String useDepartmentName);
}
