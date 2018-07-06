package com.dhht.service.make;

import com.dhht.model.MakeDepartmentSimple;
import com.dhht.model.Makedepartment;
import com.dhht.model.RecordDepartment;
import com.dhht.model.UserDomain;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MakeDepartmentService {
     PageInfo<MakeDepartmentSimple> selectByDistrictId(String districtId, int pageNum, int pageSize);

     Makedepartment selectDetailById(String id);

     int insert(Makedepartment makedepartment);

     int update(Makedepartment makedepartment);

     int deleteById(String id);

     PageInfo<Makedepartment> selectHistory(String flag,int pageNum,int pageSize);

     MakeDepartmentSimple selectByLegalTephone(String phone);

     MakeDepartmentSimple selectByDepartmentCode(String code);

     String selectCodeByLegalTelphone(String phone);

}
