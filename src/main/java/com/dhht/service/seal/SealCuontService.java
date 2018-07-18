package com.dhht.service.seal;

import com.dhht.model.Count;
import com.dhht.model.SealCount;

import java.util.List;

public interface SealCuontService {
    //
    List<SealCount> countByDepartment(String makeDepartmentCode,String districtId, String startTime, String endTime);
}
