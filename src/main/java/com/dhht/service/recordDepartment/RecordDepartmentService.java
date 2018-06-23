package com.dhht.service.recordDepartment;

import com.dhht.model.RecordDepartment;

import java.util.List;

public interface RecordDepartmentService {
    List<RecordDepartment> selectByDistrictId(String Id );
}
