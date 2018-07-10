package com.dhht.service.employee;

import com.dhht.model.Count;

import java.util.List;

public interface EmployeeCountService {
    List<Count> countAllEmployee(String districtId,String startTime,String endTime);
}
