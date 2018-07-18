package com.dhht.dao;

import com.dhht.model.ExamineRecord;
import com.dhht.model.OfficeCheck;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamineRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(ExamineRecord record);

    int insertSelective(ExamineRecord record);

    ExamineRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ExamineRecord record);

    int updateByPrimaryKey(ExamineRecord record);

    List<ExamineRecord> findPunish(@Param("makedepartmentName") String makedepartmentName, @Param("startTime") String startTime, @Param("endTime")String endTime, @Param("district")String districtId);
}