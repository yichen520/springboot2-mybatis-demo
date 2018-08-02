package com.dhht.service.seal;

import com.dhht.model.Count;
import com.dhht.model.SealCount;
import com.dhht.model.User;

import java.util.List;

public interface SealCuontService {
     
    //按照刻制企业
    List<SealCount> countByDepartment(User user,List<String>  districtIds, List<String> sealTypeCodes, String startTime, String endTime);

    //按照区域
    List<SealCount> countByDistrictId(User user, List<String> districts, List<String> sealTypeCodes, String startTime, String endTime);
}
