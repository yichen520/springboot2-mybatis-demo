package com.dhht.service.seal;

import com.dhht.model.Count;
import com.dhht.model.SealCount;

import java.util.List;

public interface SealCuontService {
     
    //按照刻制企业
    List<SealCount> countByDepartment(List<String> makeDepartmentCodes, String districtId, List<String> sealTypeCodes, List<String> Status, String startTime, String endTime);

    //按照区域
    List<SealCount> countByDistrictId(List<String> districtIds, List<String> sealTypeCodes, List<String> Status, String startTime, String endTime);
}
