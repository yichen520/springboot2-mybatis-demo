package com.dhht.dao;

import com.dhht.model.ExamineRecord;
import com.dhht.model.MakePunishRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MakePunishRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(MakePunishRecord record);

    int insertSelective(MakePunishRecord record);

    MakePunishRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MakePunishRecord record);

    int updateByPrimaryKey(MakePunishRecord record);

    List<MakePunishRecord> findPunish(@Param("makedepartmentName") String makedepartmentName, @Param("startTime") String startTime, @Param("endTime")String endTime, @Param("district")String districtId);
}