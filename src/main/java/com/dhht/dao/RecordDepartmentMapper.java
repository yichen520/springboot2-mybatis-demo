package com.dhht.dao;

import com.dhht.model.RecordDepartment;
import com.dhht.model.RecordDepartment;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordDepartmentMapper {
    int countByExample(RecordDepartment example);

    int deleteByExample(RecordDepartment example);

    int deleteByPrimaryKey(String departmentCode);

    int insert(RecordDepartment record);

    int insertSelective(RecordDepartment record);

    List<RecordDepartment> selectByExample(RecordDepartment example);

    RecordDepartment selectByPrimaryKey(String departmentCode);

    int updateByExampleSelective(@Param("record") RecordDepartment record, @Param("example") RecordDepartment example);

    int updateByExample(@Param("record") RecordDepartment record, @Param("example") RecordDepartment example);

    int updateByPrimaryKeySelective(RecordDepartment record);

    int updateByPrimaryKey(RecordDepartment record);

    //根据区域id查询下面的制作单位
    List<RecordDepartment> selectByDistrictId(String id);
}