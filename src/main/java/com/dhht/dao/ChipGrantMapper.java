package com.dhht.dao;

import com.dhht.model.ChipGrant;
import com.dhht.model.pojo.ChipCountVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChipGrantMapper {
    int deleteByPrimaryKey(String id);

    int insert(ChipGrant record);

    int insertSelective(ChipGrant record);

    ChipGrant selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChipGrant record);

    int updateByPrimaryKey(ChipGrant record);

    List<ChipCountVO> selectGrantRecord(@Param("department")String department, @Param("startTime")String startTime, @Param("endTime")String endTime, @Param("districtid")String districtid);

    List<ChipGrant> selectChipGrantinfo(@Param("districtId")String districtId, @Param("makeDepartment")String makeDepartment,  @Param("startTime")String startTime, @Param("endTime")String endTime,@Param("receiver") String receiver);
}