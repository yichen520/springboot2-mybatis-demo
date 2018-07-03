package com.dhht.service.recordDepartment;

import com.dhht.model.RecordDepartment;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RecordDepartmentService {
    PageInfo<RecordDepartment> selectByDistrictId(String Id,int pageSize,int pageNum);

    List<RecordDepartment> selectByDistrictId(String Id);

    PageInfo<RecordDepartment> selectAllRecordDepartMent(int pageSize,int pageNum);

    Boolean insert(RecordDepartment recordDepartment);

    RecordDepartment selectByCode(String code);

    boolean deleteById(String id);

    boolean updateById(RecordDepartment recordDepartment);

    List<RecordDepartment> showMore(String flag);



}
