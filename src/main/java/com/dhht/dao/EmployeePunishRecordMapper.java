package com.dhht.dao;

import com.dhht.model.EmployeePunishRecord;
import com.dhht.model.ExamineCount;
import com.dhht.model.MakePunishRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeePunishRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(EmployeePunishRecord record);

    int insertSelective(EmployeePunishRecord record);

    EmployeePunishRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(EmployeePunishRecord record);

    int updateByPrimaryKey(EmployeePunishRecord record);

    List<EmployeePunishRecord> findPunish(@Param("makedepartmentName") String makedepartmentName, @Param("startTime") String startTime, @Param("endTime")String endTime, @Param("district")String districtId);

    ExamineCount selectPunishCountByDistrict(@Param("district")String district, @Param("month")String month);

    ExamineCount selectPunishCountByCityDistrict(@Param("district")String district, @Param("month")String month);
}