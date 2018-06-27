package com.dhht.dao;

import com.dhht.model.RecordDepartment;
import com.dhht.model.RecordDepartment;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordDepartmentMapper {


    int deleteByPrimaryKey(String departmentCode);

    //添加备案单位
    int insert(RecordDepartment record);

    int updateByPrimaryKeySelective(RecordDepartment record);

    int updateByPrimaryKey(RecordDepartment record);

    //根据区域id查询下面的备案单位
    List<RecordDepartment> selectByDistrictId(@Param("id") String id);

    //查询所有的的备案单位
    List<RecordDepartment> selectAllRecordDepartment();

    //根据备案单位编号查询备案单位
    RecordDepartment selectByCode(String code);
}