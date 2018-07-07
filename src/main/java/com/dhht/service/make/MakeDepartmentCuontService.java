package com.dhht.service.make;

import com.dhht.model.District;
import com.dhht.model.DistrictMenus;
import com.dhht.model.MakedepartmentCount;

import java.util.List;

public interface MakeDepartmentCuontService {
    //
    List<MakedepartmentCount> countAllDepartment(String districtId,String startTime,String endTime);
}
