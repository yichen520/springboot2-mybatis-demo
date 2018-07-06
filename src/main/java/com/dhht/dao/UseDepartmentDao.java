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

    UseDepartment selectByCode(String usedepartmentCode);

    UseDepartment selectById(String Id);

    int updateByPrimaryKey(UseDepartment record);

    List<UseDepartment> findAllMake();

    List<UseDepartment> selectByFlag(@Param("flag") String flag);

    int updateById(UseDepartment useDepartment);

    List<UseDepartment> find(@Param("code") String code,@Param("districtId")String districtId,@Param("name")String name,@Param("departmentStatus")String departmentStatus);

    List<UseDepartment> selectByName(@Param("name") String name);
}
