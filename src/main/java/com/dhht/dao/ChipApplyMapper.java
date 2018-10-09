package com.dhht.dao;

import com.dhht.model.ChipApply;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChipApplyMapper {
    int deleteByPrimaryKey(String id);

    int insert(ChipApply record);

    int insertSelective(ChipApply record);

    ChipApply selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChipApply record);

    int updateByPrimaryKey(ChipApply record);

    List<ChipApply> selectByApplyTime(@Param("startTime") String startTime,@Param("endTime") String endTime, @Param("departmentCode") String departmentCode);

    List<ChipApply> selectChipApplyMakedepartment(String districtId);

    List<ChipApply> selectChipGrant(@Param("districtId") String districtId,@Param("makeDepartment") String makeDepartment,@Param("grantFlag") int grantFlag, @Param("startTime")String startTime,@Param("endTime")String endTime);

    List<ChipApply> selectChipGrantMakedepartment(String districtId);

    int updateChipNum(@Param("chipApplyId")String chipApplyId,@Param("num") int i,@Param("ungrantnum") int ungrantnum);
}