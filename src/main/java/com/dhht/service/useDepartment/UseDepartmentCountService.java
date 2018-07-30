package com.dhht.service.useDepartment;

import com.dhht.model.Count;

import java.util.List;

public interface UseDepartmentCountService {
    List<Count> countAllUseDepartment(String districtId, String startTime, String endTime);
}
