package com.dhht.service.recordDepartment.Impl;

import com.dhht.dao.RecordDepartmentMapper;
import com.dhht.model.RecordDepartment;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "RecordDepartmentService")
public class RecordDepartmentServiceImp implements RecordDepartmentService{
    @Autowired
    private RecordDepartmentMapper recordDepartmentMapper;
    @Override
    public List<RecordDepartment> selectByDistrictId(String Id) {
        return recordDepartmentMapper.selectByDistrictId(Id);
    }
}
