package com.dhht.dao;


import com.dhht.model.RecordDepartment;
import com.dhht.model.UseDepartment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * Created by imac_dhht on 2018/6/12.
 */
@Repository
public interface UseDepartmentDao {

    int deleteById(@Param("id")String id);

    int delete(@Param("id") String id);

    List<UseDepartment> selectByDepartmentStatus(@Param("departmentStatus") String departmentStatus);

    int insert(UseDepartment useDepartment);

    UseDepartment selectByCode(String useDepartmentCode);

    UseDepartment selectById(String Id);

    int updateByPrimaryKey(UseDepartment record);



    List<UseDepartment> findAllMake();

    List<UseDepartment> selectByFlag(@Param("flag") String flag);

    int updateById(UseDepartment useDepartment);

    List<UseDepartment> find(@Param("code") String code,@Param("districtId")String districtId,@Param("name")String name,@Param("departmentStatus")String departmentStatus);

    List<UseDepartment> selectByName(@Param("name") String name);

    UseDepartment selectDetailById(@Param("id") String id);

    //统计部分
    int indexCountAdd(@Param("districtId") String districtId);

    int indexCountDel(@Param("districtId") String districtId);

    int countAllDepartment(@Param("districtId") String districtId);

    int countDelDepartment(@Param("districtId") String districtId);

    int countWorkDepartment(@Param("districtId") String districtId);

    int countAddByStartTime(@Param("districtId") String districtId,@Param("startTime") String startTime);

    int countAddByEndTime(@Param("districtId") String districtId,@Param("endTime") String endTime);

    int countAddByTime(@Param("districtId") String districtId,@Param("startTime") String startTime,@Param("endTime") String endTime);

    int countDelByStartTime(@Param("districtId") String districtId,@Param("startTime") String startTime);

    int countDelByEndTime(@Param("districtId") String districtId,@Param("endTime") String endTime);

    int countDelByTime(@Param("districtId") String districtId,@Param("startTime") String startTime,@Param("endTime") String endTime);

}
