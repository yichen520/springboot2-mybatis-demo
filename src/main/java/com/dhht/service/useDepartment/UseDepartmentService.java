package com.dhht.service.useDepartment;

import com.dhht.common.JsonObjectBO;

import com.dhht.model.UseDepartment;
import com.dhht.model.User;
import com.dhht.model.WeChatUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Created by imac_dhht on 2018/6/12.
 */
public interface UseDepartmentService {

    JsonObjectBO insert(UseDepartment useDepartment,User updateUser);

    JsonObjectBO update(UseDepartment useDepartment,User updateUser);

    JsonObjectBO find(String localDistrictId,String code,String name,String districtId,String departmentStatus,int pageNum, int pageSize);

    JsonObjectBO delete(UseDepartment useDepartment,User updateUser);

    JsonObjectBO showHistory(String flag);

    UseDepartment selectDetailById(String id);

    List<UseDepartment> selectUseDepartment(String useDepartmentName);

    UseDepartment selectByCode(String useDepartmentCode);

    UseDepartment selectActiveUseDepartmentByCode(String socialCode);

    List<UseDepartment> selectByNameAndCode(String code);

    int binding(String id, WeChatUser weChatUser);

    int relieveBinding(WeChatUser weChatUser);

    UseDepartment selectByFlag(String flag);


}
