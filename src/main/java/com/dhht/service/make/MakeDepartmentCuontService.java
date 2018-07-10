package com.dhht.service.make;

import com.dhht.model.Count;

import java.util.List;

public interface MakeDepartmentCuontService {
    //
    List<Count> countAllDepartment(String districtId, String startTime, String endTime);
}
