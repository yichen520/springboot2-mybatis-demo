package com.dhht.service.make;

import com.dhht.model.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MakeDepartmentService {
     List<MakeDepartmentSimple> selectInfo(String districtId,String name,String status);

     Makedepartment selectDetailById(String id);

     int insert(Makedepartment makedepartment);

     int update(Makedepartment makedepartment);

     int deleteById(String id);

     List<Makedepartment> selectHistory(String flag);

     MakeDepartmentSimple selectByLegalTephone(String phone);

     MakeDepartmentSimple selectByDepartmentCode(String code);

     String selectCodeByLegalTelphone(String phone);




}
