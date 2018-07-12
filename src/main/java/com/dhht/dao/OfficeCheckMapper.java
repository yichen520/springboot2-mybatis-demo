package com.dhht.dao;

import com.dhht.model.OfficeCheck;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficeCheckMapper {
    int deleteByPrimaryKey(String makedepartmentCode);

    int insert(OfficeCheck record);

    int insertSelective(OfficeCheck record);

    OfficeCheck selectByPrimaryKey(String makedepartmentCode);

    int updateByPrimaryKeySelective(OfficeCheck record);

    int updateByPrimaryKey(OfficeCheck record);

   List<OfficeCheck> findPunish(@Param("makedepartmentName") String makedepartmentName,@Param("startTime") String startTime, @Param("endTime")String endTime, @Param("district")String districtId);
}