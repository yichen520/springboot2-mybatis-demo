package com.dhht.dao;

import com.dhht.model.Employee;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDao {

    int delete(Employee employee);

    int deleteById(@Param("id") String id);

    int selectCountEmployeeId(@Param("employeeId") String employeeId);

    List<Employee> selectAllEmployee(@Param("code") String code,@Param("name") String name);

    List<Employee> selectWorkEmployee(@Param("code") String code,@Param("name") String name);

    List<Employee> selectDeleteEmployee(@Param("code") String code,@Param("name") String name);

    int insert(Employee employee);

    Employee selectById(@Param("id") String id);

    int deleteByDepartmentCode(@Param("employeeDepartmentCode") String code);

    int update(Employee record);

    //查询某个制作单位下的从业人员
    List<Employee> selectByDepartmentCode(@Param("employeeDepartmentCode") String employeeDepartmentCode);

    List<Employee> selectHistory(@Param("flag") String flag);

    List<Employee> selectWorkEmployee(@Param("employeeDepartmentCode") String employeeDepartmentCode);

    Employee selectByPhone(@Param("phone") String phone);

    int updateHeadById(@Param("id") String id,@Param("employeeImage") String image);



}