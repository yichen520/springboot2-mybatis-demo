package com.dhht.service.make;

import com.dhht.model.*;
import com.dhht.model.pojo.CommonHistoryVO;
import com.dhht.model.pojo.SealDTO;
import com.dhht.model.pojo.SealVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface MakeDepartmentService {
     List<MakeDepartmentSimple> selectInfo(String districtId,String name,String status);

     List<MakeDepartmentSimple> selectAllInfo(String districtId);

     Makedepartment selectDetailById(String id);

     int insert(Makedepartment makedepartment,User user);

     int update(Makedepartment makedepartment,User user);

     int deleteById(String id,User user);

     List<Seal> selectSeal(User user);

     SealVO sealDetails(String id);

     List<Makedepartment> selectHistory(String flag);

     MakeDepartmentSimple selectByLegalTephone(String phone);

     MakeDepartmentSimple selectByDepartmentCode(String code);


     String selectCodeByLegalTelphone(String phone);

     List<Makedepartment> selectPunish(String MakeDepartmentName,String startTime,String endTime,String districtId,String localDistrictId);

     List<ExamineRecordDetail> selectExamineDetailByID(String id);

     Makedepartment selectByCode(String departmentCode);

     String makeDepartmentCode(String name);

     List<MakeDepartmentSimple> selectSimpleByDepartmentName(String districtId,String departmentName);

    List<Makedepartment> makeDepartmentSort(Map map);
}
