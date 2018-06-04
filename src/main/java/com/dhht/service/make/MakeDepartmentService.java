package com.dhht.service.make;

import com.dhht.model.Makedepartment;
import com.dhht.model.UserDomain;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MakeDepartmentService {
     List<Makedepartment> findAllMake();

     int addMake(Makedepartment makedepartment);

     int deletemake(String code);

     int updatemake(Makedepartment makedepartment);

     PageInfo<Makedepartment> findAllMakeBySize(int pageNum, int pageSize);

     int validateUserAccout(String code);
}
