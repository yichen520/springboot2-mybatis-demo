package com.dhht.dao;

import com.dhht.model.RecordDepartment;
import com.dhht.model.RecordDepartment;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordDepartmentMapper {


    int deleteByPrimaryKey(String departmentCode);
   //更新老历史记录状态
   int   updateDeleteFlag(@Param("id") String id);

    //根据Id查找备案单位
    RecordDepartment selectById(@Param("id") String id);
    //添加备案单位
    int insert(RecordDepartment record);

    //删除备案单位
    int deleteById(@Param("id") String id);

   // int updateByPrimaryKeySelective(RecordDepartment record);

    //修改备案单位
    int updateById(RecordDepartment record);

    //根据区域id查询下面的备案单位
    List<RecordDepartment> selectByDistrictId(@Param("id") String id);

    //查询所有的的备案单位
    List<RecordDepartment> selectAllRecordDepartment();

    //根据备案单位编号查询备案单位
    RecordDepartment selectByCode(String code);

    List<RecordDepartment>  selectByFlag(@Param("flag") String flag);

    int validateCode(@Param("code") String code);

   RecordDepartment selectByPhone(@Param("phone")String phone);

    RecordDepartment selectByDistrictIdVersion(@Param("localDistrictId") String localDistrictId);

   RecordDepartment selectBydistrict(@Param("districtId") String districtId);

}