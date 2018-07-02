package com.dhht.dao;


import com.dhht.model.UseDepartment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * Created by imac_dhht on 2018/6/12.
 */
@Repository
public interface UseDepartmentDao {

    int delete(String flag);

    int insert(UseDepartment record);

    UseDepartment selectByCode(String usedepartmentCode);

    UseDepartment selectById(String Id);

//    int updateByPrimaryKeySelective(UserDepartment record);

    int updateByPrimaryKey(UseDepartment record);

    List<UseDepartment> findAllMake();

    List<UseDepartment> selectByFlag(String flag);

    List<UseDepartment> find(@Param("code") String code,@Param("districtIde")String districtId,@Param("name")String name,@Param("departmentStatus")String departmentStatus);
}
