package com.dhht.service.make;

import com.dhht.model.*;
import com.dhht.model.pojo.CommonHistoryVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MakeDepartmentService {
     List<MakeDepartmentSimple> selectInfo(String districtId,String name,String status);

     Makedepartment selectDetailById(String id);

     int insert(Makedepartment makedepartment,User user);

     int update(Makedepartment makedepartment,User user);

     int deleteById(String id,User user);

     List<Makedepartment> selectHistory(String flag);

     MakeDepartmentSimple selectByLegalTephone(String phone);

     MakeDepartmentSimple selectByDepartmentCode(String code);

     String selectCodeByLegalTelphone(String phone);

     List<Makedepartment> selectPunish(String MakeDepartmentName,String startTime,String endTime,String districtId,String localDistrictId);

     List<ExamineRecordDetail> selectExamineDetailByID(String id);

     List<OperatorRecord> showUpdteHistory(String flag);
}
